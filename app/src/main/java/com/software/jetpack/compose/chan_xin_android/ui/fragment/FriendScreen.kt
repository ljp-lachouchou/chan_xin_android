package com.software.jetpack.compose.chan_xin_android.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.CachePolicy.*
import coil.request.ImageRequest
import com.google.gson.Gson
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.cache.database.UserDatabase
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultPaddingBottom
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultPaddingTop
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserPadding
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserScreenItemDp
import com.software.jetpack.compose.chan_xin_android.entity.Friend
import com.software.jetpack.compose.chan_xin_android.entity.FriendApply
import com.software.jetpack.compose.chan_xin_android.entity.FriendRelation
import com.software.jetpack.compose.chan_xin_android.entity.FriendStatus
import com.software.jetpack.compose.chan_xin_android.entity.User
import com.software.jetpack.compose.chan_xin_android.ext.getGroupByFirstLetter
import com.software.jetpack.compose.chan_xin_android.ext.switchTab
import com.software.jetpack.compose.chan_xin_android.http.entity.ApiResult
import com.software.jetpack.compose.chan_xin_android.ui.activity.AppTopBar
import com.software.jetpack.compose.chan_xin_android.ui.activity.MainActivityRouteEnum
import com.software.jetpack.compose.chan_xin_android.ui.activity.Wrapper
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseBox
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseButton
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseScreenItem
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.base.CanLookImage
import com.software.jetpack.compose.chan_xin_android.ui.base.CustomTextField
import com.software.jetpack.compose.chan_xin_android.ui.base.LoadingDialog
import com.software.jetpack.compose.chan_xin_android.ui.base.RefreshLazyColumn
import com.software.jetpack.compose.chan_xin_android.ui.base.selectCharRows
import com.software.jetpack.compose.chan_xin_android.ui.theme.DividerColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.LittleTextColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.PlaceholderColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.SurfaceColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.TextColor
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.util.PinAYinUtil
import com.software.jetpack.compose.chan_xin_android.util.StringUtil
import com.software.jetpack.compose.chan_xin_android.util.VibratorHelper
import com.software.jetpack.compose.chan_xin_android.vm.SocialViewModel
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.UUID
import kotlin.math.roundToInt
enum class FriendScreenRouteEnum(val route:String) {
    PARENT("parent")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FriendScreen(navController:NavHostController, uvm:UserViewmodel= hiltViewModel(), svm:SocialViewModel= hiltViewModel()) {
    val activity = LocalContext.current as Activity
    // 拦截返回键，直接退出应用
    BackHandler(enabled = true) {
        activity.moveTaskToBack(true) // 切换到后台
    }
    val thisNavController = rememberNavController()
    NavHost(navController = thisNavController, startDestination = FriendScreenRouteEnum.PARENT.route) {
        composable(FriendScreenRouteEnum.PARENT.route) {MainFriendScreen(
            navController,
            uvm = uvm,
            svm = svm
        )}
    }
}


//好友详情页
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainFriendInfoScreen(navController: NavHostController,svm:SocialViewModel = hiltViewModel()) {
    val friend by svm.clickFriend.collectAsState()
    var isSelected by remember { mutableStateOf(false) }
    val sexPainter = when(friend.gender) {
        0->R.drawable.unknow
        1->R.drawable.man
        2->R.drawable.woman
        else->R.drawable.unknow
    }
    CanLookImage(data = friend.displayAvatar, isSelected = isSelected, onChange = {isSelected = false}) {
        Scaffold(topBar = {
            TopBarWithBack(
                navController,
                action = {
                    Icon(
                        Icons.Filled.Menu,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            //todo:进入朋友资料设置页面
                        })
                })
        }) {
            BaseBox(modifier = Modifier
                .fillMaxSize()
            ) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(DefaultUserPadding)
                        .padding(horizontal = DefaultUserPadding)) {
                        Wrapper {
                            AsyncImage(
                                model = ImageRequest.Builder(AppGlobal.getAppContext())
                                    .data(friend.displayAvatar).build(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(
                                        RoundedCornerShape(5.dp)
                                    )
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }) {
                                        isSelected = true
                                    },
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Wrapper {
                                    BaseText(friend.friendStatus.remark?:"", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                }
                                Spacer(modifier = Modifier.width(5.dp))
                                Wrapper {
                                    Image(painterResource(sexPainter),contentDescription = null, modifier = Modifier.size(15.dp))
                                }
                            }
                            BaseText("昵称: ${friend.nickname}", color = PlaceholderColor, fontSize = 12.sp)
                            BaseText("禅信号: ${friend.userId}", color = PlaceholderColor, fontSize = 12.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(DefaultUserPadding*2))
                    HorizontalDivider(modifier = Modifier
                        .fillMaxWidth()
                        .padding(DefaultUserPadding), thickness = 0.3.dp, color = DividerColor)
                    UserInfoScreenItem("朋友资料", onClick = {
                        //todo:朋友资料详情
                    })
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(color = SurfaceColor))
                    UserInfoScreenItem("朋友圈", onClick = {
                        //进入个人朋友圈
                    }, heightDp = DefaultUserScreenItemDp*1.5f)
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(color = SurfaceColor))
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .height(
                            DefaultUserScreenItemDp
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            //todo:跳转会话界面
                        }) {
                        Icon(
                            painterResource(R.drawable.chan_xin),
                            contentDescription = null,
                            tint = LittleTextColor,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        BaseText("发消息", color = LittleTextColor)
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 0.3.dp,
                        color = DividerColor
                    )
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .height(
                            DefaultUserScreenItemDp
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            //todo:跳转音视频通话dialog
                        }) {
                        Icon(
                            Icons.Outlined.Call,
                            contentDescription = null,
                            tint = LittleTextColor,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        BaseText("音视频通话", color = LittleTextColor)
                    }
                }
            }
        }
    }
}





