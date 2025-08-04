package com.software.jetpack.compose.chan_xin_android.vm

import android.util.Log
import android.widget.Toast
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LOGGER
import com.google.gson.Gson
import com.software.jetpack.compose.chan_xin_android.cache.dao.ISocialDao
import com.software.jetpack.compose.chan_xin_android.cache.dao.IUserDao
import com.software.jetpack.compose.chan_xin_android.cache.database.UserDatabase
import com.software.jetpack.compose.chan_xin_android.entity.Friend
import com.software.jetpack.compose.chan_xin_android.entity.FriendApply
import com.software.jetpack.compose.chan_xin_android.entity.FriendStatus
import com.software.jetpack.compose.chan_xin_android.entity.User
import com.software.jetpack.compose.chan_xin_android.http.entity.ApiResult
import com.software.jetpack.compose.chan_xin_android.http.service.ApiService
import com.software.jetpack.compose.chan_xin_android.http.service.ApiService.FriendApplyResponse
import com.software.jetpack.compose.chan_xin_android.http.service.HttpService
import com.software.jetpack.compose.chan_xin_android.repo.SocialRepository
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor(private val socialRepository:SocialRepository):ViewModel(){
    private val _wantApplyFriend = MutableStateFlow(FriendApply())
    private val _currentFriendList = MutableStateFlow<List<Friend>>(emptyList())
    private val _clickFriend = MutableStateFlow(Friend())
    private val _currentGroup = MutableStateFlow<List<Pair<String, List<Friend>>>>(emptyList())
    private val _currentSelectFriendList = MutableStateFlow<List<Friend>>(emptyList())
    private val _currentAbandonFriendList = MutableStateFlow<List<Friend>>(emptyList())
    private val apiService = HttpService.getService()
    val applyFriendList = socialRepository.currentApplyFriendListFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val handleFriendApplyList = socialRepository.currentHandleFriendApplyListFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val friendCacheList = socialRepository.currentFriendListFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val currentSelectFriendList:StateFlow<List<Friend>>
        get() = _currentSelectFriendList
    val currentAbandonFriendList:StateFlow<List<Friend>>
        get() = _currentAbandonFriendList
    val currentGroup: StateFlow<List<Pair<String, List<Friend>>>>
        get() = _currentGroup
    val wantApplyFriend:StateFlow<FriendApply>
        get() = _wantApplyFriend
    val currentFriendList:StateFlow<List<Friend>>
        get() = _currentFriendList
    val clickFriend:StateFlow<Friend>
        get() = _clickFriend
    fun loadWantApplyFriend(user:FriendApply) {
        _wantApplyFriend.value = user
    }
    fun loadClickFriend(friend:Friend) {
        _clickFriend.value = friend
    }
    fun loadCurrentGroup(group:List<Pair<String, List<Friend>>>) {
        _currentGroup.value = group
    }
    fun loadCurrentFriendList(list:List<Friend>) {
        _currentFriendList.value = list
    }
    fun loadCurrentSelectFriendList(list:List<Friend>) {
        _currentSelectFriendList.value = list
    }
    fun loadCurrentAbandonFriendList(list:List<Friend>) {
        _currentAbandonFriendList.value = list
    }
    suspend fun applyFriend(userId:String="2",targetId:String="1",greetMsg:String="1"): FriendApplyResponse? {
        val applyFriend =
            apiService.applyFriend(ApiService.FriendApplyRequest(userId, targetId, greetMsg))
        return applyFriend.data
    }
    suspend fun getFriendApplyList(uid:String):List<FriendApply> {
        try {
            return apiService.getFriendApplyList(uid).data?.list ?: emptyList()
        } catch (e: Exception) {
            return emptyList()
        }
    }
    suspend fun getHandleFriendApplyList(tid:String):List<FriendApply> {
        try {
            return apiService.getHandleFriendApplyList(tid).data?.list ?: emptyList()
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    AppGlobal.getAppContext(),
                    "网络可能有些问题",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return emptyList()
        }
    }

    suspend fun handleFriendApply(applicantId:String="1",targetId:String="1",isApproved:Boolean) {
        try {
            apiService.handleFriendApply(
                ApiService.FriendApplyAction(
                    applicantId,
                    targetId,
                    isApproved
                )
            )
            withContext(Dispatchers.Main) {
                Toast.makeText(AppGlobal.getAppContext(),"操作成功",Toast.LENGTH_SHORT).show()
            }
        }catch (e:HttpException) {
            if (e.response()?.errorBody() != null) {
                val body = e.response()?.errorBody()!!.string()
                val obj = Gson().fromJson(body,ApiResult::class.java)
                withContext(Dispatchers.Main) {
                    Toast.makeText(AppGlobal.getAppContext(),obj.msg,Toast.LENGTH_SHORT).show()
                }
            }else {
                Log.e("okhttp0","okhttp0")
                withContext(Dispatchers.Main) {
                    Toast.makeText(AppGlobal.getAppContext(),"网络有些问题",Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e:Exception){
            Log.e("okhttp1",e.toString())
            withContext(Dispatchers.Main) {
                Toast.makeText(AppGlobal.getAppContext(),"网络有些问题",Toast.LENGTH_SHORT).show()
            }
        }
    }
    suspend fun updateFriendStatus(userId: String,friendId:String,friendStatus: FriendStatus = FriendStatus(false,false,false,"")) {
        try {
            apiService.updateFriendStatus(ApiService.UpdateFriendStatus(userId,friendId,friendStatus))
        }catch (e:Exception) {
            Log.e("fuck_updateFriendStatus",e.toString())
            withContext(Dispatchers.Main) {
                Toast.makeText(AppGlobal.getAppContext(),"网络有些问题",Toast.LENGTH_SHORT).show()
            }
        }
    }
    suspend fun getFriendList(uid:String="1"):List<Friend> {
        try {
            val apiResult = apiService.getFriendList(uid)
            Log.e("apiResult.data?.list",apiResult.data?.list.toString())
            return apiResult.data?.list ?: emptyList()
        }catch (e:Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(AppGlobal.getAppContext(),"网络异常,获取好友列表失败",Toast.LENGTH_SHORT).show()
            }
            return emptyList()
        }
    }

    private val friendInfoCache = mutableMapOf<Pair<String, String>, StateFlow<Friend>>()

    /**
     * 获取好友信息（返回StateFlow，避免UI抖动）
     * @param uid 当前用户ID
     * @param friendId 好友ID
     */
    fun getFriendInfo(uid: String, friendId: String): StateFlow<Friend> {
        val cacheKey = uid to friendId
        val userDao = UserDatabase.getInstance().userDao()
        val socialDao = UserDatabase.getInstance().socialDao()
        return friendInfoCache.getOrPut(cacheKey) {
            // 创建StateFlow的源头Flow
            val sourceFlow = if (uid == friendId) {
                getUserSelfInfoFlow(userDao)
            } else {
                getRemoteFriendInfoFlow(uid, friendId,socialDao)
            }

            sourceFlow
                .catch { e ->
                    Log.e("FriendVM", "获取好友信息异常", e)
                    emit(Friend())
                }
                .flowOn(Dispatchers.IO)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = Friend()
                )
        }
    }

    /**
     * 获取当前用户自己的信息（Flow）
     */
    private fun getUserSelfInfoFlow(userDao:IUserDao) = flow {
        val userPhone = AppGlobal.getUserPhone()
        val user = userDao.getUserInfoByPhone(userPhone).first()
        emit(
            Friend(
                userId = user.id,
                nickname = user.nickname,
                avatarUrl = user.avatar,
                gender = user.sex.toInt()
            )
        )
    }

    /**
     * 获取好友信息（本地+网络）
     */
    private fun getRemoteFriendInfoFlow(uid: String, friendId: String,socialDao:ISocialDao) = flow {
        // 先发射本地缓存（立即响应UI）
        val localFriend = socialDao.getFriendInfo(uid, friendId).firstOrNull()// 获取当前缓存
        if (localFriend != null) {
            emit(localFriend) // 标记为非加载状态
        }

        // 网络可用时请求最新数据
        if (AppGlobal.isNetworkValid()) {
            val remoteFriend = apiService.getFriendInfo(uid, friendId).data
                ?: throw NullPointerException("服务器返回数据为空")

            emit(remoteFriend)
        } else {
            // 无网络且无本地缓存，发射默认值
            if (localFriend == null) {
                emit(Friend())
            }
        }
    }


}