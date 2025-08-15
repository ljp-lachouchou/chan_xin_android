package com.software.jetpack.compose.chan_xin_android.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.software.jetpack.compose.chan_xin_android.entity.ChatLog
import com.software.jetpack.compose.chan_xin_android.entity.MessageFrame
import com.software.jetpack.compose.chan_xin_android.ext.toTime
import com.software.jetpack.compose.chan_xin_android.http.service.HttpService
import com.software.jetpack.compose.chan_xin_android.repo.ImRepository
import com.software.jetpack.compose.chan_xin_android.ui.activity.LoginScreen
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.vm.WebsocketManager.WebsocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ImViewModel @Inject constructor(imRepository: ImRepository):ViewModel() {
    private val apiService = HttpService.getService()
    private val _currentConversationId = MutableStateFlow(ImRepository.UserIdWithVersion("",0))
    private var version = 0
    private val pagingConfig = PagingConfig(
        pageSize = 6, // 每页加载数量
        initialLoadSize = 4,
        enablePlaceholders = false // 不启用占位符（适合网络数据）
    )
    init {
        initWorkManager()
    }
    private fun initWorkManager() {
        viewModelScope.launch {
            val token = AppGlobal.tokenIsAva()
            withContext(Dispatchers.Main) {
                websocketManager = WebsocketManager("ws://114.215.194.88:9080/ws",token)
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
    val chatLogFlow = _currentConversationId.flatMapLatest {(cid,_)->
        Pager(config = pagingConfig, pagingSourceFactory = {ChatLogPagingSource(cid)}).flow.cachedIn(viewModelScope)
    }.catch { Log.e("ImViewModel_chatLogFlow", it.toString()) }
    inner class ChatLogPagingSource(
        private val conversationId: String,
        private val startTime: Long = 0,
        private val endTime: Long = System.currentTimeMillis() / 1000,
        private val msgId: String = ""
    ) :
        PagingSource<Long,ChatLog>() {
        override fun getRefreshKey(state: PagingState<Long, ChatLog>): Long? {
            return System.currentTimeMillis() / 1000
        }

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ChatLog> {
            return try {
                val currentRequestKey = params.key ?: endTime
                val apiResponse = apiService.getChatLog(startTime,currentRequestKey,params.loadSize,conversationId,msgId)
                val chatLogs = apiResponse.data?.list ?: emptyList()
                val nextPageKey = if (chatLogs.isNotEmpty()) {
                    chatLogs.minOfOrNull { it.sendTime }
                } else {
                    null
                }
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
}