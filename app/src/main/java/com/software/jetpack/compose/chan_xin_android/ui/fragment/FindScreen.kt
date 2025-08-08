package com.software.jetpack.compose.chan_xin_android.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.buildSpannedString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.software.jetpack.compose.chan_xin_android.CameraActivity
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserPadding
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserScreenItemDp
import com.software.jetpack.compose.chan_xin_android.entity.Friend
import com.software.jetpack.compose.chan_xin_android.entity.Post
import com.software.jetpack.compose.chan_xin_android.entity.PostContent
import com.software.jetpack.compose.chan_xin_android.entity.PostMeta
import com.software.jetpack.compose.chan_xin_android.entity.User
import com.software.jetpack.compose.chan_xin_android.ext.switchTab
import com.software.jetpack.compose.chan_xin_android.ext.toTime
import com.software.jetpack.compose.chan_xin_android.http.service.ApiService
import com.software.jetpack.compose.chan_xin_android.ui.activity.MainActivityRouteEnum
import com.software.jetpack.compose.chan_xin_android.ui.activity.Wrapper
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseBox
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseButton
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseScreenItem
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.base.CanLookImage
import com.software.jetpack.compose.chan_xin_android.ui.base.CustomTextField
import com.software.jetpack.compose.chan_xin_android.ui.base.ExpandRowMenu
import com.software.jetpack.compose.chan_xin_android.ui.base.LazyColumnWithCover
import com.software.jetpack.compose.chan_xin_android.ui.base.LoadingDialog
import com.software.jetpack.compose.chan_xin_android.ui.base.PlayVideo
import com.software.jetpack.compose.chan_xin_android.ui.base.extraVideoFrame
import com.software.jetpack.compose.chan_xin_android.ui.base.rememberVideoFrame
import com.software.jetpack.compose.chan_xin_android.ui.theme.DividerColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.LittleTextColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.PlaceholderColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.SurfaceColor
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.util.CameraUtil
import com.software.jetpack.compose.chan_xin_android.util.Location
import com.software.jetpack.compose.chan_xin_android.util.Oss
import com.software.jetpack.compose.chan_xin_android.util.PreferencesFileName
import com.software.jetpack.compose.chan_xin_android.util.StringUtil
import com.software.jetpack.compose.chan_xin_android.vm.DynamicViewModel
import com.software.jetpack.compose.chan_xin_android.vm.SocialViewModel
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder

