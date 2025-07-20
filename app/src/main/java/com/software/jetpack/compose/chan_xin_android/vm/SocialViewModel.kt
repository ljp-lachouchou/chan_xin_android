package com.software.jetpack.compose.chan_xin_android.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.software.jetpack.compose.chan_xin_android.entity.Friend
import com.software.jetpack.compose.chan_xin_android.entity.FriendApply
import com.software.jetpack.compose.chan_xin_android.entity.User
import com.software.jetpack.compose.chan_xin_android.http.service.ApiService
import com.software.jetpack.compose.chan_xin_android.http.service.ApiService.FriendApplyResponse
import com.software.jetpack.compose.chan_xin_android.http.service.HttpService
import com.software.jetpack.compose.chan_xin_android.repo.SocialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor(private val socialRepository:SocialRepository):ViewModel(){
    private val _wantApplyFriend = MutableStateFlow(User())
    private val _friendList = MutableStateFlow(listOf<Friend>())
    val friendList:StateFlow<List<Friend>>
        get() = _friendList
    val applyFriendList = socialRepository.currentApplyFriendListFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val wantApplyFriend:StateFlow<User>
        get() = _wantApplyFriend
    fun loadWantApplyFriend(user:User) {
        _wantApplyFriend.value = user
    }
    suspend fun applyFriend(userId:String="2",targetId:String="1",greetMsg:String="1"): FriendApplyResponse? {
        val apiService = HttpService.getService()
        val applyFriend =
            apiService.applyFriend(ApiService.FriendApplyRequest(userId, targetId, greetMsg))
        return applyFriend.data
    }
    suspend fun getFriendApplyList(uid:String):List<FriendApply> {
        val apiService = HttpService.getService()
        return apiService.getFriendApplyList(uid).data?.list ?: emptyList()
    }


}