//申请好友详情
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun ApplyFriendInfoScreen(navController: NavHostController,svm:SocialViewModel= hiltViewModel()) {
    val wantApplyFriend by svm.wantApplyFriend.collectAsState()
    val sexPainter = when(wantApplyFriend.gender) {
        0 -> R.drawable.unknow
        1-> R.drawable.man
        2->R.drawable.woman
        else -> R.drawable.unknow
    }
    val status = when(wantApplyFriend.status) {
        0->"待验证"
        2->"已拒绝"
        1->"已同意"
        else->""
    }
    var selectedImage by remember { mutableStateOf(false) }
    Scaffold(topBar = {TopBarWithBack(navController)}) {
        CanLookImage(isSelected = selectedImage, data = if (wantApplyFriend.avatar != "") wantApplyFriend.avatar else R.drawable.default_avatar, onChange = {selectedImage = false}) {

            BaseBox(modifier = Modifier
                .fillMaxSize()
                .background(color = SurfaceColor)) {
                Column(){
                    UserSimpleItem(
                        User(wantApplyFriend.nickname,wantApplyFriend.avatar,wantApplyFriend.gender.toByte()),
                        heightDp = DefaultPaddingTop * 1.3f,
                        imageClick = {
                            selectedImage = true
                        },
                        sexPainter = sexPainter
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(DefaultUserPadding),
                        color = DividerColor
                    )
                    BaseScreenItem(preContent = { BaseText("来源") }, onClick = {}) {
                        BaseText("来自于账号搜索", color = PlaceholderColor)
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = DividerColor,
                        thickness = 5.dp
                    )
                    TextButton(onClick = {}, enabled = false, modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            DefaultUserScreenItemDp
                        )
                        .background(Color.White)
                        .indication(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() })) {
                        BaseText(text = status, modifier = Modifier.indication(indication = null, interactionSource = remember { MutableInteractionSource() }))
                    }

                }
            }

        }
    }
}
//添加好友查询进入详情页
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun UserInfoInFriendBySearchScreen(navController: NavHostController, uvm: UserViewmodel= hiltViewModel(), svm:SocialViewModel= hiltViewModel()) {
    val user by uvm.myUser.collectAsState()
    val wantApplyFriend by svm.wantApplyFriend.collectAsState()
    val sexPainter = when(wantApplyFriend.gender) {
        0 -> R.drawable.unknow
        1-> R.drawable.man
        2->R.drawable.woman
        else -> R.drawable.unknow
    }

    var greetMsg by remember(user) { mutableStateOf("我是${user.nickname}") }
    Log.e("greetMsg",greetMsg)
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var selectedImage by remember { mutableStateOf(false) }
    CanLookImage(isSelected = selectedImage, data = if (wantApplyFriend.avatar != "") wantApplyFriend.avatar else R.drawable.default_avatar, onChange = {selectedImage = false}) {
        Scaffold(topBar = {
            TopBarWithBack(navController,"申请添加好友", action = { BaseText("") })
        }){ _->
            BaseBox(modifier = Modifier
                .fillMaxSize()
                .background(color = SurfaceColor)) {
                Column {
                    UserSimpleItem(
                        User(wantApplyFriend.nickname,wantApplyFriend.avatar,wantApplyFriend.gender.toByte()),
                        heightDp = DefaultPaddingTop * 1.3f,
                        imageClick = {
                            selectedImage = true
                        },
                        sexPainter = sexPainter
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(DefaultUserPadding),
                        color = DividerColor
                    )
                    BaseScreenItem(preContent = { BaseText("来源") }, onClick = {}) {
                        BaseText("来自于账号搜索", color = PlaceholderColor)
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = DividerColor,
                        thickness = 5.dp
                    )
                    Wrapper {
                        TextField(
                            value = greetMsg,
                            label = { BaseText("打个招呼吧") },
                            onValueChange = { greetMsg = it },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                cursorColor = IconGreen,
                                unfocusedContainerColor = Color.White,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent
                            ), modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                Text(
                                    "${greetMsg.length}/30",
                                    color = if (greetMsg.isEmpty() || greetMsg.length > 30) Color.Red else PlaceholderColor,
                                    fontSize = 12.sp
                                )
                            }
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = DividerColor,
                        thickness = 5.dp
                    )
                    BaseScreenItem(onClick = {
                        if(greetMsg.length in 1..30) {
                            scope.launch {
                                isLoading = true
                                applyFriend(user.id,wantApplyFriend.userId,greetMsg,svm)
                                delay(500)
                                isLoading = false
                            }
                        }
                    }) {
                        BaseText(
                            "添加到通讯录",
                            color = Color.Blue,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                LoadingDialog(isLoading)
            }
        }
    }
}

@Composable
fun TopBarWithBack(navController: NavHostController,title :String="",backTint:Color=Color.Black,color: Color=Color.White,action:@Composable ()->Unit = {}) {
    Box(modifier = Modifier.fillMaxWidth().background(color), contentAlignment = Alignment.Center) {
        Row(modifier = Modifier
            .fillMaxWidth().background(color)
            .padding(DefaultUserPadding), horizontalArrangement = Arrangement.SpaceBetween) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {navController.navigateUp()}, tint = backTint)
            BaseText(text = title)
            action()
        }

    }
}
@Composable
fun MyTopBar(
    title: String = "",
    preContent: @Composable () -> Unit = { BaseText("你好", color = SurfaceColor) },
    defaultColor: Color = SurfaceColor,
    action: @Composable () -> Unit = { BaseText("你好", color = SurfaceColor) }
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(defaultColor), contentAlignment = Alignment.Center) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(DefaultUserPadding), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            preContent()
            BaseText(text = title)
            action()
        }

    }
}

