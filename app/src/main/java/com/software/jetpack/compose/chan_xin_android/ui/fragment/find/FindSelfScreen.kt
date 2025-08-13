package com.software.jetpack.compose.chan_xin_android.ui.fragment.find

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserPadding
import com.software.jetpack.compose.chan_xin_android.entity.Friend
import com.software.jetpack.compose.chan_xin_android.entity.Post
import com.software.jetpack.compose.chan_xin_android.entity.User
import com.software.jetpack.compose.chan_xin_android.ext.switchTab
import com.software.jetpack.compose.chan_xin_android.ext.toTime
import com.software.jetpack.compose.chan_xin_android.http.service.ApiService
import com.software.jetpack.compose.chan_xin_android.ui.activity.MainActivityRouteEnum
import com.software.jetpack.compose.chan_xin_android.ui.activity.Wrapper
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.base.CanLookImage
import com.software.jetpack.compose.chan_xin_android.ui.base.CustomTextField
import com.software.jetpack.compose.chan_xin_android.ui.base.EightSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.FiveSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.FourSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.LazyColumnWithCover
import com.software.jetpack.compose.chan_xin_android.ui.base.NineSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.OneSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.SelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.SevenSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.SixSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.ThreeSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.TwoSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.fragment.TopBarWithBack
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.LittleTextColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.PlaceholderColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.SurfaceColor
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.vm.DynamicViewModel
import com.software.jetpack.compose.chan_xin_android.vm.SocialViewModel
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelfFriendCircleScreen(navController: NavHostController, svm: SocialViewModel, dvm: DynamicViewModel) {
    val uvm: UserViewmodel = hiltViewModel()
    val user by uvm.myUser.collectAsState()
    val friend by svm.clickFriend.collectAsState()
    val selfUid by remember(friend) { derivedStateOf { friend.userId } }
    val isPinPosts = dvm.isPinedSelfFlow.collectAsLazyPagingItems()
    val notPinPosts = dvm.notPinedSelfFlow.collectAsLazyPagingItems()
    Log.e("notPinPosts_notPinPosts",notPinPosts.itemSnapshotList.items.toString())
    val nickName by remember(friend) { derivedStateOf { if (selfUid==user.id) user.nickname else friend.displayName } }
    LaunchedEffect(selfUid) {
        if (selfUid != "") {
            dvm.setSelfCircleUid(selfUid)
        }
    }
    var filePath by remember { mutableStateOf("") }
    LaunchedEffect(selfUid) {
        if (selfUid != "") {
            filePath = AppGlobal.getFilePath(selfUid)
        }
    }
    val listState = rememberLazyListState()
    Box {
        LazyColumnWithCover(filePath,nickName,friend.displayAvatar, canChangeCover = false,onEnterFriendInfoDetail = {}, onChangeCover = {}, onRefresh = {}, listState = listState, verticalArrangement = Arrangement.spacedBy(5.dp)) {
            items(notPinPosts.itemSnapshotList.items) {post->
                SelfPostItem(post) {
                    dvm.loadCurrentPost(post.postId)
                    navController.switchTab(MainActivityRouteEnum.MAIN_POST_INFO.route)
                }
            }
        }
        TopBarWithBack(navController, backTint = Color.White, color = Color.Transparent, action = {
            if (selfUid == user.id) {
                Icon(
                    painterResource(R.drawable.more),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            navController.switchTab(MainActivityRouteEnum.MESSAGE_QUEUE_SCREEN.route)
                        }
                )
            }
        })
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelfPostItem(post: Post, onClick: () -> Unit) {
    val isAllText by remember(post) { derivedStateOf { post.content.imageUrls.isNullOrEmpty() } }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = DefaultUserPadding)){
        BaseText(post.createTime.toTime("dd日MM月"), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(10.dp))
        Surface(color = if (isAllText) SurfaceColor else Color.Transparent) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .clickable { onClick() }
            ) {
                if (!isAllText) {
                    MediaOrImageDisplayArea(post.content.imageUrls!!, onClick = onClick)
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Text(post.content.text, modifier = Modifier
                    .height(100.dp)
                    .weight(1f), color = Color.Black, overflow = TextOverflow.Ellipsis)
            }
        }
    }

}
@Composable
fun MediaOrImageDisplayArea(urls:List<String>,onClick: () -> Unit) {
    val context= LocalContext.current
    val selfImageUi by remember(urls) { derivedStateOf {
        when(urls.size) {
            1-> OneSelfImageUi(context,urls)
            2-> TwoSelfImageUi(context,urls)
            3-> ThreeSelfImageUi(context,urls)
            4-> FourSelfImageUi(context,urls)
            5-> FiveSelfImageUi(context,urls)
            6-> SixSelfImageUi(context,urls)
            7-> SevenSelfImageUi(context,urls)
            8-> EightSelfImageUi(context,urls)
            9-> NineSelfImageUi(context,urls)
            else -> OneSelfImageUi(context,urls)
        }
    } }
    if (urls[0].contains("mp4")) {
        VideoItem(R.drawable.default_cover,modifier = Modifier.size(100.dp)) {

            onClick()
        }
    }else {
        SelfImageUi(selfImageUi)
    }
}