private val DEFAULT_USER_PADDING = 8.dp
private val AVATAR_SIZE = 50.dp
private val IMAGE_GRID_SPACING = 5.dp
private val IMAGE_GRID_ITEM_SIZE = 75.dp
private val VIDEO_THUMBNAIL_SIZE = 100.dp to 150.dp
private val TARGET_OFFSET_DP = 100.dp
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FindMainScreen(navController:NavHostController) {
    val activity = LocalContext.current as Activity
    // 拦截返回键，直接退出应用
    BackHandler(enabled = true) {
        activity.moveTaskToBack(true) // 切换到后台
    }
    BaseBox(modifier = Modifier
        .fillMaxSize()
        .background(color = SurfaceColor)) {
        Scaffold(topBar = {
            MyTopBar(title = "发现")
        }) {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(SurfaceColor)) {
                LabelScreenItem(painterResource(R.drawable.friend_circle),"朋友圈"){ navController.switchTab(MainActivityRouteEnum.FRIEND_CIRCLE_SCREEN.route) }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = DividerColor,
                    thickness = 10.dp
                )
                //todo:以下没有后端api实现
                LabelScreenItem(painterResource(R.drawable.friend_circle),"视频号"){  }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = DividerColor,
                    thickness = 0.3.dp
                )
                LabelScreenItem(painterResource(R.drawable.friend_circle),"直播"){  }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = DividerColor,
                    thickness = 10.dp
                )
                LabelScreenItem(painterResource(R.drawable.friend_circle),"扫一扫"){  }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = DividerColor,
                    thickness = 0.3.dp
                )
                LabelScreenItem(painterResource(R.drawable.friend_circle),"听一听"){  }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = DividerColor,
                    thickness = 10.dp
                )
                LabelScreenItem(painterResource(R.drawable.friend_circle),"看一看"){  }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = DividerColor,
                    thickness = 0.3.dp
                )
                LabelScreenItem(painterResource(R.drawable.friend_circle),"搜一搜"){  }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = DividerColor,
                    thickness = 10.dp
                )
                LabelScreenItem(painterResource(R.drawable.friend_circle),"附近"){  }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = DividerColor,
                    thickness = 10.dp
                )
                LabelScreenItem(painterResource(R.drawable.friend_circle),"游戏"){  }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = DividerColor,
                    thickness = 10.dp
                )
                LabelScreenItem(painterResource(R.drawable.friend_circle),"小程序"){  }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FriendCircleScreen(navController:NavHostController,dvm:DynamicViewModel,svm: SocialViewModel) {
    val uvm  = hiltViewModel<UserViewmodel>()
    val user by uvm.myUser.collectAsState()
    var filePath by remember { mutableStateOf("") }
    LaunchedEffect(user.id) {
        if (user.id != "") {
            filePath = AppGlobal.getFilePath(user.id)
        }
    }
    var deleteModel by remember { mutableIntStateOf(0) }
    val posts = dvm.pagingDataFlow.collectAsLazyPagingItems()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    var deletePostId by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val clickComment by dvm.currentClickComment.collectAsState()
    val clickPostId by dvm.currentPostId.collectAsState()
    var find by remember { mutableStateOf("") }
    Log.e("click_comment",clickComment.toString())
    Box {
        FriendCircleScreenUI(navController,sheetState,filePath,find,dvm,posts=posts,svm = svm,onFilePathChange = {
            filePath = it
        }, onValueChange = {find = it},onDelete = {
            deletePostId = it
            deleteModel = 1
        }, onDeleteComment = {deleteModel = 2}, onCommentReply = {
            //todo:回复评论
            dvm.addCommentReply(clickPostId,user.id,clickComment.userId,find)
        }) {
            //todo:進入被點擊的好友的詳情頁面
        }
        when (deleteModel) {
            1->{
                AlertDialog(onDismissRequest = {}, title = {
                    BaseText("是否刪除此動態", color = LittleTextColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }, text = {
                    BaseText("點擊刪除之後你的這條動態會被刪除")
                },confirmButton = {
                    TextButton(onClick = {
                        deleteModel = 0
                        scope.launch(Dispatchers.IO) {
                            dvm.deletePost(userId = user.id,deletePostId)
                            withContext(Dispatchers.Main) {
                                dvm.setCurrentUid(user.id)
                            }
                        }

                    }) {
                        BaseText("刪除", color = Color.Red)
                    }
                }, dismissButton = {
                    TextButton(onClick = {
                        deleteModel = 0
                    }) {
                        BaseText("取消")
                    }
                })
            }
            2->{
                AlertDialog(onDismissRequest = {}, title = {
                    BaseText("是否删除此评论?", color = Color.Red, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }, text = {
                    BaseText("点击删除后，此评论将被删除")
                },confirmButton = {
                    TextButton(onClick = {
                        deleteModel = 0
                        dvm.removeCommentReply(
                            clickComment.commentId,
                            clickPostId,
                            clickComment.userId,
                            clickComment.content,
                            clickComment.targetUserId
                        )
                    }) {
                        BaseText("刪除", color = Color.Red)
                    }
                }, dismissButton = {
                    TextButton(onClick = {
                        deleteModel = 0
                    }) {
                        BaseText("取消")
                    }
                })
            }
            else->{}
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostItem(
    post: Post,
    isScrolling: Boolean = true,
    mid:String,
    modifier: Modifier = Modifier,
    svm: SocialViewModel,
    dvm: DynamicViewModel,
    onDeleteComment:()->Unit,
    onCommentReply:()->Unit,
    onDelete: (String) -> Unit,
    onContentClick: (String) -> Unit,
    onClick: (Uri) -> Unit

) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val targetOffsetPx = with(density) { TARGET_OFFSET_DP.toPx() } // 只计算一次密度转换

    // 屏幕中心坐标
    val (screenCenterX, screenCenterY) = remember(configuration) {
        val widthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
        val heightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
        Pair(widthPx / 2, heightPx / 2)
    }
    val postId by remember(post) { derivedStateOf { post.postId } }
    val ids by dvm.listLikeByPostId(postId).collectAsState()
    val comments by dvm.listCommentByPostId(postId).collectAsState()
    val postOwnerId by remember(post) { derivedStateOf { post.userId } }
    val content by remember(post) { derivedStateOf { post.content } }
    val createTime by remember(post) { derivedStateOf { post.createTime } }
    val friend by svm.getFriendInfo(mid,postOwnerId).collectAsState(Friend())
    Log.e("friend_ss",friend.toString())
    var isAtTargetPosition by remember { mutableStateOf(false) }
    var isLiked by remember { mutableStateOf(false) }
    Log.e("comments",comments.toString())
    LaunchedEffect(postId, mid) {
        isLiked = dvm.userLikedPost(mid,postId)
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(DEFAULT_USER_PADDING)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DEFAULT_USER_PADDING)
                .onGloballyPositioned { layoutCoordinates ->
                    val componentCenterX = layoutCoordinates.positionInWindow().x +
                            (layoutCoordinates.size.width / 2)
                    val componentCenterY = layoutCoordinates.positionInWindow().y +
                            (layoutCoordinates.size.height / 2)

                    isAtTargetPosition =
                        componentCenterX in (screenCenterX - targetOffsetPx)..(screenCenterX + targetOffsetPx) &&
                                componentCenterY in (screenCenterY - targetOffsetPx)..(screenCenterY + targetOffsetPx)
                }
        ) {
            // 头像区域
            UserAvatar(displayAvatar = friend.displayAvatar, lifecycle = lifecycle)

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                // 用户名
                BaseText(text = friend.displayName, color = LittleTextColor)

                // 帖子内容
                BaseText(text = content.text)

                // 媒体内容（图片/视频）
                MediaContent(
                    postContent = content,
                    isScrolling = isScrolling,
                    isAtTargetPosition = isAtTargetPosition,
                    lifecycle = lifecycle,
                    onClick = onClick
                )

                // 时间和交互区
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BaseText(text = createTime.toTime(), color = PlaceholderColor)
                    Spacer(modifier = Modifier.weight(1f))
                    if (postOwnerId == mid) {
                        LittleButton(modifier=Modifier.width(20.dp),onclick = {
                            onDelete(postId)
                        }) {
                            Icon(Icons.Filled.Delete,contentDescription = null, tint = LittleTextColor, modifier = Modifier.size(13.dp))
                        }
                        Spacer(modifier.width(10.dp))
                    }
                    ExpandableLikeAndContent(
                        onLikeClick = {
                            isLiked = !isLiked
                            if (isLiked) { dvm.addLikeId(postId,mid) }else { dvm.removeLikeId(postId,mid) }
                        },
                        onContentClick = {
                            onContentClick(postId)
                        }
                    )

                }
                Wrapper {
                    LikeAndCommentArea(mid,postId,comments,ids,svm = svm, onCommentReply = onCommentReply, onDelete = onDeleteComment, dvm = dvm)
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.3.dp,
            color = DividerColor
        )
    }

}
@Composable
fun LikeAndCommentArea(mId:String, postId: String,comments: List<ApiService. ListCommentRespStruct>,ids:List<String>, svm:SocialViewModel,dvm:DynamicViewModel,uvm: UserViewmodel= hiltViewModel(),onDelete:()->Unit,onCommentReply:()->Unit) {

    val user by uvm.myUser.collectAsState()
    val friendList by svm.friendCacheList.collectAsState()
    val friendMap by remember(friendList) {
        derivedStateOf { friendList.associateBy { it.userId } }
    }
    val displayNames by remember(ids,friendMap) {
        derivedStateOf { ids.map {
            if (it == mId) user.nickname
            else friendMap[it]?.displayName ?: "11"

        } }
    }
    Log.e("LikeAndCommentArea_comments",comments.toString())
    val like by remember(displayNames) { derivedStateOf { displayNames.joinToString(separator = ", ") } }
    Surface(color = SurfaceColor, shape = RoundedCornerShape(5.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            key(like) { BaseText("♡ $like", color = LittleTextColor, fontWeight = FontWeight.Bold) }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = DividerColor, thickness = 0.5.dp)
            Column {
                comments.forEach { comment->
                    CommentItem(comment,user,svm) {
                        dvm.loadClickComment(comment)
                        dvm.loadCurrentPost(postId)
                        Log.e("长按评论","回复或者删除评论${comment.commentId}")
                        if (comment.userId==mId) {
                            //删除评论
                            onDelete()
                        }else {
                            //回复别人
                            onCommentReply()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CommentItem(comment:ApiService.ListCommentRespStruct,user:User,svm: SocialViewModel,onclick:() -> Unit) {
    val friendList by svm.friendCacheList.collectAsState()
    val friendMap by remember(friendList) {
        derivedStateOf { friendList.associateBy { it.userId } }
    }
    val fromDisplayName by remember(friendMap,comment) {
        derivedStateOf {
            if (comment.userId==user.id) user.nickname
            else friendMap[comment.userId]?.displayName ?: ""
        }
    }
    val toDisplayName by remember(friendMap,comment) {
        derivedStateOf {
            if (comment.targetUserId==user.id) user.nickname
            else friendMap[comment.targetUserId]?.displayName ?: ""
        }
    }
    Column(modifier = Modifier.clickable { onclick() }) {
        Wrapper {
            Text(buildAnnotatedString {
                withStyle(SpanStyle(color = LittleTextColor, fontWeight = FontWeight.Bold)) {
                    append(fromDisplayName)
                }
                if (comment.targetUserId != "") {
                    withStyle(SpanStyle(color = LittleTextColor)) {
                        append("对")
                    }
                    withStyle(SpanStyle(color = LittleTextColor, fontWeight = FontWeight.Bold)) {
                        append(toDisplayName)
                    }
                    withStyle(SpanStyle(color = LittleTextColor)) {
                        append("说")
                    }
                }
                append(": ")
                append(comment.content)
            })
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.2.dp)

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FriendCircleScreenUI(
    navController: NavHostController,
    sheetState: ModalBottomSheetState,
    filePath: String,
    find: String,
    dvm: DynamicViewModel,
    svm: SocialViewModel,
    uvm: UserViewmodel = hiltViewModel(),
    posts: LazyPagingItems<Post>,
    modifier: Modifier = Modifier,
    onFilePathChange: (String) -> Unit,
    onCommentReply:()->Unit,
    onDeleteComment:()->Unit,
    onValueChange:(String)->Unit,
    onDelete: (String) -> Unit,
    enterDetail: (String) -> Unit
) {
    // 状态管理
    val state = rememberFriendCircleState(navController,dvm)
    val user by uvm.myUser.collectAsState()
    val clickFriend by svm.clickFriend.collectAsState()
    val scrollState = rememberLazyListState()
    val isScrolling by remember { derivedStateOf { scrollState.isScrollInProgress } }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var isRefresh by remember { mutableStateOf(false) }
    var isFocus by remember { mutableStateOf(false) }
    var postId by remember { mutableStateOf("") }
    var commentModel by remember { mutableIntStateOf(0) }
    // 处理图片选择超出限制的提示
    LaunchedEffect(state.urisSize) {
        if (state.urisSize > 9) {
            withContext(Dispatchers.Main) {
                Toast.makeText(AppGlobal.getAppContext(),"图片选择超出限制，最多选择9张",Toast.LENGTH_SHORT).show()
            }
            state.urisSize = 0 // 重置计数
        }
    }

    // 处理加载状态
    LaunchedEffect(posts.itemCount) {
        state.isLoading = posts.itemCount == 0
    }

    when {
        state.selectedVideoUri != null -> {
            Log.e("VideoPlayerScreen","VideoPlayerScreen")
            VideoPlayerScreen(
                videoUri = state.selectedVideoUri!!,
                onBack = { state.selectedVideoUri = null }
            )
        }

        state.selectedUri != null -> {
            CoverSelectionScreen(
                uri = state.selectedUri!!,
                onCancel = { state.selectedUri = null },
                onConfirm = { uri ->
                    handleCoverConfirmation(
                        scope = state.scope,
                        uri = uri,
                        userId = user.id,
                        dvm = dvm,
                        onFilePathChange = onFilePathChange
                    )
                    state.selectedUri = null
                }
            )
        }
        // 主界面状态
        else -> {
            ModalBottomSheetLayout(
                sheetState = sheetState,
                sheetContent = {
                    DynamicCreateSheet(
                        scope = state.scope,
                        launcher = state.activityLauncher,
                        bottomSheetState = sheetState,
                        onClickPhoto = { state.mutableLauncher.launch("image/*") },
                        onClickVideo = { state.videoLauncher.launch("video/*") }
                    )
                }
            ) {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .imePadding()
                        .pointerInput(Unit) {
                            detectTapGestures(onPress = {
                                if (isFocus) {
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                    isFocus = false
                                    commentModel = 0
                                }
                            })
                        }
                ) {
                    // 朋友圈列表
                    FriendCircleList(
                        filePath = filePath,
                        scope = state.scope,
                        user = user,
                        posts = posts,
                        scrollState = scrollState,
                        isScrolling = isScrolling,
                        svm = svm,
                        dvm = dvm,
                        onRefresh = {
                            state.scope.launch(Dispatchers.IO) {
                                isRefresh = true
                                dvm.setCurrentUid(user.id)
                                delay(1000)
                                isRefresh = false
                            }
                        },
                        onChangeCover = { state.launcher.launch("image/*") },
                        onEnterFriendInfoDetail = { enterDetail(clickFriend.userId) },
                        onDelete = onDelete,
                        focusRequester = state.focusRequester,
                        keyboardController = keyboardController,
                        isFocus = isFocus,
                        onFocusChange = { isFocus = it },
                        onVideoSelected = { state.selectedVideoUri = it },
                        onContentClick = { postId = it },
                        onDeleteComment = onDeleteComment,
                        onCommentModelChange = {commentModel = it}
                    )

                    // 顶部导航栏
                    TopBar(
                        isRefresh = isRefresh,
                        onNavigateUp = { navController.navigateUp() },
                        scope = state.scope,
                        sheetState = sheetState
                    )

                    // 底部评论栏
                    CommentInputBar(
                        find = find,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .height(if (isFocus) 55.dp else 0.dp)
                            .alpha(if (isFocus) 1f else 0f),
                        onFindChange = { onValueChange(it) },
                        focusRequester = state.focusRequester,
                        onCommentClick = {

                            if (postId != "" && find != "") {
                                if (commentModel == 2) {
                                    onCommentReply()
                                }else if (commentModel == 1){
                                    dvm.addCommentReply(postId, userId = user.id,"",find)
                                }

                                onValueChange("")
                                isFocus = false
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        }
                    )
                    LoadingDialog(isShowing  = state.isLoading)
                }
            }
        }
    }
}

// 状态封装
@Composable
private fun rememberFriendCircleState(navController: NavHostController,dvm: DynamicViewModel): FriendCircleState {
    Log.e("state_select_uri__","state_select_uri__")
    val scope = rememberCoroutineScope()
    val selectedUri = remember { mutableStateOf<Uri?>(null) }
    val selectedVideoUri = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedUri.value = uri
        Log.e("state_select_uri_",selectedUri.toString())
    }
    val urisSize = remember { mutableIntStateOf(0) }
    val activityLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        handleActivityResult(result, dvm, navController)
    }
    val mutableLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        urisSize.intValue = uris.size
        if (urisSize.intValue <= 9) {
            dvm.loadPhotoUris(uris)
            dvm.loadVideoUri(null)
            navController.switchTab(MainActivityRouteEnum.CREATE_POST_SCREEN.route)
        }
    }
    val videoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        dvm.loadVideoUri(uri)
        dvm.loadPhotoUris(emptyList())
        if (uri!=null) navController.switchTab(MainActivityRouteEnum.CREATE_POST_SCREEN.route)
    }

    return remember {
        FriendCircleState(
            scope = scope,
            launcher = launcher,
            activityLauncher = activityLauncher,
            mutableLauncher = mutableLauncher,
            selectedUriState = selectedUri,
            videoLauncher = videoLauncher,
            urisSizeState = urisSize,
            selectedVideoUriState = selectedVideoUri,
            focusRequester = FocusRequester()
        )
    }
}

// 封面选择屏幕
@Composable
private fun CoverSelectionScreen(
    uri: Uri,
    onCancel: () -> Unit,
    onConfirm: (Uri) -> Unit
) {
    BackHandler { onCancel() }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(AppGlobal.getAppContext())
                .data(uri)
                .build(),
            contentDescription = "选择封面预览",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Dimens.DefaultUserScreenItem,
                    end = Dimens.DefaultUserScreenItem,
                    bottom = Dimens.DefaultUserScreenItem * 1.5f
                )
                .align(Alignment.BottomCenter)
        ) {
            TextButton(onClick = onCancel) {
                Text("取消", color = Color.White)
            }

            TextButton(onClick = { onConfirm(uri) }) {
                Text("完成", color = Color.White)
            }
        }
    }
}
object Dimens {
    val DefaultUserScreenItem = 16.dp
}
// 视频播放屏幕
@Composable
private fun VideoPlayerScreen(
    videoUri: Uri,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        contentAlignment = Alignment.Center
    ) {
        PlayVideo(
            videoUri = videoUri,
            defaultAspectRatio = 1f
        )
    }
}

// 朋友圈列表
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun FriendCircleList(
    filePath: String,
    scope: CoroutineScope,
    user: User, // 假设存在User数据类
    posts: LazyPagingItems<Post>,
    scrollState: LazyListState,
    isScrolling: Boolean,
    svm: SocialViewModel,
    dvm: DynamicViewModel,
    onRefresh: () -> Unit,
    onChangeCover: () -> Unit,
    onEnterFriendInfoDetail: () -> Unit,
    onDelete: (String) -> Unit,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
    isFocus: Boolean,
    onDeleteComment: () -> Unit,
    onContentClick: (String) -> Unit,
    onCommentModelChange:(Int)->Unit,
    onFocusChange: (Boolean) -> Unit,
    onVideoSelected: (Uri) -> Unit
) {
    LazyColumnWithCover(
        filePath.ifEmpty { R.drawable.default_cover },
        user.nickname,
        user.displayAvatar,
        modifier = Modifier.fillMaxSize(),
        listState = scrollState,
        onRefresh = onRefresh,
        onChangeCover = onChangeCover,
        onEnterFriendInfoDetail = onEnterFriendInfoDetail
    ) {
        items(
            count = posts.itemCount,
            key = { posts[it]?.postId ?: it.toString() }
        ) { index ->
            val post = posts[index]
            if (post != null) {
                PostItem(
                    post = post,
                    isScrolling = isScrolling,
                    mid = user.id,
                    svm = svm,
                    dvm = dvm,
                    onDelete = { onDelete(post.postId) },
                    onContentClick = {
                        onContentClick(post.postId)
                        if (!isFocus) {
                            scope.launch { scrollState.animateScrollToItem(index) }
                            focusRequester.requestFocus()
                            keyboardController?.show()
                            onFocusChange(true)
                            onCommentModelChange(1)
                        }
                    },
                    onDeleteComment = onDeleteComment,
                    onCommentReply = {
                        onContentClick(post.postId)
                        if (!isFocus) {
                            scope.launch { scrollState.animateScrollToItem(index) }
                            focusRequester.requestFocus()
                            keyboardController?.show()
                            onFocusChange(true)
                            onCommentModelChange(2)
                        }
                    },
                    onClick = onVideoSelected
                )
            }
        }

        // 加载更多状态
        when (posts.loadState.append) {
            is LoadState.Loading -> item { LoadingMoreItem() }
            is LoadState.NotLoading -> item { NoMoreItem() }
            else -> {}
        }
    }
}

// 顶部导航栏
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TopBar(
    isRefresh: Boolean,
    onNavigateUp: () -> Unit,
    scope: CoroutineScope,
    sheetState: ModalBottomSheetState
) {
    MyTopBar(
        title = "",
        preContent = {
            if (isRefresh) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = IconGreen
                )
            } else {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "返回",
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onNavigateUp() },
                    tint = Color.White
                )
            }
        },
        action = {
            Icon(
                painterResource(R.drawable.photo),
                contentDescription = "发布内容",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        scope.launch { sheetState.show() }
                    }
            )
        },
        defaultColor = Color.Transparent
    )
}

