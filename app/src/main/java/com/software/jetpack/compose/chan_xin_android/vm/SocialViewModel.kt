package com.software.jetpack.compose.chan_xin_android.vm

import androidx.lifecycle.ViewModel
import com.software.jetpack.compose.chan_xin_android.entity.Friend
import com.software.jetpack.compose.chan_xin_android.http.service.ApiService
import com.software.jetpack.compose.chan_xin_android.http.service.ApiService.FriendApplyResponse
import com.software.jetpack.compose.chan_xin_android.http.service.HttpService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor():ViewModel(){
    private val _friendList = MutableStateFlow(listOf<Friend>())
    val friendList:StateFlow<List<Friend>>
        get() = _friendList

    suspend fun applyFriend(userId:String="2",targetId:String="1",greetMsg:String="1"): FriendApplyResponse? {
        val apiService = HttpService.getService()
        val applyFriend =
            apiService.applyFriend(ApiService.FriendApplyRequest(userId, targetId, greetMsg))
        return applyFriend.data
    }


}