suspend fun applyFriend(aId: String, tId: String, greetMsg: String,svm:SocialViewModel) {
    try {
        val friendApplyResponse = svm.applyFriend(aId,tId,greetMsg)
        withContext(Dispatchers.Main) {
            Toast.makeText(AppGlobal.getAppContext(),"申请成功，请耐心等待对方通过",Toast.LENGTH_SHORT).show()
        }
    }catch (e:HttpException) {
        if (e.response() != null && e.response()!!.errorBody() != null&&Gson().fromJson(e.response()!!.errorBody()!!.string(),ApiResult::class.java).code == 2) {
            withContext(Dispatchers.Main) {
                Toast.makeText(AppGlobal.getAppContext(),"你对对方已经提交过好友申请",Toast.LENGTH_SHORT).show()
            }
        }else {
            withContext(Dispatchers.Main) {
                Toast.makeText(AppGlobal.getAppContext(),"网络可能出了些问题",Toast.LENGTH_SHORT).show()
            }
        }

    } catch (e:Exception) {
        withContext(Dispatchers.Main) {
            Toast.makeText(AppGlobal.getAppContext(),"网络可能出了些问题",Toast.LENGTH_SHORT).show()
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchFriendScreen(navController: NavHostController, uvm:UserViewmodel = hiltViewModel<UserViewmodel>(), svm:SocialViewModel = hiltViewModel<SocialViewModel>()) {
    @Stable
    data class SearchFriendScreenState(val isFocus: Boolean = false,val findModel: Int = 0,val find:String = "")
    var screenState by remember { mutableStateOf(SearchFriendScreenState()) }
    Scaffold(topBar = {
        AnimatedVisibility(visible = !screenState.isFocus, enter = expandVertically(tween(200)), exit = shrinkVertically(tween(200))) {
            TopAppBar(title = { Text(text = "添加朋友", modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DefaultUserScreenItemDp), textAlign = TextAlign.Center) }, modifier = Modifier
                .fillMaxWidth(), contentColor = TextColor, backgroundColor = Color.White, navigationIcon = {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    tint = Color.Black,
                    contentDescription = null,
                    modifier = Modifier.clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { navController.navigateUp() }
                )
            })
        }
    }) { _ ->
        AnimatedVisibility(visible = screenState.isFocus, enter = expandVertically(tween(200)), exit = shrinkVertically(tween(200))) {
            uvm.pagedUsers = null
            SearchFriendFieldScreen(svm = svm,navController = navController,find=screenState.find, onValueChange = {s:String->screenState=screenState.copy(find = s)}, isFocus = screenState.isFocus, uvm = uvm, findModel =
                screenState.findModel, findModelChange = {screenState=screenState.copy(findModel = it)}, onFindEvent = { findModel, uvm, _, find->
                findUser(findModel,uvm,find)
            }){screenState = screenState.copy(isFocus = false)}
        }
        AnimatedVisibility(visible = !screenState.isFocus, enter = expandVertically(tween(200)), exit = shrinkVertically(tween(200))) {
            SearchScreenFirstScreen(isPadding = true) { screenState = screenState.copy(isFocus = true) }
        }

    }
}
//添加好友首页
@Composable
fun SearchScreenFirstScreen(isPadding:Boolean = true,defaultHeight:Dp = DefaultUserScreenItemDp,defaultPadding:Dp = DefaultUserPadding*1.5f,defaultColor:Color = Color.Gray.copy(0.1f),onClick: () -> Unit) {
    Column {
        if (isPadding) Spacer(modifier = Modifier.height(DefaultPaddingTop))
        Surface(modifier =if(!isPadding) Modifier
            .fillMaxWidth()
            .height(DefaultUserScreenItemDp) else Modifier, color =if(!isPadding) SurfaceColor else Color.Transparent) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(defaultHeight)
                    .padding(
                        horizontal = defaultPadding,
                        vertical = if (!isPadding) defaultPadding else 0.dp
                    )
                    .background(color = defaultColor)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        onClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Row {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = null,
                        tint = PlaceholderColor
                    );Text("禅信号/手机号/昵称", color = PlaceholderColor)
                }

            }
        }
    }
}
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SearchFriendFieldScreen(
    navController: NavHostController,
    find: String,
    canFindModel:Boolean=true,
    hasLeadingIcon:Boolean = false,
    textFieldHeight:Float = 0f,
    findModel: Int,
    onValueChange: (String) -> Unit,
    findModelChange: (k: Int) -> Unit,
    isFocus: Boolean,
    uvm: UserViewmodel,
    svm: SocialViewModel,
    onFindEvent: suspend (findModel: Int, uvm: UserViewmodel, svm: SocialViewModel, find: String) -> Unit = { _,_,_,_->

    },
    onClick: () -> Unit
) {
    val user by uvm.myUser.collectAsState()
    val focusRequester = remember { FocusRequester() }
    var isLoading by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val list = uvm.pagedUsers?.collectAsLazyPagingItems()
    LaunchedEffect(isFocus) {
        if (isFocus) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }
    val selection = mapOf(0 to "手机号搜索",1 to "禅信号搜索",2 to "昵称搜索")
    val (selectedOption,onOptionSelected) = remember { mutableStateOf(selection[0]) }
    val scope = rememberCoroutineScope()
    val leadingIcon:@Composable (()->Unit)? = if (hasLeadingIcon) {
        {
            Icon(Icons.Outlined.Search,contentDescription = null, tint = PlaceholderColor)
        }
    } else {null}
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = SurfaceColor)) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DefaultUserPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Wrapper {
                    CustomTextField(
                        value = find,
                        onValueChange = onValueChange,
                        modifier = if (textFieldHeight==0f) {
                            Modifier
                                .padding(
                                    DefaultUserPadding
                                )
                                .width(TextFieldDefaults.MinWidth)
                                .focusRequester(focusRequester)
                        }else {
                            Modifier
                                .height(textFieldHeight.dp)
                                .width(300.dp)
                                .padding(
                                    end = DefaultUserPadding
                                )
                                .focusRequester(focusRequester)
                        },
                        leadingIcon =leadingIcon,
                        placeholder = {
                            Text(
                                text = "禅信号/手机号/昵称",
                                color = PlaceholderColor
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            cursorColor = IconGreen,
                        ),
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onSend = {
                            uvm.pagedUsers = null
                            scope.launch {
                                isLoading = true
                                onFindEvent(findModel,uvm,svm,find)
                                isLoading = false
                            }
                        }),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Send
                        ), singleLine = true

                    )
                }
                Text(
                    "取消",
                    color = Color.Blue,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        onClick()
                    })
            }
            if (canFindModel) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup()
                        .padding(DefaultUserPadding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    BaseText("搜索方式:")
                    selection.forEach { (k, v) ->
                        Row(
                            modifier = Modifier.selectable(
                                selected = v == selectedOption,
                                onClick = { onOptionSelected(v);findModelChange(k) },
                                role = Role.RadioButton
                            ), verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (v == selectedOption),
                                onClick = null,
                                colors = androidx.compose.material3.RadioButtonDefaults.colors(
                                    selectedColor = IconGreen
                                ),
                                modifier = Modifier.indication(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() })
                            )
                            BaseText(text = v, fontSize = 12.sp)
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = (list?.itemCount
                    ?: 0) > 0 && list?.loadState?.refresh is LoadState.NotLoading,
                enter = expandVertically(tween(200)),
                exit = shrinkVertically(tween(200))
            ) {
                LazyColumn {
                    items(list?.itemCount ?: 0) { i ->
                        val item  =list?.get(i) ?: User()
                        if (item.id != user.id) {
                            FriendScreenItem(
                                if (item.avatar != "") item.avatar
                                     else R.drawable.default_avatar,
                                onClick = {
                                    svm.loadWantApplyFriend(user =  FriendApply(item.id,item.nickname,item.avatar,item.sex.toInt()))
                                    navController.switchTab(MainActivityRouteEnum.USER_INFO_IN_FRIEND_BY_SEARCH.route)
                                }) { BaseText(list?.get(i)?.nickname ?: "") }
                        }

                    }
                }
            }
        }
        LoadingDialog(isLoading)
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SearchFriendFieldScreen(
    navController: NavHostController,
    find: String,
    canFindModel:Boolean=true,
    hasLeadingIcon:Boolean = false,
    textFieldHeight:Float = 0f,
    findModel: Int,
    list:List<Friend>,
    onValueChange: (String) -> Unit,
    findModelChange: (k: Int) -> Unit,
    isFocus: Boolean,
    uvm: UserViewmodel,
    svm: SocialViewModel,
    onFindEvent: suspend (findModel: Int, uvm: UserViewmodel, svm: SocialViewModel, find: String) -> Unit = { _,_,_,_->},
    onClick: () -> Unit
) {
    val user by uvm.myUser.collectAsState()
    val focusRequester = remember { FocusRequester() }
    var isLoading by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(isFocus) {
        if (isFocus) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }
    val selection = mapOf(0 to "手机号搜索",1 to "禅信号搜索",2 to "昵称搜索")
    val (selectedOption,onOptionSelected) = remember { mutableStateOf(selection[0]) }
    val scope = rememberCoroutineScope()
    val leadingIcon:@Composable (()->Unit)? = if (hasLeadingIcon) {
        {
            Icon(Icons.Outlined.Search,contentDescription = null, tint = PlaceholderColor)
        }
    } else {null}
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = SurfaceColor)) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DefaultUserPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Wrapper {
                    CustomTextField(
                        value = find,
                        onValueChange = onValueChange,
                        modifier = if (textFieldHeight==0f) {
                            Modifier
                                .padding(
                                    DefaultUserPadding
                                )
                                .width(TextFieldDefaults.MinWidth)
                                .focusRequester(focusRequester)
                        }else {
                            Modifier
                                .height(textFieldHeight.dp)
                                .width(300.dp)
                                .padding(
                                    end = DefaultUserPadding
                                )
                                .focusRequester(focusRequester)
                        },
                        leadingIcon =leadingIcon,
                        placeholder = {
                            Text(
                                text = "禅信号/手机号/昵称",
                                color = PlaceholderColor
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            cursorColor = IconGreen,
                        ),
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onSend = {
                            uvm.pagedUsers = null
                            scope.launch {
                                isLoading = true
                                onFindEvent(findModel,uvm,svm,find)
                                isLoading = false
                            }
                        }),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Send
                        ), singleLine = true

                    )
                }
                Text(
                    "取消",
                    color = Color.Blue,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        onClick()
                    })
            }
            if (canFindModel) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup()
                        .padding(DefaultUserPadding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    BaseText("搜索方式:")
                    selection.forEach { (k, v) ->
                        Row(
                            modifier = Modifier.selectable(
                                selected = v == selectedOption,
                                onClick = { onOptionSelected(v);findModelChange(k) },
                                role = Role.RadioButton
                            ), verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (v == selectedOption),
                                onClick = null,
                                colors = androidx.compose.material3.RadioButtonDefaults.colors(
                                    selectedColor = IconGreen
                                ),
                                modifier = Modifier.indication(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() })
                            )
                            BaseText(text = v, fontSize = 12.sp)
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = list.isNotEmpty(),
                enter = expandVertically(tween(200)),
                exit = shrinkVertically(tween(200))
            ) {
                LazyColumn {
                    items(list.size) { i ->
                        val item  = list[i]
                        if (item.userId != user.id) {
                            FriendScreenItem(
                                if (item.avatarUrl != "") item.avatarUrl
                                else R.drawable.default_avatar,
                                onClick = {
                                    svm.loadClickFriend(friend = item)
                                    navController.switchTab(MainActivityRouteEnum.MAIN_FRIEND_INFO.route)
                                }) { BaseText(list[i].displayName) }
                        }

                    }
                }
            }
        }
        LoadingDialog(isLoading)
    }
}