// 评论输入栏
@Composable
private fun CommentInputBar(
    find: String,
    modifier:Modifier = Modifier,
    onFindChange: (String) -> Unit,
    focusRequester: FocusRequester,
    onCommentClick: () -> Unit
) {
    Surface(
        color = SurfaceColor,
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = DefaultUserPadding)) {
            CustomTextField(
                value = find,
                onValueChange = onFindChange,
                modifier = Modifier
                    .height(40.dp)
                    .weight(1f)
                    .focusRequester(focusRequester),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = IconGreen,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier=Modifier.width(5.dp))
            BaseButton(onClick = onCommentClick) {
                BaseText("评论", color = Color.White)
            }
        }
    }
}

// 工具函数：处理封面确认逻辑
private fun handleCoverConfirmation(
    scope: CoroutineScope,
    uri: Uri,
    userId: String,
    dvm: DynamicViewModel,
    onFilePathChange: (String) -> Unit
) {
    scope.launch(Dispatchers.IO) {
        try {
            val fileExtension = StringUtil.getFileExtensionFromUri(AppGlobal.getAppContext(), uri)
            val coverUrl = Oss.uploadFile("${System.currentTimeMillis()}.$fileExtension", uri)
            dvm.setCover(userId, coverUrl)
            val path = AppGlobal.saveBitmapToFile(coverUrl, System.currentTimeMillis().toString())
            AppGlobal.saveUserRela(PreferencesFileName.USER_COVER_FILE_PATH(userId), path)
            onFilePathChange(path)
        } catch (e: Exception) {
            // 处理异常
            Log.e("CoverUpload", "Error uploading cover", e)
        }
    }
}

