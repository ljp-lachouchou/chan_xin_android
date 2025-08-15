package com.software.jetpack.compose.chan_xin_android.ui.fragment.im

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.room.util.query
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserPadding
import com.software.jetpack.compose.chan_xin_android.entity.Conversation
import com.software.jetpack.compose.chan_xin_android.entity.Friend
import com.software.jetpack.compose.chan_xin_android.entity.MessageContent
import com.software.jetpack.compose.chan_xin_android.entity.MessageData
import com.software.jetpack.compose.chan_xin_android.entity.MessageFrame
import com.software.jetpack.compose.chan_xin_android.entity.User
import com.software.jetpack.compose.chan_xin_android.ext.switchTab
import com.software.jetpack.compose.chan_xin_android.ext.toTime
import com.software.jetpack.compose.chan_xin_android.http.service.HttpService
import com.software.jetpack.compose.chan_xin_android.ui.activity.MainActivityRouteEnum
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseScreenItem
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.fragment.friend.MyTopBar
import com.software.jetpack.compose.chan_xin_android.ui.theme.PlaceholderColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.SurfaceColor
import com.software.jetpack.compose.chan_xin_android.vm.ImViewModel
import com.software.jetpack.compose.chan_xin_android.vm.SocialViewModel
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import androidx.lifecycle.compose.currentStateAsState
import com.software.jetpack.compose.chan_xin_android.R

private val CONVERSATION_ITEM_DP = 65.dp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConversationScreen(navHostController: NavHostController,ivm: ImViewModel,svm:SocialViewModel) {
    val activity = LocalContext.current as Activity
    // 拦截返回键，直接退出应用
    BackHandler(enabled = true) {
        activity.moveTaskToBack(true) // 切换到后台
    }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(Unit) {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                ivm.connect()
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
    ConversationListScreen(navHostController,ivm,svm)
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConversationListScreen(navController: NavHostController,ivm:ImViewModel,svm: SocialViewModel) {
    val uvm:UserViewmodel = hiltViewModel()
    val user by uvm.myUser.collectAsState()
    val conversations by ivm.currentConversation.collectAsState()
    val conversationList by remember(conversations) { derivedStateOf { conversations.toList() } }
    Scaffold(
        topBar = {
            MyTopBar("禅信", preContent = { BaseText("") }, action = { ChanXinAction(onCreateGroup = {

            }, onAddFriend = { navController.switchTab(MainActivityRouteEnum.FIND_USER_IN_FRIEND.route) }) })
        }
    ) { paddingValues ->
        ConversationListScreenUI(modifier = Modifier.padding(paddingValues),conversationList, user) {friend,conversation->
            svm.loadClickFriend(friend)
            ivm.setConversationId(conversation.conversationId)
            navController.switchTab(MainActivityRouteEnum.CHAT_SCREEN.route)
        }
    }
}
@Composable
fun ChanXinAction(onCreateGroup:()->Unit,onAddFriend:()->Unit) {
    var isExpand by remember { mutableStateOf(false) }
    Box {
        Icon(
            painterResource(R.drawable.add_outline),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier
                .size(24.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    isExpand = !isExpand
                }
        )
        DropdownMenu(expanded = isExpand, onDismissRequest = {isExpand = false}, modifier = Modifier.background(
            Color.Black)) {
            DropdownMenuItem(onClick = {
                onCreateGroup()
            }) { ChanXinDropdownMenuItemUI("发起群聊", R.drawable.create_group) }
            DropdownMenuItem(onClick = {
                onAddFriend()
            }) { ChanXinDropdownMenuItemUI("添加朋友", R.drawable.apply_friend) }
        }
    }
}
@Composable
fun ChanXinDropdownMenuItemUI(title:String,resId:Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(35.dp).width(100.dp)) {
        Icon(
            painterResource(resId),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.White
        )
        BaseText(title, color = Color.White)
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConversationListScreenUI(modifier:Modifier = Modifier,conversationList: List<Pair<String, Conversation>>,user:User,onClick: (Friend,Conversation) -> Unit) {
    LazyColumn(modifier = modifier,horizontalAlignment = Alignment.CenterHorizontally) {
        items(conversationList) { (conversationId,conversation)->
            val friendId = conversationId.split("_").filterNot { it== user.id}
            ConversationItem(friendId.first(),conversation,onClick={friend,cid->onClick(friend,cid)})
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConversationItem(friendId:String,conversation:Conversation,onClick: (Friend,Conversation) -> Unit) {
    val svm:SocialViewModel = hiltViewModel()
    val uvm:UserViewmodel = hiltViewModel()
    val user by uvm.myUser.collectAsState()
    val friend by svm.getFriendInfo(user.id,friendId).collectAsState()
    if (friend.userId != "") {
        ConversationItemUI(friend,conversation, onClick = {onClick(friend,conversation)})
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConversationItemUI(friend:Friend,conversation:Conversation,onClick:()->Unit) {
    val context = LocalContext.current
    Column {
        BaseScreenItem(
            backgroundColor = SurfaceColor.copy(0.1f),
            height = CONVERSATION_ITEM_DP,
            onClick = onClick,
            preContent = {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(friend.displayAvatar).build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(
                            RoundedCornerShape(5.dp)
                        ),
                    contentScale = ContentScale.Crop
                )
            },
            tailContent = {
                BaseText(conversation.msg.sendTime.toTime(), fontSize = 10.sp, color = PlaceholderColor)
            }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                BaseText(friend.displayName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                BaseText(conversation.msg.msgContent, color = PlaceholderColor, fontSize = 10.sp)
            }
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(end = 20.dp, start = DefaultUserPadding + 50.dp), thickness = 0.3.dp)
    }
}