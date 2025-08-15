package com.software.jetpack.compose.chan_xin_android.ui.fragment.im

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserPadding
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserScreenItemDp
import com.software.jetpack.compose.chan_xin_android.entity.ChatLog
import com.software.jetpack.compose.chan_xin_android.entity.Conversation
import com.software.jetpack.compose.chan_xin_android.entity.Friend
import com.software.jetpack.compose.chan_xin_android.entity.MessageContent
import com.software.jetpack.compose.chan_xin_android.entity.MessageData
import com.software.jetpack.compose.chan_xin_android.entity.MessageFrame
import com.software.jetpack.compose.chan_xin_android.entity.User
import com.software.jetpack.compose.chan_xin_android.ext.switchTab
import com.software.jetpack.compose.chan_xin_android.ui.activity.MainActivityRouteEnum
import com.software.jetpack.compose.chan_xin_android.ui.activity.Wrapper
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.base.BubbleDirection
import com.software.jetpack.compose.chan_xin_android.ui.base.ChatBubble
import com.software.jetpack.compose.chan_xin_android.ui.base.ChatBubblePreview
import com.software.jetpack.compose.chan_xin_android.ui.base.CustomTextField
import com.software.jetpack.compose.chan_xin_android.ui.fragment.friend.TopBarWithBack
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.SurfaceColor
import com.software.jetpack.compose.chan_xin_android.vm.ImViewModel
import com.software.jetpack.compose.chan_xin_android.vm.SocialViewModel
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel

@Composable
fun ChatScreen(navHostController: NavHostController,svm:SocialViewModel,ivm:ImViewModel) {
    val clickFriend by svm.clickFriend.collectAsState()
    /**
     *
     * ivm.send(MessageFrame(fromId = "0x000000900000000f", toId = "0x000000d000000003", data = MessageData("0x000000900000000f", recvId = "0x000000d000000003",0, msg = MessageContent(1,"你好呀"))))
     */
    Scaffold(topBar = {
        TopBarWithBack(
            navHostController,
            clickFriend.displayName,
            color = SurfaceColor,
            isBold = true,
            action = {
                ChatScreenAction()
            })
    }){ paddingValues ->
        ChatScreenUI(modifier = Modifier.padding(paddingValues),clickFriend,ivm)
    }
}
@Composable
fun ChatScreenUI(modifier: Modifier=Modifier,friend:Friend,ivm: ImViewModel) {
    val chatLogs = ivm.chatLogFlow.collectAsLazyPagingItems()
    val uvm:UserViewmodel = hiltViewModel()
    val user by uvm.myUser.collectAsState()
    Box (modifier=modifier.fillMaxSize().imePadding()){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = DefaultUserPadding, end = DefaultUserPadding, bottom = 105.dp),
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ){
            items(chatLogs.itemSnapshotList.items) {chatLog->
                ChatLogItem(user,friend,chatLog)
            }
        }
        Wrapper(modifier=Modifier.align(Alignment.BottomStart)) {
            BottomChatArea()
        }
    }
}
@Composable
fun ChatLogItem(user:User,friend:Friend,chatLog: ChatLog,modifier:Modifier=Modifier.fillMaxWidth()) {
    val direction = if (chatLog.sendId == friend.userId) BubbleDirection.LEFT else BubbleDirection.RIGHT
    val horizontalArrangement = if (chatLog.sendId == friend.userId) Arrangement.Start else Arrangement.End
    val context = LocalContext.current
    Row(horizontalArrangement = horizontalArrangement, verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        when(direction) {
            BubbleDirection.LEFT -> {
                AsyncImage(
                    ImageRequest.Builder(context).data(friend.displayAvatar).build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(
                            RoundedCornerShape(8.dp)
                        ),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier=Modifier.width(5.dp))
                ChatBubble(chatLog.msgContent, direction = direction)
            }
            BubbleDirection.RIGHT -> {
                ChatBubble(chatLog.msgContent, direction = direction)
                Spacer(modifier=Modifier.width(5.dp))
                AsyncImage(
                    ImageRequest.Builder(context).data(user.displayAvatar).build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(
                            RoundedCornerShape(8.dp)
                        ),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
@Composable
fun BottomChatArea() {
    var find by remember { mutableStateOf("") }
    Surface(color = SurfaceColor, modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)) {
        Box(modifier = Modifier.height(50.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.padding(horizontal = DefaultUserPadding, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                CustomTextField(
                    value = find,
                    onValueChange = { find = it },
                    colors = TextFieldDefaults.colors(
                        cursorColor = IconGreen,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    defaultVerticalPadding = 5.dp,
                    modifier = Modifier.height(45.dp)
                )
                BaseText("表情")
                Icon(
                    painterResource(R.drawable.add_outline),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    }
}
@Composable
fun ChatScreenAction() {
    Icon(
        painterResource(R.drawable.more),
        contentDescription = null,
        tint = Color.Black,
        modifier = Modifier
            .size(24.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
            }
    )

}