// 工具函数：处理Activity结果
private fun handleActivityResult(result: ActivityResult,dvm: DynamicViewModel,navController: NavHostController) {
    when (result.resultCode) {
        CameraUtil.IMAGE_URI_CODE -> {
            val uri = getParcelableExtra<Uri>(result.data, CameraUtil.IMAGE_URI)
            uri?.let {
                dvm.loadPhotoUris(listOf(it)) // 需要在外部获取dvm引用或通过参数传递
                navController.switchTab(MainActivityRouteEnum.CREATE_POST_SCREEN.route) // 需要在外部获取navController引用或通过参数传递
            }
        }
        CameraUtil.VIDEO_URI_CODE -> {
            val uri = getParcelableExtra<Uri>(result.data, CameraUtil.VIDEO_URI)
            uri?.let {
                dvm.loadVideoUri(it) // 需要在外部获取dvm引用或通过参数传递
                navController.switchTab(MainActivityRouteEnum.CREATE_POST_SCREEN.route) // 需要在外部获取navController引用或通过参数传递
            }
        }
    }
}

// 工具函数：获取ParcelableExtra（兼容不同API版本）
private inline fun <reified T : Parcelable> getParcelableExtra(data: Intent?, key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        data?.getParcelableExtra(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        data?.getParcelableExtra(key)
    }
}

