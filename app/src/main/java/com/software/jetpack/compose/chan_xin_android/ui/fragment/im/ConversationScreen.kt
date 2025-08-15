package com.software.jetpack.compose.chan_xin_android.ui.fragment.im

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.software.jetpack.compose.chan_xin_android.http.service.HttpService
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel

@Composable
fun ConversationScreen(navHostController: NavHostController) {
    val activity = LocalContext.current as Activity
    // 拦截返回键，直接退出应用
    BackHandler(enabled = true) {
        activity.moveTaskToBack(true) // 切换到后台
    }
    ConversationListScreen(navHostController)
}
@Composable
fun ConversationListScreen(navHostController: NavHostController) {
    val uvm:UserViewmodel = hiltViewModel()
    val user by uvm.myUser.collectAsState()
    LaunchedEffect(user.id) {
        if (user.id != "") {
            try {
                HttpService.getService().getConversations(user.id)
            }catch (e:Exception) {
                Log.e("ConversationListScreen",e.toString())
            }
        }
    }
}

@Composable
fun ConversationItem() {

}