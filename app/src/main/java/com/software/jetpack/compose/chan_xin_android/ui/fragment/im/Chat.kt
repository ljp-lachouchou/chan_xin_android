package com.software.jetpack.compose.chan_xin_android.ui.fragment.im

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.entity.MessageContent
import com.software.jetpack.compose.chan_xin_android.entity.MessageData
import com.software.jetpack.compose.chan_xin_android.entity.MessageFrame
import com.software.jetpack.compose.chan_xin_android.ui.fragment.friend.TopBarWithBack
import com.software.jetpack.compose.chan_xin_android.vm.ImViewModel
import com.software.jetpack.compose.chan_xin_android.vm.SocialViewModel

@Composable
fun ChatScreen(navHostController: NavHostController,svm:SocialViewModel,ivm:ImViewModel) {
    val clickFriend by svm.clickFriend.collectAsState()
    /**
     *
     * ivm.send(MessageFrame(fromId = "0x000000900000000f", toId = "0x000000d000000003", data = MessageData("0x000000900000000f", recvId = "0x000000d000000003",0, msg = MessageContent(1,"你好呀"))))
     */
    Scaffold(topBar = { TopBarWithBack(navHostController,clickFriend.displayName, action = {
        ChatScreenAction()
    }) }){ paddingValues ->
        ChatScreenUI(modifier = Modifier.padding(paddingValues))
    }
}
@Composable
fun ChatScreenUI(modifier: Modifier=Modifier) {

}
@Composable
fun ChatScreenAction() {
    Icon(
        painterResource(R.drawable.more),
        contentDescription = null,
        tint = Color.Black,
        modifier = Modifier.size(24.dp)
    )
}