@Composable
fun LoadingMoreItem() {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        BaseText("加载更多", color = PlaceholderColor)
        CircularProgressIndicator(color = IconGreen)
    }
}
@Composable
fun NoMoreItem() {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        BaseText("没有更多了", color = PlaceholderColor)
    }
}




// 提取用户头像子组件
@Composable
private fun UserAvatar(
    displayAvatar: Any,
    lifecycle: Lifecycle,
    modifier: Modifier = Modifier
) {
    Wrapper {
        AsyncImage(
            model = ImageRequest.Builder(AppGlobal.getAppContext())
                .data(displayAvatar)
                .allowHardware(true)
                .lifecycle(lifecycle)
                .build(),
            contentDescription = "用户头像", // 完善无障碍描述
            modifier = modifier
                .size(AVATAR_SIZE)
                .clip(RoundedCornerShape(5.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

// 提取媒体内容（图片/视频）子组件
@Composable
private fun MediaContent(
    postContent: PostContent, // 传递Content对象，减少参数数量
    isScrolling: Boolean,
    isAtTargetPosition: Boolean,
    lifecycle: Lifecycle,
    onClick: (Uri) -> Unit
) {
    Wrapper {
        val imageUrls = postContent.imageUrls ?: return@Wrapper // 空安全处理：无图片直接返回

        if (imageUrls.isNotEmpty() && imageUrls[0].contains("mp4")) {
            // 视频处理
            val videoUrl = imageUrls[0]
            val videoUri = remember(videoUrl) { Uri.parse(videoUrl) }
            val encodedUrl = remember(videoUrl) {
                URLEncoder.encode(videoUrl, "UTF-8")
            }
            val encodedUri = remember(encodedUrl) { Uri.parse(encodedUrl) }
            val bitmap by rememberVideoFrame(encodedUri)

            Box(
                modifier = Modifier
                    .width(VIDEO_THUMBNAIL_SIZE.first)
                    .height(VIDEO_THUMBNAIL_SIZE.second)
            ) {
                when {
                    !isScrolling && isAtTargetPosition -> {
                        PlayVideo(
                            videoUri = videoUri,
                            defaultWidth = VIDEO_THUMBNAIL_SIZE.first
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { onClick(videoUri) }
                        )
                    }
                    !isScrolling -> {
                        VideoItem(
                            bitmap ?: R.drawable.default_cover,
                        ) { onClick(videoUri)}
                    }
                    else -> {
                        LoadingPlaceholder(
                            width = VIDEO_THUMBNAIL_SIZE.first,
                            height = VIDEO_THUMBNAIL_SIZE.second
                        )
                    }
                }
            }
        } else {
            // 图片网格处理
            val gridHeight = remember(imageUrls.size) {
                ((imageUrls.size + 2) / 3 * 80).dp
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(IMAGE_GRID_SPACING),
                horizontalArrangement = Arrangement.spacedBy(IMAGE_GRID_SPACING),
                modifier = Modifier.height(gridHeight)
            ) {
                items(
                    items = imageUrls,
                    key = { it } // 使用图片URL作为key（比索引更稳定）
                ) { imageUrl ->
                    if (!isScrolling) {
                        AsyncImage(
                            model = remember(imageUrl, lifecycle) { // 依赖lifecycle变化
                                ImageRequest.Builder(AppGlobal.getAppContext())
                                    .data(imageUrl)
                                    .allowHardware(true)
                                    .lifecycle(lifecycle)
                                    .build()
                            },
                            contentDescription = "帖子图片",
                            modifier = Modifier.size(IMAGE_GRID_ITEM_SIZE),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        LoadingPlaceholder(
                            width = IMAGE_GRID_ITEM_SIZE,
                            height = IMAGE_GRID_ITEM_SIZE
                        )
                    }
                }
            }
        }
    }
}

// 提取加载占位符子组件（复用）
@Composable
private fun LoadingPlaceholder(
    width: Dp,
    height: Dp,
    backgroundColor: Color = SurfaceColor,
    textColor: Color = PlaceholderColor
) {
    Box(
        modifier = Modifier
            .size(width = width, height = height)
            .background(color = backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
            BaseText(
                text = "加载中",
                fontSize = 12.sp,
                color = textColor
            )
        }
    }
}
@Composable
fun ExpandableLikeAndContent(onLikeClick:()->Unit,onContentClick:()->Unit) {
    var isExpand by remember { mutableStateOf(false) }
    Row {
        ExpandRowMenu(expanded = isExpand, onDismissRequest = {isExpand = false}, offset = DpOffset(-35.dp,-25.dp),modifier = Modifier
            .height(35.dp)
            .width(150.dp)
            .background(color = Color.Black.copy(0.4f))) {
            DropdownMenuItem(onClick = {
                isExpand = false
                onLikeClick()
            },modifier = Modifier
                .height(35.dp)
                .width(75.dp)) {
                BaseText("点赞", color = Color.White, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(3.dp))
                Icon(Icons.Filled.ThumbUp,contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            DropdownMenuItem(onClick = {
                isExpand = false
                onContentClick()
            }, modifier = Modifier
                .height(35.dp)
                .width(75.dp)) {
                BaseText("评论", color = Color.White, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(3.dp))
                Icon(Icons.Filled.MailOutline,contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
        LittleButton(onclick = {
            isExpand = !isExpand
        }) {
            Icon(painterResource(R.drawable.more),contentDescription = null, tint = LittleTextColor, modifier = Modifier.size(13.dp))
        }
    }
}
@Composable
fun LittleButton(modifier: Modifier = Modifier,onclick: () -> Unit,backgroundColor: Color = SurfaceColor,content: @Composable () -> Unit) {
    Box(modifier = modifier
        .defaultMinSize(minHeight = 15.dp, minWidth = 35.dp)
        .background(backgroundColor)
        .clickable { onclick() }, contentAlignment = Alignment.Center) {
        content()
    }
}

enum class CreatePostEnum(val route:String) {
    MAIN_CREATE("main_create"),
    SELECT_LOCATION("select_location"),
    SELECT_SCOPE("select_scope")
}


@Composable
fun CreatePostScreen(navController: NavHostController,dvm: DynamicViewModel,svm:SocialViewModel) {
    val uvm = hiltViewModel<UserViewmodel>()
    val user by uvm.myUser.collectAsState()
    val thisController = rememberNavController()
    var lookModel by remember { mutableIntStateOf(0) }
    var location by remember { mutableStateOf("所在位置") }
    var scopeList by remember { mutableStateOf<List<String>>(emptyList()) }
    LaunchedEffect(user.id) {
        if (user.id != "") {
            scopeList = svm.getFriendList(user.id).map { it.userId }
        }
    }
    Log.e("scopeList_f",scopeList.toString())
    NavHost(navController=thisController, startDestination = CreatePostEnum.MAIN_CREATE.route) {
        composable(CreatePostEnum.MAIN_CREATE.route){
            MainCreatePostScreen(navController,thisController,lookModel,location,scopeList,dvm, svm)
        }
        composable(CreatePostEnum.SELECT_LOCATION.route) {
            SelectLocationToCreatePostScreen(thisController,location) {
                location = it
            }
        }
        composable(CreatePostEnum.SELECT_SCOPE.route) {
            SelectScopeToCreatePostScreen(navController,thisController,svm, onChangeScopeList = {scopeList = it}) { lookModel = it }
        }
    }
}

@Composable
fun SelectLocationToCreatePostScreen(thisController: NavHostController,location:String,onChange:(String)->Unit) {
    var selectLocation by remember { mutableStateOf(location) }
    CreatePostScaffold("所在位置", onCancel = {thisController.navigateUp()}, onCreate = {onChange(selectLocation);thisController.navigateUp()}) {
        Column {
            Wrapper { SexSingleSelect("不显示位置",selectLocation == "所在位置") {selectLocation = "所在位置"} }
            Wrapper { SexSingleSelect(Location.province,selectLocation == Location.province) {selectLocation = Location.province } }
            Wrapper { SexSingleSelect(Location.city,selectLocation == Location.city) {selectLocation = Location.city } }
            Wrapper { SexSingleSelect(Location.address,selectLocation == Location.address) {selectLocation = Location.address } }
        }
    }
}


@Composable
fun SelectScopeToCreatePostScreen(navController: NavHostController,thisController: NavHostController,svm:SocialViewModel,onChangeScopeList:(List<String>)->Unit,onChange: (Int) -> Unit) {
    var selectModel by rememberSaveable { mutableIntStateOf(0) }
    val userViewModel = hiltViewModel<UserViewmodel>()
    val user by userViewModel.myUser.collectAsState()
    val friendList by svm.currentFriendList.collectAsState()
    val selectFriendList by svm.currentSelectFriendList.collectAsState()
    val abandonList by svm.currentAbandonFriendList.collectAsState()
    val sb1 by remember { mutableStateOf(StringBuilder()) }
    var allList by remember { mutableStateOf(friendList) }
    LaunchedEffect(user.id) {
        if (user.id != "") {
            allList = svm.getFriendList(user.id)
        }
    }
    LaunchedEffect(selectFriendList) {
        sb1.append(selectFriendList.map { it.displayName })
    }
    val sb2 by remember { mutableStateOf(StringBuilder()) }
    LaunchedEffect(abandonList) {
        sb2.append(abandonList.map { it.displayName })
    }
    CreatePostScaffold("谁可以看", onCancel = {thisController.navigateUp()}, onCreate = {
        if (selectModel==2 && selectFriendList.isEmpty()) {
            Toast.makeText(AppGlobal.getAppContext(),"至少选择一个好友",Toast.LENGTH_SHORT).show()
            return@CreatePostScaffold
        }
        onChange(selectModel)
        onChangeScopeList(when(selectModel) {
            0-> {
                allList.map { it.userId }
            }
            1-> {
                listOf(user.id)
            }
            2->{
                selectFriendList.map { it.userId }
            }
            3-> {
                (friendList - abandonList).map { it.userId }
            }
            else -> {emptyList()}
        })
        thisController.navigateUp()}) {
        Column {
            Wrapper { SexSingleSelect("公开",selectModel == 0) { selectModel = 0 } }
            Wrapper { SexSingleSelect("私密",selectModel == 1) {selectModel = 1 } }
            Wrapper { SexSingleSelect("部分可见 $sb1",selectModel == 2) {selectModel = 2
                svm.loadCurrentSelectFriendList(emptyList())
                navController.switchTab(MainActivityRouteEnum.SELECT_FRIEND_SCREEN.route) } }
            Wrapper {  SexSingleSelect("谁不可看 $sb2",selectModel == 3) {
                selectModel = 3
                navController.switchTab(MainActivityRouteEnum.ABANDON_FRIEND_SCREEN.route)
            } }
        }
    }

}
@Composable
fun PlayVideoScreen(uri: Uri,onCancel: () -> Unit) {
    Box {
       PlayVideo(uri)
        Icon(Icons.Filled.Close,contentDescription = null, tint = Color.White, modifier = Modifier.clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { onCancel() })
    }
}

@Composable
fun MainCreatePostScreen(navController: NavHostController,thisController:NavHostController,lookModel: Int,location:String,scopeList:List<String>,dvm: DynamicViewModel,svm:SocialViewModel,uvm:UserViewmodel= hiltViewModel()) {
    val user by uvm.myUser.collectAsState()
    val videoUri by dvm.videoUri.collectAsState()
    val photoUris by dvm.photoUris.collectAsState()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {uris: List<Uri> ->
        val newUris = photoUris.let {
            (it+uris).take(9)
        }
        Log.e("urissad",newUris.size.toString())
        dvm.loadPhotoUris(newUris)
    }
    var selectedUri by remember { mutableStateOf(Uri.Builder().build()) }
    var lookImage by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var textCount by remember { mutableIntStateOf(0) }
    val maxCount = 200
    val isLimited by remember(textCount) { derivedStateOf { textCount > maxCount } }
    val size = 85.dp
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var videoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(Unit) {
        videoBitmap = extraVideoFrame(AppGlobal.getAppContext(),videoUri?:Uri.Builder().build(),0L)
    }
    var isPlay by remember { mutableStateOf(false) }
    Box {
        CreatePostScaffold("发表动态",selectedUri,lookImage&&videoUri==null, onChange = {lookImage = !lookImage;Log.e("lookImage","$lookImage")}, onCancel = {navController.navigateUp()}, onCreate = {
            if (!isLimited) {
                scope.launch(Dispatchers.IO) {
                    isLoading  =true
                    val urls = if (videoUri == null) {
                        photoUris.map {
                            Oss.uploadFile("${System.currentTimeMillis()}.${StringUtil.getFileExtensionFromUri(AppGlobal.getAppContext(),it)}", uri = it)
                        }
                    }else {
                        listOf(videoUri).map { Oss.uploadFile("${System.currentTimeMillis()}.${StringUtil.getFileExtensionFromUri(AppGlobal.getAppContext(),it)}", uri = it) }
                    }
                    dvm.createPost(user.id, PostContent(text,urls,""), PostMeta(if (location=="所在位置") "" else location,if (lookModel>1) 2 else lookModel,
                        scopeList
                    ))
                    isLoading = false
                    delay(100)
                    withContext(Dispatchers.Main) {
                        navController.navigateUp()
                    }
                }

            }else {
                Toast.makeText(AppGlobal.getAppContext(),"字数超过200",Toast.LENGTH_SHORT).show()
            }
        }){
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
                Wrapper {
                    CustomTextField(
                        text,
                        onValueChange = { text = it;textCount=text.length},
                        placeholder = { BaseText("这一刻的想法...", color = PlaceholderColor, fontSize = 14.sp) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            cursorColor = IconGreen,
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        )
                    )
                }
                if (videoUri != null) {
                    Wrapper {
                        if (!isPlay) {
                            VideoItem(videoBitmap ?: R.drawable.default_cover) {
                                isPlay = true
                            }
                        }else {
                            PlayVideoScreen(videoUri!!){isPlay = false}
                        }
                    }

                }
                else {
                    LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = PaddingValues(DefaultUserPadding), verticalArrangement = Arrangement.spacedBy(5.dp), horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.width(size*3+40.dp)) {
                        items(photoUris) {uri->
                            Wrapper {
                                ImageItem(uri,size, onDelete = {
                                    dvm.loadPhotoUris(photoUris.let {
                                        val newUris = it.toMutableList()
                                        newUris.remove(uri)
                                        newUris
                                    })
                                }) { selectedUri = uri;lookImage = true }
                            }
                        }
                        if (photoUris.size < 9) {
                            item{
                                AsyncImage(ImageRequest.Builder(AppGlobal.getAppContext()).data(R.drawable.add_image).build(),contentDescription = null,
                                    modifier = Modifier
                                        .size(size)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }) {
                                            launcher.launch("image/*")
                                        }, contentScale = ContentScale.Crop)
                            }
                        }
                    }
                }
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = DividerColor)
                CreatePostItem(R.drawable.location,location) { thisController.switchTab(CreatePostEnum.SELECT_LOCATION.route) }
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = DividerColor)
                //谁可以看
                CreatePostLookItem(R.drawable.user,lookModel, svm = svm) { thisController.switchTab(CreatePostEnum.SELECT_SCOPE.route) }
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = DividerColor)
            }
            Spacer(modifier = Modifier.weight(1f))
            Wrapper(modifier = Modifier.imePadding()) {
                BaseText("字数:$textCount/$maxCount", color = if (isLimited) Color.Red else PlaceholderColor, modifier = Modifier.imePadding())
            }
        }
        LoadingDialog(isLoading)
    }
}

@Composable
fun CreatePostItem(icon:Int, title:String, onclick: () -> Unit) {
    BaseScreenItem(preContent = {
        Icon(painterResource(icon),contentDescription = null, tint = PlaceholderColor, modifier = Modifier.size(24.dp))
    },onclick, tailContent = {
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight,contentDescription = null, tint = PlaceholderColor)
    }, indication = null) {
        BaseText(title)
    }
}


@Composable
fun CreatePostLookItem(icon:Int, lookModel:Int,
    svm: SocialViewModel, onclick: () -> Unit) {
    var title by remember { mutableStateOf("谁可以看") }
    val isCanLook by remember(lookModel) { derivedStateOf { lookModel==3 } }
    val selectFriendList by svm.currentSelectFriendList.collectAsState()
    val abandonList by svm.currentAbandonFriendList.collectAsState()
    val sb1 by remember { mutableStateOf(StringBuilder()) }
    LaunchedEffect(selectFriendList) {
        sb1.append(selectFriendList.map { it.displayName })
    }
    val sb2 by remember { mutableStateOf(StringBuilder()) }
    LaunchedEffect(abandonList) {
        sb2.append(abandonList.map { it.displayName })
    }
    val sb = if (lookModel==2) sb1 else sb2
    Column{
        BaseScreenItem(preContent = {
            Icon(painterResource(icon),contentDescription = null, tint = PlaceholderColor, modifier = Modifier.size(24.dp))
        },onclick, tailContent = {
            when(lookModel) {
                0,1-> {
                    Row {
                        BaseText(if (lookModel==0) "公开" else "私密")
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight,contentDescription = null, tint = PlaceholderColor)
                    }
                }
                2,3-> {
                    title = if (lookModel==2) "谁可以看" else "谁不可以看"
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight,contentDescription = null, tint = PlaceholderColor)
                }
            }
        }, indication = null) {
            BaseText(title, color = if(isCanLook) Color.Red else Color.Black)
        }
        if (lookModel>1) {
            BaseText("-朋友: $sb", color = PlaceholderColor, fontSize = 10.sp, modifier = Modifier.padding(start = DefaultUserPadding), maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}
@Composable
fun CreatePostScaffold(title:String="",selectedUri:Uri=Uri.Builder().build(),lookImage:Boolean=false, onCancel:()->Unit, onCreate:()->Unit,onChange:()->Unit = {}, content:@Composable ColumnScope.()->Unit) {

    CanLookImage(selectedUri,lookImage, onChange = {onChange()}) {
        Column(modifier = Modifier.padding(DefaultUserPadding), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            MyTopBar(
                title,
                {
                    BaseText(
                        "取消",
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }) { onCancel() })
                },
                defaultColor = Color.White,
                action = {
                    Wrapper {
                        BaseButton(onClick =  { onCreate()}, modifier = Modifier
                            .height(30.dp)
                            .width(60.dp)) { BaseText("发表", color = Color.White, fontSize = 12.sp) }
                    }
                })
            content()
        }
    }

}
@Composable
fun ImageItem(data:Any, size: Dp,onDelete:()->Unit={}, onclick:()->Unit) {
    Box {
        AsyncImage(ImageRequest.Builder(AppGlobal.getAppContext()).data(data).build(),contentDescription = null, modifier = Modifier
            .size(size)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                onclick()
            }, contentScale = ContentScale.Crop)
        Icon(Icons.Filled.Close,contentDescription = null, tint = PlaceholderColor, modifier = Modifier
            .size(16.dp)
            .align(Alignment.TopEnd)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                onDelete()
            })
    }
}
@Composable
fun VideoItem(data: Any,onclick:  () -> Unit) {
    Box(modifier = Modifier.clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { onclick() }) {
        AsyncImage(ImageRequest.Builder(AppGlobal.getAppContext()).data(data).build(),contentDescription = null, modifier = Modifier
            .width(100.dp)
            .height(150.dp), contentScale = ContentScale.Crop)
        Icon(Icons.Filled.PlayArrow,contentDescription = null, tint = Color.White , modifier = Modifier
            .align(Alignment.Center)
            .size(50.dp)
            .padding(start = DefaultUserPadding))

    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DynamicCreateSheet(scope: CoroutineScope, launcher: ManagedActivityResultLauncher<Intent, ActivityResult>, bottomSheetState: ModalBottomSheetState, onClickPhoto:()->Unit, onClickVideo:()->Unit){
    val context = LocalContext.current
    val intent = Intent(context, CameraActivity::class.java)
    intent.putExtra("camera_model",1)
    scope.apply {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
        ) {
            TextButton(onClick = {
                launch { bottomSheetState.hide() }
                launcher.launch(intent)
            }, shape = RectangleShape) {
                BaseText("拍照", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
            TextButton(onClick = {
                launch { bottomSheetState.hide() }
                onClickPhoto()
            }, shape = RectangleShape) {
                BaseText("图片动态(最多选择九张哦)", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
            TextButton(onClick = {
                launch { bottomSheetState.hide() }
                onClickVideo()
            }, shape = RectangleShape) {
                BaseText("视频动态", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
            Spacer(Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(SurfaceColor))
            TextButton(onClick = {
                launch { bottomSheetState.hide() }
            }, shape = RectangleShape) {
                BaseText(text = "取消", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

// 状态数据类
private class FriendCircleState(
    val scope: CoroutineScope,
    val launcher: ManagedActivityResultLauncher<String, Uri?>,
    val activityLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    val mutableLauncher:  ManagedActivityResultLauncher<String, List<@JvmSuppressWildcards Uri>>,
    val videoLauncher:  ManagedActivityResultLauncher<String, Uri?>,
    val focusRequester: FocusRequester,
    private val selectedUriState: MutableState<Uri?>,
    private val urisSizeState: MutableIntState,
    var isLoading: Boolean = false,
    private val selectedVideoUriState: MutableState<Uri?>,
) {
    var selectedUri:Uri?
        get() = selectedUriState.value
        set(value) {selectedUriState.value = value}
    var urisSize: Int
        get() = urisSizeState.intValue
        set(value) { urisSizeState.intValue = value }
    var selectedVideoUri:Uri?
        get() = selectedVideoUriState.value
        set(value) { selectedVideoUriState.value = value }

}