@Composable
fun DisplayImagesScreen(modifier: Modifier = Modifier, imageCount:Int? = 4, svm: SocialViewModel, dvm: DynamicViewModel = hiltViewModel()) {
    val friend by svm.clickFriend.collectAsState()
    val selfUid by remember(friend) { derivedStateOf { friend.userId } }
    val notPinPosts = dvm.notPinedSelfFlow.collectAsLazyPagingItems()
    LaunchedEffect(selfUid) {
        if (selfUid != "") {
            dvm.setSelfCircleUid(selfUid)
        }
    }
    val posts by remember(notPinPosts) { derivedStateOf { notPinPosts.itemSnapshotList.items } }
    val images by remember(posts) {
        derivedStateOf {
            val list = posts.mapNotNull { it.content.imageUrls }.flatten()
            if (imageCount != null) {
                list.take(imageCount)
            } else {
                list.take(6)
            }
        }
    }
    Log.e("DisplayImagesScreen_images",posts.toString())
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        images.forEach { image->
            if (!image.contains("mp4")) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(image).build(),
                    contentDescription = null,
                    modifier= Modifier.size(55.dp),
                    contentScale = ContentScale.Crop
                )
            }else {
                var bitmap: Bitmap? = null
                LaunchedEffect(Unit) {
                    bitmap = AppGlobal.getBitmapFromUrl(image)
                }
                VideoItem(bitmap ?: R.drawable.default_cover, modifier = Modifier.size(55.dp))
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainPostInfoScreen(navController: NavHostController,dvm: DynamicViewModel,svm: SocialViewModel) {
    val clickFriend by svm.clickFriend.collectAsState()
    val uvm:UserViewmodel = hiltViewModel()
    val user by uvm.myUser.collectAsState()
    var isSelected by remember { mutableStateOf(false) }
    val post by dvm.postInfo.collectAsState()
    val postId by remember(post) { derivedStateOf { post.postId } }
    var lookImage by remember { mutableStateOf<Any>(R.drawable.default_avatar) }
    var isFocus by remember { mutableStateOf(false) }
    val onChangeFocus:(Boolean)->Unit = {isFocus = it}
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequest = remember { FocusRequester() }
    val comments by dvm.listCommentByPostId(postId).collectAsState()
    val ids by dvm.listLikeByPostId(postId).collectAsState()
    val scope = rememberCoroutineScope()
    var deleteModel by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf(ApiService.ListCommentRespStruct()) }
    LaunchedEffect(isFocus) {
        Log.e("isFocus_isFocus",isFocus.toString())
        if (isFocus) {
            focusRequest.requestFocus()
            keyboardController?.show()
        }else {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }
    CanLookImage(lookImage,isSelected, onChange = {
        isSelected = false
    }) {
        Box {
            MainPostInfoUi(navController,post,clickFriend,focusRequest, onCreateComment = {content->
                dvm.addCommentReply(postId,user.id,comment.userId,content)
                comment = ApiService.ListCommentRespStruct()
            },
                onImageClick = {onChangeFocus(false)
                    lookImage = it
                    isSelected = true}, onChangeDeleteModel = {deleteModel = it},onChangeFocus=onChangeFocus) {
                LikeAndCommentArea(user.id,postId, comments = comments, ids = ids,svm,dvm, onDelete = {
                    /**
                     * 删除评论
                     */
                    comment = it
                    deleteModel = 2
                }, onCommentReply = {
                    comment = it
                    onChangeFocus(true)
                })
            }
            when(deleteModel) {
                0-> {}
                1-> {
                    DeleteDialog(title = "是否删除此动态?", text = "点击确定删除动态",onDelete = {
                        scope.launch(Dispatchers.IO) {
                            try {
                                dvm.deletePost(userId = user.id,postId)
                                delay(100)
                                withContext(Dispatchers.Main) {
                                    dvm.setCurrentUid(user.id)
                                    Toast.makeText(AppGlobal.getAppContext(),"删除成功",Toast.LENGTH_SHORT).show()
                                    deleteModel = 0
                                    delay(100)
                                    navController.navigateUp()
                                }
                            }catch (e:Exception) {
                                withContext(Dispatchers.Main) {
                                    deleteModel = 0
                                    Toast.makeText(AppGlobal.getAppContext(),e.message,Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }, onCancel = {  deleteModel = 0;comment = ApiService.ListCommentRespStruct()})
                }
                2-> {
                    DeleteDialog("是否删除此评论","点击确定删除评论", onDelete = {
                        dvm.removeCommentReply(
                            comment.commentId,
                            postId,
                            comment.userId,
                            comment.content,
                            comment.targetUserId
                        )
                        deleteModel=0
                        comment = ApiService.ListCommentRespStruct()
                    }, onCancel = {deleteModel=0;comment = ApiService.ListCommentRespStruct()})
                }
            }
        }
    }

}
@Composable
fun DeleteDialog(title:String,text:String,onDelete: () -> Unit,onCancel:()->Unit) {
    AlertDialog(onDismissRequest = {}, confirmButton = {
        BaseText("确定", color = Color.Red, modifier = Modifier.clickable { onDelete() })
    }, dismissButton = {
        BaseText("取消", modifier = Modifier.clickable { onCancel() })
    }, title = {
        BaseText(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }, text = {
        BaseText(text, fontSize = 12.sp)
    })
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainPostInfoUi(
    navController: NavHostController,
    post: Post,
    clickFriend: Friend,
    focusRequest:FocusRequester,
    onChangeDeleteModel:(Int)->Unit,
    onCreateComment:(String)->Unit,
    onImageClick:(Any)->Unit,
    onChangeFocus: (Boolean) -> Unit,
    likeAndComment:@Composable () -> Unit
) {
    Scaffold(topBar = {
        TopBarWithBack(navController, title = "详情", color = SurfaceColor, action = {
            BaseText("")
        })
    }) { padding->
        PostInfoDetail(
            post,
            clickFriend,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    onChangeFocus(false)
                },
            focusRequester = focusRequest,
            onContentClick = { onChangeFocus(true) },
            onDelete = {
                /**
                 * 删除动态
                 */
                onChangeDeleteModel(1)

            },
            onChangeFocus = {onChangeFocus(true)},
            onLikeClick = {}, onCreateComment = {content->
                onCreateComment(content) },
            onImageClick = {onImageClick(it)}) {
            likeAndComment()
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostInfoDetail(post: Post,friend:Friend,modifier: Modifier=Modifier,focusRequester: FocusRequester,onChangeFocus:()->Unit,onLikeClick:()->Unit,onDelete:()->Unit,onContentClick:()->Unit,onCreateComment:(String)->Unit,onImageClick: (Any) -> Unit,likeAndComment:@Composable ()->Unit) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uvm:UserViewmodel = hiltViewModel()
    val user by uvm.myUser.collectAsState()
    var find by remember { mutableStateOf("") }

    Box(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier
            .fillMaxWidth()
            .padding(
                DefaultUserPadding
            )
            .imePadding()) {
            UserAvatar(friend.displayAvatar, lifecycle) { onImageClick(friend.displayAvatar) }
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(post.content.text, color = Color.Black)
                if (post.content.imageUrls != null) MediaContent(post.content,false,true,lifecycle,onImageClick={
                    onImageClick(it)
                }) {
                    //todo:视频播放
                }
                BottomDynamicArea(postOwnerId = post.userId, mid = user.id, createTime = post.createTime, location = post.meta.location,onLikeClick = onLikeClick, onDelete = onDelete, onContentClick = onContentClick)
                likeAndComment()
            }
        }
        Box(modifier=Modifier
            .fillMaxWidth()
            .height(75.dp)
            .background(SurfaceColor)
            .align(Alignment.BottomStart)){
            Wrapper(modifier=Modifier.align(Alignment.Center)) {
                CustomTextField(
                    value = find,
                    onValueChange = { find = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = IconGreen
                    ),
                    modifier = Modifier
                        .height(30.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (it.isFocused) {
                                onChangeFocus()
                            }
                        },
                    maxLines = 1,
                    placeholder = { BaseText("点击评论:", fontSize = 12.sp, color = PlaceholderColor) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { onCreateComment(find);find = "" })
                )
            }
        }
    }
}

@Composable
fun PostInfoItem() {

}