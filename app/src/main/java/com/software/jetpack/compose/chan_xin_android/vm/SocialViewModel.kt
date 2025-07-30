package com.software.jetpack.compose.chan_xin_android.vm

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LOGGER
import com.google.gson.Gson
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
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


}