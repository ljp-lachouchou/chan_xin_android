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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
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
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.gson.Gson
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.cache.database.UserDatabase
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultPaddingTop
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserPadding
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserScreenItemDp
import com.software.jetpack.compose.chan_xin_android.entity.FriendApply
import com.software.jetpack.compose.chan_xin_android.entity.User
import com.software.jetpack.compose.chan_xin_android.ext.switchTab
import com.software.jetpack.compose.chan_xin_android.http.entity.ApiResult
import com.software.jetpack.compose.chan_xin_android.ui.activity.AppTopBar
import com.software.jetpack.compose.chan_xin_android.ui.activity.MainActivityRouteEnum
import com.software.jetpack.compose.chan_xin_android.ui.activity.Wrapper
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseBox
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseScreenItem
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.base.LittleText
import com.software.jetpack.compose.chan_xin_android.ui.base.LoadingDialog
import com.software.jetpack.compose.chan_xin_android.ui.base.RefreshLazyColumn
import com.software.jetpack.compose.chan_xin_android.ui.theme.DividerColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.PlaceholderColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.SurfaceColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.TextColor
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.util.StringUtil
import com.software.jetpack.compose.chan_xin_android.util.VibratorHelper
import com.software.jetpack.compose.chan_xin_android.vm.SocialViewModel
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel_HiltModules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.time.format.TextStyle
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
        composable(FriendScreenRouteEnum.PARENT.route) {MainFriendScreen(navController,thisNavController, uvm = uvm, svm = svm)}
    }

}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun UserInfoInFriendBySearchScreen(navController: NavHostController, uvm: UserViewmodel= hiltViewModel(), svm:SocialViewModel= hiltViewModel()) {
    val user by uvm.myUser.collectAsState()
    val wantApplyFriend by svm.wantApplyFriend.collectAsState()
    val sexPainter = when(wantApplyFriend.sex.toInt()) {
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
    Scaffold(topBar = {
        TopBarWithBack(navController,"申请添加好友", action = { BaseText("") })
    }){ _->
        BaseBox(modifier = Modifier
            .fillMaxSize()
            .background(color = SurfaceColor)) {
            Column {
                UserSimpleItem(
                    wantApplyFriend,
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
                        ), modifier = Modifier.fillMaxWidth()
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = DividerColor,
                    thickness = 5.dp
                )
                BaseScreenItem(onClick = {
                    scope.launch {
                        isLoading = true
                        applyFriend(user.id,wantApplyFriend.id,greetMsg,svm)
                        delay(500)
                        isLoading = false
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
        if (selectedImage) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
            AsyncImage(model = ImageRequest.Builder(AppGlobal.getAppContext()).data(if (wantApplyFriend.avatar != "") wantApplyFriend.avatar else R.drawable.default_avatar).build(),contentDescription = null, modifier = Modifier.fillMaxWidth().aspectRatio(1f), contentScale = ContentScale.Crop)
            }
        }
    }
}

@Composable
fun TopBarWithBack(navController: NavHostController,title :String,action:@Composable ()->Unit = {}) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(DefaultUserPadding), horizontalArrangement = Arrangement.SpaceBetween) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {navController.navigateUp()})
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
            SearchScreenFirstScreen { screenState = screenState.copy(isFocus = true) }
        }

    }
}
@Composable
fun SearchScreenFirstScreen(isPadding:Boolean = true,onClick: () -> Unit) {
    Column {
        if (isPadding) Spacer(modifier = Modifier.height(DefaultPaddingTop))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(DefaultUserScreenItemDp)
                .background(color = Color.Gray.copy(0.1f))
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
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SearchFriendFieldScreen(navController:NavHostController,find:String,findModel:Int,onValueChange:(String)->Unit,findModelChange:(k:Int)->Unit,isFocus:Boolean,uvm:UserViewmodel,svm:SocialViewModel,onFindEvent:suspend (findModel:Int,uvm:UserViewmodel,svm:SocialViewModel,find:String)->Unit,onClick: () -> Unit) {
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
    val users = uvm.pagedUsers?.collectAsLazyPagingItems()

    val selection = mapOf(0 to "手机号搜索",1 to "禅信号搜索",2 to "昵称搜索")
    val (selectedOption,onOptionSelected) = remember { mutableStateOf(selection[0]) }
    val scope = rememberCoroutineScope()
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
                    TextField(
                        value = find,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                DefaultUserPadding
                            )
                            .focusRequester(focusRequester),
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
                            cursorColor = IconGreen
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
                        )
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

            AnimatedVisibility(
                visible = (users?.itemCount
                    ?: 0) > 0 && users?.loadState?.refresh is LoadState.NotLoading,
                enter = expandVertically(tween(200)),
                exit = shrinkVertically(tween(200))
            ) {
                LazyColumn {
                    items(users?.itemCount ?: 0) { i ->
                        if (users?.get(i)?.id != user.id) {
                            FriendScreenItem(
                                if (users?.get(i)?.avatar != "") users?.get(i)?.avatar
                                    ?: R.drawable.default_avatar else R.drawable.default_avatar,
                                onClick = {
                                    svm.loadWantApplyFriend(user = users?.get(i) ?: User())
                                    navController.switchTab(MainActivityRouteEnum.USER_INFO_IN_FRIEND_BY_SEARCH.route)
                                }) { BaseText(users?.get(i)?.nickname ?: "") }
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainFriendScreen(navController: NavHostController,thisNavController:NavHostController,uvm: UserViewmodel,svm:SocialViewModel) {
    data class MainFriendScreenState(val isFocus: Boolean = false,val findModel: Int = 0,val find:String = "")
    var screenState by remember { mutableStateOf(MainFriendScreenState()) }
    Log.e("ContactSideBars","ContactSideBar")
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(topBar = {
                AppTopBar(title = "朋友", navigationIcon = null, actions = {
                    Icon(
                        painterResource(R.drawable.apply_friend),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                navController.switchTab(
                                    MainActivityRouteEnum.FIND_USER_IN_FRIEND.route
                                )
                            })
                })
            }) { padding ->
            BaseBox {
                LazyColumn(modifier = Modifier.padding(padding)) {
                    item {
                        Wrapper {
                            AnimatedVisibility(visible = screenState.isFocus, enter = expandVertically(tween(200)), exit = shrinkVertically(tween(200))) {
                                uvm.pagedUsers = null
                                SearchFriendFieldScreen(svm = svm, navController=navController,
                                    isFocus = screenState.isFocus,
                                    uvm = uvm,
                                    findModel = screenState.findModel,
                                    find = screenState.find,
                                    onValueChange = { screenState = screenState.copy(find = it) },
                                    findModelChange = { screenState = screenState.copy(findModel = it) },
                                    onFindEvent = { findModel, uvm,svm, find ->

                                    }) { screenState = screenState.copy(isFocus = false) }
                            }
                        }
                        Wrapper {
                            AnimatedVisibility(visible = !screenState.isFocus, enter = expandVertically(tween(200)), exit = shrinkVertically(tween(200))) {
                                SearchScreenFirstScreen(isPadding = false) { screenState = screenState.copy(isFocus = true) }
                            }
                        }
                    }
                    item {
                        FriendScreenItem(data = R.drawable.handle_apply, onClick = {navController.switchTab(MainActivityRouteEnum.APPLY_FRIEND_LIST.route)}) {
                            //TODO:我请求添加的好友
                            BaseText("请求列表")
                        }
                    }
                    item {
                        FriendScreenItem(data = R.drawable.handle_apply, onClick = {}) {
                            //TODO:别人请求添加我的
                            BaseText("新的朋友")
                        }
                    }
                }
            }
        }
        ContactSideBar(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 15.dp)
        ) { selectedIndex ->

        }
    }
}
@SuppressLint(
    "UnusedMaterialScaffoldPaddingParameter"
)
@Composable
fun ApplyFriendScreen(navController:NavHostController,uvm:UserViewmodel = hiltViewModel(),svm:SocialViewModel= hiltViewModel()) {
    @Stable
    data class ApplyFriendScreenState(
        var list: List<FriendApply> = emptyList(),
        var isLoading: Boolean = true
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
                    }
                )
            }
        }
    }

    LaunchedEffect(uid,screenState.list) {
        if (uid != "") {
            if (AppGlobal.isNetworkValid()) {
                screenState.list = svm.getFriendApplyList(uid)
                UserDatabase.getInstance().socialDao().saveFriendApply(screenState.list.map { it.copy(applicantId = uid) })
            } else {
                screenState.list = applyFriendList
            }
        }
    }

    Scaffold(topBar = {
        TopBarWithBack(navController, "请求列表(看看你想添加的人吧)") {
            BaseText(
                "添加好友",
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    navController.switchTab(MainActivityRouteEnum.FIND_USER_IN_FRIEND.route)
                })
        }
    }) { _ ->
        Box {
            LaunchedEffect(Unit) {
                delay(300)
                screenState.isLoading = false
            }
            RefreshLazyColumn {
                items(friendItems) { item ->
                    FriendScreenItem(
                        if (item.friend.avatar != "") item.friend.avatar else R.drawable.default_avatar,
                        tailContent = {
                            BaseText(text = item.statusText, color = PlaceholderColor)
                        },
                        onClick = {//todo:验证好友申请
                            }
                    ) {
                        Column {
                            BaseText(item.friend.nickname, fontWeight = FontWeight.Bold)
                            BaseText(color = PlaceholderColor, text = item.friend.greetMsg, fontSize = 12.sp)
                        }
                    }
                }
            }

            LoadingDialog(screenState.isLoading)
        }
    }

}
data class FriendItem(
    val friend: FriendApply,
    val statusText: String
)