suspend fun findUser(findModel: Int, uvm: UserViewmodel, find: String) {
    try {
        return if (find == "") uvm.findUser(name = "禅信号/手机号/昵称") else when (findModel) {
            0 -> uvm.findUser(phone = find)
            1 -> uvm.findUser(ids = StringUtil.listToString(listOf(find)))
            2 -> uvm.findUser(name =  find)
            else -> uvm.findUser()
        }
    }catch (e:Exception) {
        withContext(Dispatchers.Main) {Toast.makeText(AppGlobal.getAppContext(),"网络可能出了些问题",Toast.LENGTH_SHORT).show()}
        return
    }
}


private suspend fun getGroup(originalList:List<Friend>,mainEvent:(groups: List<Pair<String, List<Friend>>>)->Unit) {
    withContext(Dispatchers.Default) {
        val groups = originalList.groupBy {
            if (it.friendStatus.remark!!.isEmpty())
                it.nickname.getGroupByFirstLetter()
            else
                it.friendStatus.remark.getGroupByFirstLetter()
        }.toList().sortedWith(compareBy(groupComparator) { it.first })
        withContext(Dispatchers.Main) {
            mainEvent(groups)
        }
    }
}
//好友首页
@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainFriendScreen(navController: NavHostController, uvm: UserViewmodel, svm: SocialViewModel) {
    var isLoading by remember { mutableStateOf(false) }
    val user by uvm.myUser.collectAsState()
    val listState = rememberLazyListState()
    val groupedFriends by svm.currentGroup.collectAsState()
    var showSidebar by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val listCache by svm.friendCacheList.collectAsState()
    LaunchedEffect(user.id) {
        if (user.id != "") {
            withContext(Dispatchers.IO) {
                val originalList:List<Friend> = if (AppGlobal.isNetworkValid()) {
                    svm.getFriendList(user.id)
                }else {
                    listCache
                }
                svm.loadCurrentFriendList(originalList)
                if (originalList.sortedBy { it.userId }==listCache.sortedBy { it.userId }) {
                    if (groupedFriends.isEmpty()) {
                        isLoading = true
                        getGroup(listCache){groups ->
                            svm.loadCurrentGroup(groups)
                            isLoading = false
                            showSidebar = true
                        }
                    }
                    showSidebar = true
                    Log.e("hhhssss","ssss")
                    return@withContext
                }
                isLoading = true
                getGroup(originalList){groups ->
                    svm.loadCurrentGroup(groups)
                    isLoading = false
                    showSidebar = true
                }
            }

        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(topBar = {
            MyTopBar(title = "朋友", action = {
                Icon(
                    painterResource(R.drawable.apply_friend),
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            navController.switchTab(
                                MainActivityRouteEnum.FIND_USER_IN_FRIEND.route
                            )
                        })
            })
        }) { padding ->
            LazyColumn(modifier = Modifier.padding(padding), state = listState) {
                item(key="search_bar") {
                    Wrapper {
                        SearchScreenFirstScreen(isPadding = false, defaultHeight = 25.dp, defaultColor = Color.White, defaultPadding = 10.dp) { navController.switchTab(MainActivityRouteEnum.MAIN_FRIEND_SEARCH_SCREEN.route) }
                    }
                }
                item(key = "handle_apply") {
                    FriendScreenItem(data = R.drawable.handle_apply, onClick = {navController.switchTab(MainActivityRouteEnum.APPLY_FRIEND_LIST.route)}) {
                        BaseText("请求列表")
                    }
                }
                item(key="new_friend") {
                    Wrapper {
                        FriendScreenItem(data = R.drawable.new_friend, onClick = {
                            navController.switchTab(MainActivityRouteEnum.HANDLE_FRIEND_APPLY_LIST.route)
                        }) {
                            BaseText("新的朋友")
                        }
                    }
                }
                groupedFriends.forEach{(k,v)->
                    stickyHeader(key = "header_$k") {
                        Wrapper {
                            BaseText(color = PlaceholderColor,text=k, modifier = Modifier
                                .fillMaxWidth()
                                .background(color = SurfaceColor)
                                .padding(
                                    DefaultUserPadding
                                ))
                        }
                    }
                    itemsIndexed(v,key = {_,item->item.userId}) {_,friend->
                        Wrapper {
                            FriendScreenItem(friend.displayAvatar, onClick = {
                                svm.loadClickFriend(friend)
                                navController.switchTab(MainActivityRouteEnum.MAIN_FRIEND_INFO.route)
                            }) {
                                BaseText(friend.displayName)
                            }
                        }
                    }
                    item(key = "group_divider_$k") {
                        HorizontalDivider(modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal =
                                DefaultUserPadding
                            ), color = PlaceholderColor, thickness = 0.1.dp)
                    }
                }
                item {
                    Wrapper {
                        Text(
                            text = "${listCache.size}个朋友",
                            color = PlaceholderColor, // 醒目颜色
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        if (showSidebar) {
            ContactSideBar(modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 15.dp)){ selectedIndex ->
                var targetPosition = 0
                val offsetItemCount = 3
                val targetGroupIndex = groupedFriends.indexOfFirst { group ->
                    if(selectedIndex==26) {
                        group.first.uppercase()=="#"
                    }else {
                        group.first.uppercase() == ('A'+selectedIndex).toString().uppercase()
                    }
                }
                if (targetGroupIndex != -1) {
                    for (i in 0 until targetGroupIndex) {
                        val (_,friend) = groupedFriends[i]
                        targetPosition+=friend.size+1+1
                    }
                    scope.launch { listState.animateScrollToItem(index = targetPosition+offsetItemCount) }
                }
            }
        }
        Wrapper(modifier = Modifier.fillMaxSize()) {
            LoadingDialog(isLoading)
        }
    }
}
@Composable
fun MainFriendSearchScreen(navController: NavHostController,svm: SocialViewModel= hiltViewModel(),uvm:UserViewmodel= hiltViewModel()) {
    val isFocus by remember { mutableStateOf(true) }
    var findModel by remember { mutableIntStateOf(0) }
    var find by remember { mutableStateOf("") }
    val list by svm.currentFriendList.collectAsState()
    SearchFriendFieldScreen(svm = svm, navController=navController,
        hasLeadingIcon = true,
        canFindModel = false,
        textFieldHeight = 25f,
        isFocus = isFocus,
        list = list.filter { friend: Friend ->
            find != "" && friend.userId.contains(find) or PinAYinUtil.getFirstLetter(
                friend.nickname
            ).contains(find) or PinAYinUtil.getFirstLetter(friend.displayName).contains(find)
        },
        uvm = uvm,
        findModel = findModel,
        find = find,
        onValueChange = { find = it },
        findModelChange = { findModel = it }) { navController.navigateUp() }
}
val groupComparator = compareBy<String> {it == "#"}.thenBy { it }
@SuppressLint(
    "UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition"
)
@Composable
fun ApplyFriendScreen(navController:NavHostController,uvm:UserViewmodel = hiltViewModel(),svm:SocialViewModel= hiltViewModel()) {
    @Stable
    data class ApplyFriendScreenState(
        var list: List<FriendApply> = emptyList()
    )

    val user by uvm.myUser.collectAsState()
    Log.e("ApplyFriendScreen_user", user.toString())

    val uid by remember(user) { derivedStateOf { user.id } }
    val screenState by remember { mutableStateOf(ApplyFriendScreenState()) }
    val applyFriendList by svm.applyFriendList.collectAsState()

// 将状态转换为不可变数据结构，避免并发修改问题
    val friendItems by remember(screenState.list) {
        derivedStateOf {
            screenState.list.map { friend ->
                FriendItem(
                    friend = friend,
                    statusText = when (friend.status) {
                        0 -> "待验证"
                        1 -> "已同意"
                        2 -> "已拒绝"
                        else -> ""
                    },
                    greetPre = "我: "
                )
            }
        }
    }
    LaunchedEffect(uid, screenState.list) {
        if (uid != "") {
            if (AppGlobal.isNetworkValid()) {
                screenState.list = svm.getFriendApplyList(uid)
                UserDatabase.getInstance().socialDao()
                    .saveFriendApply(screenState.list.map { it.copy(applicantId = uid) })
            } else {
                screenState.list = applyFriendList
            }
        }
    }
    BaseApplyScreen(navController, title = "请求列表(看看你想添加的人吧)", action = {
        BaseText(
            "添加好友",
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                navController.switchTab(MainActivityRouteEnum.FIND_USER_IN_FRIEND.route)
            })
    }, onClick = {item->
        svm.loadWantApplyFriend(
            item.friend
        )
        navController.switchTab(MainActivityRouteEnum.APPLY_FRIEND_INFO.route)
    },screenState.list,friendItems)
}
data class FriendItem(
    val friend: FriendApply,
    val statusText: String,
    val greetPre:String
)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BaseApplyScreen(navController: NavHostController,title:String,action:@Composable () -> Unit={},onClick: (FriendItem) -> Unit,list:List<FriendApply>,friendItems:List<FriendItem>) {
    var isLoading by remember { mutableStateOf(true) }
    Scaffold(topBar = {
        TopBarWithBack(navController, title) {
            action()
        }
    }) {
        Box {
            LaunchedEffect(Unit) {
                delay(300)
                isLoading = false
            }
            key(list) {
                RefreshLazyColumn {
                    items(friendItems) { item ->
                        FriendScreenItem(
                            if (item.friend.avatar != "") item.friend.avatar else R.drawable.default_avatar,
                            tailContent = {
                                BaseText(text = item.statusText, color = PlaceholderColor)
                            },
                            onClick = {onClick(item)}
                        ) {
                            Column {
                                BaseText(item.friend.nickname, fontWeight = FontWeight.Bold)
                                BaseText(
                                    color = PlaceholderColor,
                                    text = item.greetPre + item.friend.greetMsg,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
            LoadingDialog(isLoading)
        }
    }
}
@SuppressLint(
    "UnusedMaterialScaffoldPaddingParameter"
)
@Composable
fun HandleFriendApplyListScreen(navController: NavHostController,uvm: UserViewmodel= hiltViewModel(),svm: SocialViewModel= hiltViewModel()) {
    @Stable
    data class HandleFriendApplyState(
        var list: List<FriendApply> = emptyList()
    )
    val user by uvm.myUser.collectAsState()
    Log.e("ApplyFriendScreen_user", user.toString())

    val uid by remember(user) { derivedStateOf { user.id } }
    val screenState by remember { mutableStateOf(HandleFriendApplyState()) }
    val handleFriendApplyList by svm.handleFriendApplyList.collectAsState()

// 将状态转换为不可变数据结构，避免并发修改问题
    val friendItems by remember(screenState.list) {
        derivedStateOf {
            screenState.list.map { friend ->
                FriendItem(
                    friend = friend,
                    statusText = when (friend.status) {
                        0 -> "待验证"
                        1 -> "已同意"
                        2 -> "已拒绝"
                        else -> ""
                    },
                    greetPre = "${friend.nickname}: "
                )
            }
        }
    }
    LaunchedEffect(uid, screenState.list) {
        if (uid != "") {
            if (AppGlobal.isNetworkValid()) {
                screenState.list = svm.getHandleFriendApplyList(uid)
                UserDatabase.getInstance().socialDao()
                    .saveFriendApply(screenState.list.map { it.copy(userId = uid, applicantId = it.userId) })
            } else {
                screenState.list = handleFriendApplyList
            }
        }
    }
    BaseApplyScreen(navController, title = "新的朋友", action = { BaseText("") }, onClick = {item->
        svm.loadWantApplyFriend(item.friend)
        navController.switchTab(MainActivityRouteEnum.HANDLE_FRIEND_APPLY_INFO.route)

    },screenState.list,friendItems)

}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HandleFriendApplyInfoScreen(navController: NavHostController,svm:SocialViewModel = hiltViewModel(),uvm: UserViewmodel = hiltViewModel()) {
    val wantApplyFriend by svm.wantApplyFriend.collectAsState()
    val user by uvm.myUser.collectAsState()
    val sexPainter = when(wantApplyFriend.gender) {
        0 -> R.drawable.unknow
        1-> R.drawable.man
        2->R.drawable.woman
        else -> R.drawable.unknow
    }
    val status = when(wantApplyFriend.status) {
        0->"待验证"
        2->"已拒绝"
        1->"已同意"
        else->""
    }
    val enabled by remember(wantApplyFriend.status) { derivedStateOf { if (wantApplyFriend.status==0){true}else false } }
    var selectedImage by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Scaffold(topBar = {TopBarWithBack(navController)}) {
        CanLookImage(isSelected = selectedImage, data = if (wantApplyFriend.avatar != "") wantApplyFriend.avatar else R.drawable.default_avatar, onChange = {selectedImage = false}) {
            BaseBox(modifier = Modifier
                .fillMaxSize()
                .background(color = SurfaceColor)) {
                Column{
                    UserSimpleItem(
                        User(wantApplyFriend.nickname,wantApplyFriend.avatar,wantApplyFriend.gender.toByte()),
                        heightDp = DefaultPaddingTop * 1.3f,
                        imageClick = {
                            selectedImage = true
                        },
                        sexPainter = sexPainter
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(DefaultUserPadding),
                        color = DividerColor
                    )
                    BaseScreenItem(preContent = { BaseText("来源") }, onClick = {}) {
                        BaseText("来自于账号搜索", color = PlaceholderColor)
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = DividerColor,
                        thickness = 5.dp
                    )
                    BaseScreenItem(preContent = {BaseText("招呼")}, onClick = {}) {
                        BaseText(wantApplyFriend.greetMsg,color= PlaceholderColor)
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = DividerColor,
                        thickness = 5.dp
                    )
                    TextButton(onClick = {
                        navController.switchTab(MainActivityRouteEnum.HANDLE_FRIEND_APPLY_VERIFY.route)
                    }, enabled = enabled, modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            DefaultUserScreenItemDp
                        )
                        .background(Color.White), shape = RoundedCornerShape(0.dp)
                    ) {
                        BaseText(text = status)
                    }
                    if (enabled) {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = DividerColor,
                            thickness = 5.dp
                        )
                        TextButton(onClick = {
                            svm.loadWantApplyFriend(wantApplyFriend.copy(status=2))
                            scope.launch {
                                svm.handleFriendApply(wantApplyFriend.userId,user.id,false)
                            }
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                DefaultUserScreenItemDp
                            )
                            .background(Color.White), shape = RoundedCornerShape(0.dp)
                        ) {
                            BaseText(text = "拒绝")
                        }
                    }

                }
            }

        }
    }
}
//验证好友
@OptIn(ExperimentalTextApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HandleFriendApplyVerifyScreen(navController: NavHostController, uvm: UserViewmodel= hiltViewModel(), svm: SocialViewModel) {
    val wantApplyFriend by svm.wantApplyFriend.collectAsState()
    var remark by remember { mutableStateOf(wantApplyFriend.nickname) }
    var isFocused by remember { mutableStateOf(false) }
    val user by uvm.myUser.collectAsState()
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
   Box(modifier = Modifier.fillMaxSize()) {
       HandleFriendApplyVerifyUI(scope,navController,remark,isFocused,wantApplyFriend,{remark=it},{isFocused = it.isFocused}){
           svm.loadWantApplyFriend(wantApplyFriend.copy(status = 1))
           scope.launch(Dispatchers.IO) {
               isLoading = true
               svm.handleFriendApply(wantApplyFriend.userId,user.id,true)
               svm.updateFriendStatus(user.id,wantApplyFriend.userId, friendStatus = FriendStatus(false,false,false,remark))
               UserDatabase.getInstance().socialDao().saveFriendRelation(listOf(FriendRelation(0,user.id,wantApplyFriend.userId,
                   FriendStatus(false,false,false,remark)),FriendRelation(0,wantApplyFriend.userId,user.id,
                   FriendStatus())))
               delay(300)
               isLoading = false
               withContext(Dispatchers.Main) {
                   navController.navigateUp()
               }
           }
       }
       LoadingDialog(isLoading)
   }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HandleFriendApplyVerifyUISheetContent(greetMsg:String,sheetState:ModalBottomSheetState,scope:CoroutineScope,onValueChange: (String) -> Unit) {
    val configuration = LocalConfiguration.current
    val sb = StringBuilder()
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(configuration.screenHeightDp.dp / 2)
        .padding(15.dp)
        .padding(bottom = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Icon(
                Icons.Filled.Close,
                contentDescription = null,
                Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) { scope.launch { sheetState.hide() } })
        }
        Spacer(Modifier.weight(0.2f))
        val booleanArray = selectCharRows(greetMsg)
        greetMsg.forEachIndexed { i,char->
            if (booleanArray[i]) {
                sb.append(char)
            }
        }
        Spacer(Modifier.weight(1f))
        BaseButton(onClick = {
            scope.launch { sheetState.hide() }
            if (sb.toString().isNotEmpty()) {
                onValueChange(sb.toString())
            }
        }, modifier = Modifier
            .height(45.dp)
            .width(145.dp)) {
            BaseText("完成", color = Color.White)
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HandleFriendApplyVerifyUI(scope:CoroutineScope,navController:NavHostController,remark:String,isFocused:Boolean,wantApplyFriend:FriendApply,onValueChange:(String)->Unit, onFocusChanged: (FocusState) -> Unit,onClick: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    ModalBottomSheetLayout(sheetContent = {HandleFriendApplyVerifyUISheetContent(wantApplyFriend.greetMsg,sheetState,scope,onValueChange=onValueChange)},sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp), sheetState = sheetState) {
        Scaffold(topBar = { TopBarWithBack(navController,"通过好友验证", action = { Text("") }) }) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .fillMaxSize()
                .padding(bottom = DefaultPaddingBottom * 3)) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(DefaultUserPadding * 1.5f)) {
                    BaseText("备注",color= PlaceholderColor, modifier = Modifier.padding(start = 15.dp))
                    TextField(
                        value = remark,
                        onValueChange = onValueChange,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Gray.copy(0.1f),
                            unfocusedContainerColor = Color.Gray.copy(0.1f),
                            cursorColor = IconGreen,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = PlaceholderColor
                        ), modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { state ->
                                onFocusChanged(state)
                            }, singleLine = !isFocused
                    )
                    val clickText = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = PlaceholderColor, textDecoration = null)) {
                            append("\"${wantApplyFriend.greetMsg}\"  ")
                        }
                        pushStringAnnotation("从招呼中选择", "从招呼中选择")
                        withStyle(style = SpanStyle(color = LittleTextColor, textDecoration = null)) {
                            append("从招呼中选择")
                        }
                        pop()
                    }
                    ClickableText(clickText, modifier = Modifier.padding(start = 15.dp)) { offset ->
                        clickText.getStringAnnotations(offset, offset).firstOrNull()
                            ?.let { annotation ->
                                when (annotation.tag) {
                                    "从招呼中选择" -> {
                                        scope.launch { sheetState.show() }
                                    }
                                }
                            }
                    }
                }
                Box(
                    modifier = Modifier
                        .height(160.dp)
                        .aspectRatio(1f)
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(0.4f),
                                    Color.White.copy(0.3f),
                                    Color.White.copy(0.2f),
                                    Color.White.copy(0.1f)
                                )
                            ),
                            RoundedCornerShape(0.dp)
                        ), contentAlignment = Alignment.Center
                ){
                    BaseButton(
                        onClick = {
                            onClick()
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .aspectRatio(3f)
                    ) {
                        BaseText("发送", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun FriendScreenItem( data: Any, onClick: () -> Unit,tailContent:@Composable ()->Unit={},content: @Composable () -> Unit) {
    BaseScreenItem(preContent = {
        AsyncImage(
            model = ImageRequest.Builder(AppGlobal.getAppContext()).data(data).build(),
            contentDescription = null, modifier = Modifier
                .size(35.dp)
                .clip(RoundedCornerShape(5.dp)), contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.default_avatar)
        )
    }, onClick = onClick, tailContent = tailContent) {
        content()
    }
}


@SuppressLint("ModifierFactoryUnreferencedReceiver", "UnnecessaryComposedModifier")
fun Modifier.clickableWithPosition(
    onClick: (Offset) -> Unit
): Modifier = composed {
    pointerInput(Unit) {
        detectTapGestures(
            onTap = { offset ->
                onClick(offset)
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedTransitionTargetStateParameter", "RememberReturnType")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContactSideBar(
    modifier: Modifier = Modifier,
    letters: List<String> = ('A'..'Z').map { it.toString() }.toMutableList().apply { add("#") }.toList(),
    topPadding: Dp = DefaultPaddingTop*2,
    itemCircleSize: Float = 12.5f,
    spacerSize: Float = 1.5f,
    commonColor: Color = Color.Gray.copy(alpha = 0.3f),
    selectColor: Color = IconGreen,
    commonTextColor: Color = Color.Black,
    selectTextColor: Color = Color.White,
    vibratorHelper: VibratorHelper = VibratorHelper(AppGlobal.getAppContext()),
    friendListEvent: (Int) -> Unit
) {
    Log.e("SideBarState","SideBarState")

    @Stable
    data class SideBarState(
        val selectedIndex: Int = -1,
        val animationState: String = "idle",
        val clickCount: Int = 0,
        val touchY: Float = 0f
    )
    var sideBarState by remember {
        mutableStateOf(SideBarState())
    }


    val componentPositions = remember { mutableMapOf<Int, Offset>() }
    var sideHeight by remember { mutableFloatStateOf(0f) }

    val transition = updateTransition(sideBarState.clickCount, label = "animationTransition")

    val scale by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 300, easing = FastOutSlowInEasing)
        }, label = "scale"
    ) {
        if (sideBarState.animationState == "showing") 1f else 0f
    }

    val alpha by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 200)
        }, label = "alpha"
    ) {
        if (sideBarState.animationState == "showing") 1f else 0f
    }

    // 只在clickCount变化时执行，避免其他状态变化触发
    LaunchedEffect(sideBarState.clickCount) {
        delay(300)
        if (sideBarState.animationState == "showing") {
            sideBarState = sideBarState.copy(animationState = "hiding")
        }
    }
    Box(modifier = modifier.padding(top = topPadding)) {
        key(letters) {
            Column(
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        sideHeight = coordinates.size.height.toFloat()
                    }
                    .pointerInteropFilter { event ->
                        when (event.action) {
                            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                                val newTouchY = event.y
                                if (sideHeight > 0) {
                                    val letterHeight = sideHeight / letters.size
                                    val index = ((newTouchY / letterHeight).coerceIn(
                                        0f, (letters.size - 1).toFloat()
                                    )).roundToInt()

                                    if (sideBarState.selectedIndex != index) {
                                        vibratorHelper.vibrate()
                                    } else if (sideBarState.touchY != newTouchY) {
                                        sideBarState = sideBarState.copy(touchY = newTouchY)
                                    }
                                    sideBarState = sideBarState.copy(
                                        selectedIndex = index,
                                        animationState = "showing",
                                        clickCount = sideBarState.clickCount + 1,
                                        touchY = newTouchY
                                    )
                                    friendListEvent(index)
                                }
                                true
                            }

                            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                true
                            }

                            else -> false
                        }
                    }
            ) {
                letters.forEachIndexed { index, letter ->
                    key(index) {
                        Surface(
                            color = if (sideBarState.selectedIndex == index) selectColor else commonColor,
                            modifier = Modifier
                                .size(itemCircleSize.dp)
                                .onGloballyPositioned { coordinates ->
                                    componentPositions[index] = coordinates.positionInRoot()
                                },
                            shape = CircleShape
                        ) {
                            // 使用remember避免每次重组都创建TextStyle
                            val textStyle = remember(sideBarState.selectedIndex == index) {
                                androidx.compose.ui.text.TextStyle(
                                    textAlign = TextAlign.Center,
                                    fontSize = 12.sp,
                                    color = if (sideBarState.selectedIndex == index) selectTextColor else commonTextColor
                                )
                            }
                            Text(
                                text = letter,
                                style = textStyle
                            )
                        }
                        Spacer(modifier = Modifier.height(spacerSize.dp))
                    }
                }
            }
        }
    }

    // 使用sideBarState.selectedIndex作为key，避免不必要的重组
    if (componentPositions[sideBarState.selectedIndex] != null && sideBarState.animationState != "idle") {
        val globalPosition = componentPositions[sideBarState.selectedIndex]!!

        // 使用key确保只有当selectedIndex或animationState变化时才重组
        key(sideBarState.selectedIndex, sideBarState.animationState) {
            Surface(
                color = commonColor,
                modifier = modifier
                    .size(40.dp * scale)
                    .offset(
                        (-itemCircleSize - 1f).dp,
                        topPadding + ((sideBarState.selectedIndex - 1) * (spacerSize + itemCircleSize)).dp
                    )
                    .alpha(alpha),
                shape = CircleShape
            ) {
                Text(
                    text = letters[sideBarState.selectedIndex],
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    color = selectTextColor
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun Example() {
    ContactSideBar(letters = (1..26).map { ('a' + it - 1).toString() }.toList()) {
        Log.e("selectedIndex","$it")
    }
}