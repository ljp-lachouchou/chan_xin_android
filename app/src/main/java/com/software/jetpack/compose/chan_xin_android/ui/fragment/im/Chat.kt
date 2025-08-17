package com.software.jetpack.compose.chan_xin_android.ui.fragment.im

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
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
import com.software.jetpack.compose.chan_xin_android.ui.activity.IconButton
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val InputFieldHeight = 45.dp
private val ButtonPadding = 8.dp
private val ButtonIconSize = 24.dp
@Composable
fun ChatScreen(navHostController: NavHostController,svm:SocialViewModel,ivm:ImViewModel) {
    val clickFriend by svm.clickFriend.collectAsState()
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
    val chatLogsItems by ivm.chatLogListByConversationId().collectAsState()
    val uvm:UserViewmodel = hiltViewModel()
    val user by uvm.myUser.collectAsState()
    val listState = rememberLazyListState()
    LaunchedEffect(chatLogsItems.size) {
        listState.animateScrollToItem(0)
    }
    Column(modifier=modifier
        .fillMaxSize()
        .imePadding()
    ){
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = DefaultUserPadding, end = DefaultUserPadding),
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ){
            items(chatLogsItems, key = {item->item.id}) { chatLog->
                Wrapper {
                    ChatLogItem(user,friend,chatLog)
                }
            }
        }
        Wrapper {
            BottomChatArea(onSend = {content->
                ivm.send(
                    MessageFrame(
                        toId = friend.userId,
                        fromId = user.id,
                        data = MessageData(
                            sendId = user.id,
                            recvId = friend.userId,
                            chatType = 0,
                            msg = MessageContent(0, content)
                        )
                    )
                )

            }, onEmojiClick = {}, onAddClick = {})
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
fun BottomChatArea(onSend:(String)->Unit,onEmojiClick:()->Unit,onAddClick:()->Unit) {
    var find by remember { mutableStateOf("") }
    Surface(
        color = SurfaceColor,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(top = 10.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DefaultUserPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomTextField(
                    value = find,
                    onValueChange = { find = it },
                    colors = TextFieldDefaults.colors(
                        cursorColor = IconGreen,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White
                    ),
                    defaultVerticalPadding = 5.dp,
                    modifier = Modifier
                        .weight(1f)
                        .height(InputFieldHeight)
                        .border(
                            width = 1.dp, color = Color(0xFFE0E0E0)
                        ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (find.isNotBlank()) onSend(find)
                            find = ""
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Send,
                        keyboardType = KeyboardType.Text
                    ),
                    maxLines = 1,
                    singleLine = true
                )

                IconButton(
                    onClick = onEmojiClick,
                    modifier = Modifier
                        .size(InputFieldHeight)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(ButtonPadding)
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "表情",
                        modifier = Modifier.size(ButtonIconSize)
                    )
                }

                IconButton(
                    onClick = onAddClick,
                    modifier = Modifier
                        .size(InputFieldHeight)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(ButtonPadding)
                ) {
                    Icon(
                        painterResource(R.drawable.add_outline),
                        contentDescription = "添加",
                        tint = Color.Black,
                        modifier = Modifier.size(ButtonIconSize)
                    )
                }
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
            .clickable(indication = null,
                interactionSource = remember { MutableInteractionSource() }) {}
    )

}