@Composable
fun FriendScreenItem( data: Any, onClick: () -> Unit,tailContent:@Composable ()->Unit={},content: @Composable () -> Unit) {
    BaseScreenItem(preContent = {
        AsyncImage(
            model = ImageRequest.Builder(AppGlobal.getAppContext()).data(data).build(),
            contentDescription = null, modifier = Modifier.size(40.dp), contentScale = ContentScale.Crop
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
    letters: List<String> = ('A'..'Z').map { it.toString() },
    topPadding: Dp = DefaultPaddingTop*2,
    itemCircleSize: Float = 12.5f,
    spacerSize: Float = 1.5f,
    commonColor: Color = Color.Gray.copy(alpha = 0.3f),
    selectColor: Color = IconGreen,
    commonTextColor: Color = Color.Black,
    selectTextColor: Color = Color.White,
    vibratorHelper: VibratorHelper = VibratorHelper(AppGlobal.getAppContext()), // 假设这是一个振动帮助类aa，
    friendListEvent: (Int) -> Unit
) {
    Log.e("SideBarState","SideBarState")
    // 使用Stable接口确保数据类稳定
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

    // 使用derivedStateOf缓存计算结果
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
                                        sideBarState = sideBarState.copy(
                                            selectedIndex = index,
                                            animationState = "showing",
                                            clickCount = sideBarState.clickCount + 1,
                                            touchY = newTouchY
                                        )
                                        friendListEvent(index)
                                    } else if (sideBarState.touchY != newTouchY) {
                                        sideBarState = sideBarState.copy(touchY = newTouchY)
                                    }
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