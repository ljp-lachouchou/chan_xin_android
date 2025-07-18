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
import androidx.compose.animation.expandHorizontally
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultPaddingTop
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserPadding
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserScreenItemDp
import com.software.jetpack.compose.chan_xin_android.defaultValue.defaultPlaceholderText
import com.software.jetpack.compose.chan_xin_android.entity.User
import com.software.jetpack.compose.chan_xin_android.ext.switchTab
import com.software.jetpack.compose.chan_xin_android.ui.activity.AppTopBar
import com.software.jetpack.compose.chan_xin_android.ui.activity.MainActivityRouteEnum
import com.software.jetpack.compose.chan_xin_android.ui.activity.Wrapper
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseBox
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseScreenItem
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.base.LoadingDialog
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.PlaceholderColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.SurfaceColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.TextColor
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.util.StringUtil
import com.software.jetpack.compose.chan_xin_android.util.VibratorHelper
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FriendScreen(navController:NavHostController,uvm:UserViewmodel) {
    val activity = LocalContext.current as Activity
    // 拦截返回键，直接退出应用
    BackHandler(enabled = true) {
        activity.moveTaskToBack(true) // 切换到后台
    }
    val thisNavController = rememberNavController()
    NavHost(navController = thisNavController, startDestination = FriendScreenRouteEnum.PARENT.route) {
        composable(FriendScreenRouteEnum.PARENT.route) {MainFriendScreen(navController,thisNavController, uvm = uvm)}
    }

}
enum class FriendScreenRouteEnum(val route:String) {
    PARENT("parent")
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchFriendScreen(navController: NavHostController,uvm:UserViewmodel) {
    var isFocus by remember { mutableStateOf(false) }
    var findModel by remember { mutableIntStateOf(0) } //0:手机号;1:禅信号;2:昵称
    var find by remember { mutableStateOf("") }
    Scaffold(topBar = {
        AnimatedVisibility(visible = !isFocus, enter = expandVertically(tween(200)), exit = shrinkVertically(tween(200))) {
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
        AnimatedVisibility(visible = isFocus, enter = expandVertically(tween(200)), exit = shrinkVertically(tween(200))) {
            uvm.pagedUsers = null
            SearchFriendFieldScreen(navController = navController,find=find, onValueChange = {s:String->find=s}, isFocus = isFocus, uvm = uvm, findModel = findModel, findModelChange = {findModel=it}, onFindEvent = {findModel,uvm,find->
                if (uvm is UserViewmodel) {
                    findUser(findModel,uvm,find)
                }
            }){isFocus = false}
        }
        AnimatedVisibility(visible = !isFocus, enter = expandVertically(tween(200)), exit = shrinkVertically(tween(200))) {
            SearchScreenFirstScreen { isFocus = true }
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
@Composable
fun SearchFriendFieldScreen(navController:NavHostController,find:String,findModel:Int,onValueChange:(String)->Unit,findModelChange:(k:Int)->Unit,isFocus:Boolean,uvm:UserViewmodel,onFindEvent:suspend (findModel:Int,uvm:ViewModel,find:String)->Unit,onClick: () -> Unit) {
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
                                onFindEvent(findModel,uvm,find)
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
                        FriendScreenItem(
                            if (users?.get(i)?.avatar != "") users?.get(i)?.avatar
                                ?: R.drawable.default_avatar else R.drawable.default_avatar,
                            onClick = {navController.switchTab(MainActivityRouteEnum.USER_INFO_IN_FRIEND_BY_SEARCH.route)}) { BaseText(users?.get(i)?.nickname ?: "") }
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
fun MainFriendScreen(navController: NavHostController,thisNavController:NavHostController,uvm: UserViewmodel) {
    var isFocus by remember { mutableStateOf(false) }
    var findModel by remember { mutableIntStateOf(0) } //0:手机号;1:禅信号;2:昵称
    var find by remember { mutableStateOf("") }
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
                        AnimatedVisibility(visible = isFocus, enter = expandVertically(tween(200)), exit = shrinkVertically(tween(200))) {
                            uvm.pagedUsers = null
                            SearchFriendFieldScreen(navController=navController,
                                isFocus = isFocus,
                                uvm = uvm,
                                findModel = findModel,
                                find = find,
                                onValueChange = { find = it },
                                findModelChange = { findModel = it },
                                onFindEvent = { findModel, uvm, find ->

                                }) { isFocus = false }
                        }
                        AnimatedVisibility(visible = !isFocus, enter = expandVertically(tween(200)), exit = shrinkVertically(tween(200))) {
                            SearchScreenFirstScreen(isPadding = false) { isFocus = true }
                        }
                    }
                    item {
                        FriendScreenItem(data = R.drawable.new_friend, onClick = {}) {
                            BaseText("新的朋友")
                        }
                    }
                }
            }
        }
        ContactSideBar(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 15.dp),
            letters = (1..26).map { ('a' + it - 1).toString() }.toList()
        ) { selectedIndex ->

        }
    }
}

@Composable
fun FriendScreenItem( data: Any, onClick: () -> Unit,content: @Composable () -> Unit) {
    BaseScreenItem(preContent = {
        AsyncImage(
            model = ImageRequest.Builder(AppGlobal.getAppContext()).data(data).build(),
            contentDescription = null, modifier = Modifier.size(40.dp)
        )
    }, onClick = onClick) {
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
@SuppressLint("UnusedTransitionTargetStateParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContactSideBar(
    topPadding:Dp = DefaultPaddingTop*2f,
    spacerSize:Float = 1.5f,
    itemCircleSize:Float=  12.5f,
    modifier: Modifier = Modifier,
    selectColor: Color = IconGreen,
    commonColor: Color = Color.Gray.copy(0.6f),
    selectTextColor: Color = Color.White,
    letters: List<String>,
    vibratorHelper: VibratorHelper = remember { VibratorHelper(AppGlobal.getAppContext()) },
    friendListEvent:(selectIndex:Int)->Unit
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }
    val componentPositions = remember { mutableMapOf<Int, Offset>() }
    var animationState by remember { mutableStateOf("idle") } // idle, showing, hiding
    var clickCount by remember { mutableIntStateOf(0) }
    val transition = updateTransition(clickCount, label = "animationTransition")
    var sideHeight by remember { mutableFloatStateOf(0f) }
    var touchY by remember { mutableFloatStateOf(0f) }
    val scale by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 300, easing = FastOutSlowInEasing)
        }, label = "scale"
    ) {
        if (animationState == "showing") 1f else 0f
    }
    val alpha by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 200)
        }, label = "alpha"
    ) {
        if (animationState == "showing") 1f else 0f
    }
    LaunchedEffect(clickCount) {
        delay(300)
        if (animationState == "showing") {
            animationState = "hiding"
        }
    }
