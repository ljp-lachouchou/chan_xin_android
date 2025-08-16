package com.software.jetpack.compose.chan_xin_android.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.software.jetpack.compose.chan_xin_android.entity.ChatLog
import com.software.jetpack.compose.chan_xin_android.entity.MessageFrame
import com.software.jetpack.compose.chan_xin_android.http.service.HttpService
import com.software.jetpack.compose.chan_xin_android.repo.ImRepository
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.vm.WebsocketManager.ChatLogRemoteMediator
import com.software.jetpack.compose.chan_xin_android.vm.WebsocketManager.OnWebSocketMessageListener
import com.software.jetpack.compose.chan_xin_android.vm.WebsocketManager.WebsocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@HiltViewModel
class ImViewModel @Inject constructor(private val imRepository: ImRepository):ViewModel(),OnWebSocketMessageListener {
    private val apiService = HttpService.getService()
    private val _currentConversationId = MutableStateFlow(ImRepository.UserIdWithVersion("",0))
    private var version = 0
    private val pagingConfig = PagingConfig(
        pageSize = 15, // 每页加载数量
        initialLoadSize = 15,
        enablePlaceholders = false // 不启用占位符（适合网络数据）
    )
    private val chatLogMap = ConcurrentHashMap<String,MutableStateFlow<Flow<PagingData<ChatLog>>>>()
    init {
        initWorkManager()
    }
    private fun initWorkManager() {
        viewModelScope.launch {
            val token = AppGlobal.tokenIsAva()
            withContext(Dispatchers.Main) {
                websocketManager = WebsocketManager("ws://114.215.194.88:9080/ws",token,this@ImViewModel)
                websocketManager?.connect()
            }
        }
    }
    private var websocketManager:WebsocketManager? = null
    fun send(messageFrame: MessageFrame) {
        websocketManager?.sendMessage(messageFrame)
    }
    fun connect() {
        if (isConnect) return
        websocketManager?.connect() ?: {
            initWorkManager()
        }
    }
    val isConnect:Boolean
        get() = websocketManager?.isConnect ?: false
    val currentConversation = imRepository.currentConversationFlow.stateIn(
        scope = viewModelScope,
        initialValue = emptyMap(),
        started = SharingStarted.WhileSubscribed(5000)
    )
    @OptIn(ExperimentalCoroutinesApi::class)
    val chatLogListFlow = _currentConversationId.flatMapLatest { (cid,_)->
        if (AppGlobal.isNetworkValid()) {
            flow {
                emit(apiService.getChatLog(conversationId = cid, count = 1000, endSendTime = System.currentTimeMillis(), msgId = "", startSendTime = 0L).data?.list ?: emptyList())
            }
        }else {
            imRepository.chatDao.getChatLogsFlow(cid, count = 1000)
        }
    }.catch { Log.e("ImViewModel_chatLogListFlow", it.toString()) }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = emptyList())
    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
    val chatLogFlow = _currentConversationId.flatMapLatest { (cid,_)->
        Pager(config = pagingConfig, remoteMediator = ChatLogRemoteMediator(cid,imRepository.chatDao,pagingConfig.pageSize),pagingSourceFactory = {ChatLogPagingSource(cid)}).flow.cachedIn(viewModelScope)
    }.catch { Log.e("ImViewModel_chatLogFlow", it.toString()) }
    inner class ChatLogPagingSource(
        private val conversationId: String,
        private val startTime: Long = 0,
        private val msgId: String = ""
    ) :
        PagingSource<Long,ChatLog>() {
        override fun getRefreshKey(state: PagingState<Long, ChatLog>): Long? {
            return state.anchorPosition?.let { anchorPosition ->
                state.closestItemToPosition(anchorPosition)?.sendTime
            } ?: run {
                System.currentTimeMillis()
            }
        }

        override val keyReuseSupported: Boolean
            get() = true
        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ChatLog> {
            return try {
                val currentRequestKey =  params.key ?: System.currentTimeMillis()
                Log.e("currentRequestKey",currentRequestKey.toString())
                Log.e("QueryRange", "查询范围：sendTime >= $startTime AND sendTime <= $currentRequestKey")
                Log.e("NextKeyDebug", "下一页加载，params.key = ${params.key}（应等于上一页的 nextKey）")
                val chatLogs = apiService.getChatLog(conversationId=conversationId,startSendTime=startTime,endSendTime=currentRequestKey, count = params.loadSize, msgId = msgId).data?.list ?: emptyList()
                Log.e("LoadData", "本次加载 ${chatLogs.size} 条数据（预期15条）")
                Log.e("remoteLogs_ss",chatLogs.toString())
                val nextPageKey = chatLogs.minOfOrNull { it.sendTime }?.minus(1)
                LoadResult.Page(chatLogs, nextKey = nextPageKey, prevKey = null)
            }catch (e:Exception) {
                Log.e("fuck_ChatLogPagingSource",e.toString())
                LoadResult.Error(e)
            }
        }

    }

    fun setConversationId(cid:String) {
        version = (version + 1) % 10
        _currentConversationId.value = ImRepository.UserIdWithVersion(cid,version)
    }
    @OptIn(ExperimentalPagingApi::class)
    private fun initChatLog(
        conversationId: String,
        flow: MutableStateFlow<Flow<PagingData<ChatLog>>>
    ) {
        flow.value = Pager(config = pagingConfig, remoteMediator = ChatLogRemoteMediator(conversationId,imRepository.chatDao,pagingConfig.pageSize),pagingSourceFactory = { ChatLogPagingSource(_currentConversationId.value.userId) }).flow.cachedIn(viewModelScope)
    }

    fun addChatLog() {
        val conversationId = _currentConversationId.value.userId
        val flow = chatLogMap[conversationId] ?:return
        Log.e("websocket_messageFrame","websocket_messageFrame_addChatLog")

    }

    fun chatLogsByConversationId():StateFlow<Flow<PagingData<ChatLog>>> {
        return chatLogMap.getOrPut(_currentConversationId.value.userId) {
            MutableStateFlow<Flow<PagingData<ChatLog>>>(flow { emit(PagingData.empty()) }).also { flow->
                initChatLog(_currentConversationId.value.userId, flow)
            }
        }
    }

    override fun onMessageReceive(messageFrame: MessageFrame) {
        Log.e("websocket_messageFrame","websocket_messageFrame22")
        addChatLog()
    }
}