package com.software.jetpack.compose.chan_xin_android.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.ImageLoader
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
import com.software.jetpack.compose.chan_xin_android.ui.activity.IconButton
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
import java.sql.Timestamp
import java.util.UUID
private val DEFAULT_USER_PADDING = 8.dp
private val AVATAR_SIZE = 50.dp
private val IMAGE_GRID_SPACING = 5.dp
private val IMAGE_GRID_ITEM_SIZE = 75.dp
private val VIDEO_THUMBNAIL_SIZE = 100.dp to 150.dp // 宽x高
private val TARGET_OFFSET_DP = 100.dp // 使用dp避免密度计算
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
    var isDelete by remember { mutableStateOf(false) }
    val posts = dvm.pagingDataFlow.collectAsLazyPagingItems()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    var deletePostId by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Box {
        FriendCircleScreenUI(navController,sheetState,filePath,dvm,posts=posts,svm = svm,onFilePathChange = {
            filePath = it
        }, onDelete = {
            deletePostId = it
            isDelete = true
        }) {
            //todo:進入被點擊的好友的詳情頁面
        }
        if (isDelete) {
            AlertDialog(onDismissRequest = {}, title = {
                BaseText("是否刪除此動態", color = LittleTextColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }, text = {
                BaseText("點擊刪除之後你的這條動態會被刪除")
            },confirmButton = {
                TextButton(onClick = {
                    isDelete = false
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
                    isDelete = false
                }) {
                    BaseText("取消")
                }
            })
        }

    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FriendCircleScreenUI(navController:NavHostController,sheetState: ModalBottomSheetState,filePath:String,dvm: DynamicViewModel,svm:SocialViewModel,uvm:UserViewmodel = hiltViewModel(),posts:LazyPagingItems<Post>,modifier: Modifier = Modifier,onFilePathChange:(String)->Unit,onDelete: (String) -> Unit,enterDetail:(String)->Unit) {
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    val scope = rememberCoroutineScope()
    var urisSize by remember { mutableIntStateOf(0) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            uri: Uri? ->
        selectedUri = uri
    }
    val clickFriend by svm.clickFriend.collectAsState()
    val activityLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {result->
        if (result.resultCode==CameraUtil.IMAGE_URI_CODE) {
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra(CameraUtil.IMAGE_URI,Uri::class.java)

            }else {
                result.data?.getParcelableExtra<Uri>(CameraUtil.IMAGE_URI)
            }
            if (uri != null) {
                dvm.loadPhotoUris(listOf(uri))
                navController.switchTab(MainActivityRouteEnum.CREATE_POST_SCREEN.route)
            }
        }
        if (result.resultCode == CameraUtil.VIDEO_URI_CODE) {
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra(CameraUtil.VIDEO_URI,Uri::class.java)

            }else {
                result.data?.getParcelableExtra<Uri>(CameraUtil.VIDEO_URI)
            }
            dvm.loadVideoUri(uri)
            navController.switchTab(MainActivityRouteEnum.CREATE_POST_SCREEN.route)

        }
    }
    val mutableLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        urisSize = uris.size
        if (urisSize <= 9) {
            dvm.loadPhotoUris(uris)
            dvm.loadVideoUri(null)
            navController.switchTab(MainActivityRouteEnum.CREATE_POST_SCREEN.route)
        }
    }
    val videoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            uri: Uri? ->
        dvm.loadVideoUri(uri)
        dvm.loadPhotoUris(emptyList())
        if (uri!=null) navController.switchTab(MainActivityRouteEnum.CREATE_POST_SCREEN.route)

    }
    var isLoading by remember { mutableStateOf(false) }
    val user by uvm.myUser.collectAsState()
    val scrollState = rememberLazyListState()
    val isScrolling by remember { derivedStateOf { scrollState.isScrollInProgress } }
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var isRefresh by remember { mutableStateOf(false) }
    if (urisSize>9) {
        Toast.makeText(AppGlobal.getAppContext(),"图片选择超出限制",Toast.LENGTH_SHORT).show()
    }
    LaunchedEffect(posts.itemCount) {
        isLoading = posts.itemCount==0
    }
    if (selectedVideoUri == null) {
        ModalBottomSheetLayout(sheetState = sheetState, sheetContent = { DynamicCreateSheet(scope,activityLauncher,sheetState, onClickPhoto={
            mutableLauncher.launch("image/*")
        },onClickVideo = {
            videoLauncher.launch("video/*")
        }) }) {
            Box {
                //if——选择封面,else朋友圈界面
                if (selectedUri != null) {
                    BackHandler {
                        selectedUri = null
                    }
                    Box(contentAlignment = Alignment.Center, modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Black)) {
                        Wrapper {
                            AsyncImage(model = ImageRequest.Builder(AppGlobal.getAppContext()).data(selectedUri).build(),contentDescription = null, modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f), contentScale = ContentScale.Crop)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = DefaultUserScreenItemDp,
                                end = DefaultUserScreenItemDp,
                                bottom = DefaultUserScreenItemDp * 1.5f
                            )
                            .align(Alignment.BottomCenter), horizontalArrangement = Arrangement.SpaceBetween) {
                            Wrapper {
                                Text(
                                    "取消",
                                    color = Color.White,
                                    modifier = Modifier.clickable(
                                        indication = null,
                                        interactionSource = remember {
                                            MutableInteractionSource()
                                        }) { selectedUri = null;dvm.loadPhotoUris(emptyList());dvm.loadVideoUri(null) })
                            }
                            Wrapper {
                                Text("完成",color=Color.White,modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember {
                                        MutableInteractionSource()
                                    }) {
                                    scope.launch(Dispatchers.IO) {
                                        isLoading = true
                                        val coverUrl = Oss.uploadFile("${System.currentTimeMillis()}.${StringUtil.getFileExtensionFromUri(AppGlobal.getAppContext(),selectedUri)}",selectedUri)
                                        dvm.setCover(user.id,coverUrl)
                                        val path = AppGlobal.saveBitmapToFile(coverUrl,System.currentTimeMillis().toString())
                                        AppGlobal.saveUserRela(PreferencesFileName.USER_COVER_FILE_PATH(user.id),path)
                                        onFilePathChange(path)
                                        delay(100)
                                        selectedUri = null
                                        isLoading = false
                                    }
                                })
                            }
                        }
                    }
                }else {
                    Wrapper {
                        LazyColumnWithCover(if (filePath=="") R.drawable.default_cover else filePath,user.nickname,user.displayAvatar, modifier = modifier,listState = scrollState, onRefresh = {
                            isRefresh = true
                            dvm.setCurrentUid(user.id)
                            delay(1000)
                            isRefresh = false
                        },onChangeCover = {
                            launcher.launch("image/*")
                        }, onEnterFriendInfoDetail = {
                            enterDetail(clickFriend.userId)
                        }) {
                            items(posts.itemCount, key = {posts[it]?.postId ?:it.toString()}) {i->
                                val post = posts[i]
                                Wrapper {
                                    if (post != null) {
                                        PostItem(post,isScrolling, mid = user.id,svm = svm, onDelete = {
                                            onDelete(post.postId)
                                        }) {
                                            selectedVideoUri = it
                                        }

                                    }
                                }

                            }
                            when(posts.loadState.append) {
                                is LoadState.Loading -> {
                                    item { LoadingMoreItem() }
                                }
                                is LoadState.NotLoading -> {
                                    item { NoMoreItem() }
                                }
                                else -> {}
                            }
                        }
                    }
                    Wrapper {
                        MyTopBar(title = "", preContent = {
                            if (isRefresh) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = IconGreen)
                            }else {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = null,
                                    modifier = Modifier.clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {navController.navigateUp()}, tint = Color.White)
                            }
                        }, action = {
                            Icon(
                                painterResource(R.drawable.photo),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }) {
                                        scope.launch { sheetState.show() }
                                    })
                        }, defaultColor = Color.Transparent)
                    }

                }
                LoadingDialog(isLoading)
            }
        }
    }else {
        BackHandler {
            selectedVideoUri = null
        }
        Box(modifier = Modifier.fillMaxSize().background(color = Color.Black), contentAlignment = Alignment.Center) {
            PlayVideo(selectedVideoUri!!, defaultAspectRatio = 1f)
        }
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostItem(
    post: Post,
    isScrolling: Boolean = true,
    mid:String,
    modifier: Modifier = Modifier,
    svm: SocialViewModel,
    dvm: DynamicViewModel = hiltViewModel(),
    onDelete: (String) -> Unit,
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
    val friend by svm.getFriendInfo(mid,post.userId).collectAsState(Friend())
    val scope = rememberCoroutineScope()
    Log.e("friend_ss",friend.toString())
    var isAtTargetPosition by remember { mutableStateOf(false) }
    var isLiked by remember { mutableStateOf(false) }
    LaunchedEffect(post.postId, mid) {
        isLiked = dvm.userLikedPost(mid,post.postId)
    }
    Log.e("isLiked_postItem",isLiked.toString())
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

                    isAtTargetPosition = componentCenterX in (screenCenterX - targetOffsetPx)..(screenCenterX + targetOffsetPx) &&
                            componentCenterY in (screenCenterY - targetOffsetPx)..(screenCenterY + targetOffsetPx)
                }
        ) {
            // 头像区域
            UserAvatar(displayAvatar = friend.displayAvatar, lifecycle = lifecycle)

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f) // 占据剩余宽度，避免内容挤压
            ) {
                // 用户名
                BaseText(text = friend.displayName, color = LittleTextColor)

                // 帖子内容
                BaseText(text = post.content.text)

                // 媒体内容（图片/视频）
                MediaContent(
                    postContent = post.content,
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
                    BaseText(text = post.createTime.toTime(), color = PlaceholderColor)
                    Spacer(modifier = Modifier.weight(1f))
                    if (post.userId == mid) {
                        LittleButton(modifier=Modifier.width(20.dp),onclick = {
                            onDelete(post.postId)
                        }) {
                            Icon(Icons.Filled.Delete,contentDescription = null, tint = LittleTextColor, modifier = Modifier.size(13.dp))
                        }
                        Spacer(modifier.width(10.dp))
                    }
                    ExpandableLikeAndContent(
                        onLikeClick = {
//                            dvm.toggleLike(post.postId,mid,true)
                            isLiked = !isLiked
                            scope.launch(Dispatchers.IO) {
                                dvm.toggleLike(post.postId,mid,!isLiked)
                            }
                        },
                        onContentClick = {
                            //todo：評論
                        }
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.3.dp,
            color = Color.Gray // 显式指定颜色，避免依赖主题默认值
        )
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
                // 计算网格高度：每行3个，每行高度80dp
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
        ExpandRowMenu(expanded = isExpand, onDismissRequest = {isExpand = false}, offset = DpOffset(-35.dp,-25.dp),modifier = Modifier.height(35.dp).width(150.dp).background(color = Color.Black.copy(0.4f))) {
            DropdownMenuItem(onClick = {
                isExpand = false
                onLikeClick()
            },modifier = Modifier.height(35.dp).width(75.dp)) {
                BaseText("点赞", color = Color.White, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(3.dp))
                Icon(Icons.Filled.ThumbUp,contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            DropdownMenuItem(onClick = {
                isExpand = false
                onContentClick()
            }, modifier = Modifier.height(35.dp).width(75.dp)) {
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
    Box(modifier = modifier.defaultMinSize(minHeight = 15.dp, minWidth = 35.dp).background(backgroundColor).clickable { onclick() }, contentAlignment = Alignment.Center) {
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