//    Column(verticalArrangement = Arrangement.SpaceBetween,modifier=modifier) {
//        Spacer(modifier=Modifier.height(DefaultPaddingTop*1.5f))
    Box(modifier=modifier.padding(top = topPadding)) {
        Column(modifier = Modifier
            .onGloballyPositioned { coordinates ->
                sideHeight = coordinates.size.height.toFloat()
                Log.e("sideHeight", "$sideHeight")
            }
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                        touchY = event.y
                        if (sideHeight > 0) {
                            val letterHeight = sideHeight / letters.size
                            val index = ((touchY / letterHeight).coerceIn(
                                0f, (letters.size - 1).toFloat()
                            )).roundToInt()
                            if (selectedIndex != index) {
                                selectedIndex = index;vibratorHelper.vibrate()
                            }
                            animationState = "showing" // 立即显示
                            clickCount++ // 触发动画重置
                        }
                        friendListEvent(selectedIndex)
                        true
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        true
                    }

                    else -> false
                }
            }) {
            for (i in letters.indices) {
                val letter = letters[i]
                Surface(color = if (selectedIndex == i) selectColor else commonColor,
                    modifier = Modifier
                        .size(itemCircleSize.dp)
                        .onGloballyPositioned { coordinates ->
                            componentPositions[i] = coordinates.positionInRoot()
                        },
                    shape = CircleShape
                ) {
                    BaseText(
                        text = letter,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        color = if (selectedIndex == i) selectTextColor else TextColor
                    )
                }
                Spacer(modifier = Modifier.height(spacerSize.dp))
            }
        }

//        }
    }
    if (componentPositions[selectedIndex] != null && animationState != "idle") {
        val globalPosition = componentPositions[selectedIndex]!!
        val baseY = with(LocalDensity.current) { (globalPosition.y).toDp() }
        val finalY = baseY + (-95).dp
        Surface(
            color = commonColor,
            modifier = modifier
                .size(40.dp * scale)
                .offset((-itemCircleSize-1f).dp,topPadding+((selectedIndex - 1)*(spacerSize+itemCircleSize)).dp)
                .alpha(alpha),
            shape = CircleShape
        ) {
            BaseText(
                text = letters[selectedIndex],
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                color = selectTextColor
            )
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