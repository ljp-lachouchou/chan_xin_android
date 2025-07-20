package com.software.jetpack.compose.chan_xin_android.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.cache.database.UserDatabase
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.fragment.AboutChanXinScreen
import com.software.jetpack.compose.chan_xin_android.ui.fragment.ApplyFriendScreen
import com.software.jetpack.compose.chan_xin_android.ui.fragment.FriendScreen
import com.software.jetpack.compose.chan_xin_android.ui.fragment.SearchFriendScreen
import com.software.jetpack.compose.chan_xin_android.ui.fragment.SettingScreen
import com.software.jetpack.compose.chan_xin_android.ui.fragment.UserInfoInFriendBySearchScreen
import com.software.jetpack.compose.chan_xin_android.ui.fragment.UserInfoScreen
import com.software.jetpack.compose.chan_xin_android.ui.fragment.UserScreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.NavigationBarColor
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.vm.SocialViewModel
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import kotlinx.coroutines.launch

enum class TabEnum(val label:String,val resId:Int,val route:String) {
    HOME("禅信", R.drawable.chan_xin,"chat_fragment"),
    SOCIAL("朋友",R.drawable.friend,"social_fragment"),
    FIND("发现",R.drawable.find,"find_fragment"),
    USER("我",R.drawable.user,"user_fragment"),
}
val FragmentModifier = Modifier.padding(bottom = 60.dp)
@Composable
fun BottomNavBar(navController: NavHostController) {
    var selectedTab by remember {
        mutableIntStateOf(0)
    }
    selectedTab = when (navController.currentDestination?.route) {
        TabEnum.HOME.route -> 0
        TabEnum.FIND.route -> 2
        TabEnum.SOCIAL.route -> 1
        TabEnum.USER.route -> 3
        else -> 0
    }
    val activity = LocalContext.current as Activity
    BackHandler {
        activity.moveTaskToBack(true)
    }
    BottomNavigation(
        backgroundColor = NavigationBarColor,
        elevation = 0.dp
    ) {
        val tabs = listOf(TabEnum.HOME,TabEnum.SOCIAL,TabEnum.FIND,TabEnum.USER)
        tabs.forEach {
            tab->
            ButtonTabItem(this,tab,selectedTab) {
                if (selectedTab == tab.ordinal) return@ButtonTabItem
                selectedTab = tab.ordinal
                //导航
                navController.navigate(tab.route)

            }
        }
    }

}
enum class MainActivityRouteEnum(val route: String) {
    PARENT("parent"),
    ABOUT_IN_USER("about"),
    USER_INFO_IN_USER("userInfo"),
    SETTING_IN_USER("setting"),
    FIND_USER_IN_FRIEND("find_user"),
    USER_INFO_IN_FRIEND_BY_SEARCH("userInfoInFriendBySearch"),
    APPLY_FRIEND_LIST("apply_friend_list")
}
@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainActivityScreen() {
    val rootNavController = rememberNavController()
    val vm :UserViewmodel = hiltViewModel()
    val svm:SocialViewModel = hiltViewModel()
    val user by vm.myUser.collectAsState()
    Log.e("users",user.toString())
    rememberCoroutineScope().launch {
        Log.e("countc",UserDatabase.getInstance().socialDao().getFriendApplyCount().toString())
    }
    NavHost(navController = rootNavController, startDestination = MainActivityRouteEnum.PARENT.route) {
        composable(MainActivityRouteEnum.PARENT.route) { MainScreen(rootNavController) }
        composable(MainActivityRouteEnum.ABOUT_IN_USER.route) { AboutChanXinScreen(rootNavController) }
        composable(MainActivityRouteEnum.USER_INFO_IN_USER.route) {UserInfoScreen(navController = rootNavController,user=user)}
        composable(MainActivityRouteEnum.SETTING_IN_USER.route) {SettingScreen(navController = rootNavController)}
        composable(MainActivityRouteEnum.FIND_USER_IN_FRIEND.route) { SearchFriendScreen(rootNavController,svm=svm) }
        composable(MainActivityRouteEnum.USER_INFO_IN_FRIEND_BY_SEARCH.route) { UserInfoInFriendBySearchScreen(rootNavController,svm=svm) }
        composable(MainActivityRouteEnum.APPLY_FRIEND_LIST.route) { ApplyFriendScreen(rootNavController,svm=svm) }
    }

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(rootController:NavHostController) {
    val mainController = rememberNavController()
    val screenCache = remember { mutableMapOf<String,@Composable () -> Unit>() }
    Scaffold(bottomBar = { BottomNavBar(mainController) }) { padding->
        //设置路由
        NavHost(navController = mainController,
            startDestination = TabEnum.HOME.route,
            modifier = Modifier.padding(padding),
            enterTransition = { fadeIn(tween(1000)) },
            exitTransition = { fadeOut(tween(1000)) }) {
            composable(route = TabEnum.HOME.route) {
                val activity = LocalContext.current as Activity
                // 拦截返回键，直接退出应用
                BackHandler(enabled = true) {
                    activity.moveTaskToBack(true) // 切换到后台
                }
                Text("禅信")
            }
            composable(route = TabEnum.SOCIAL.route) {
                val friendScreen = screenCache.getOrPut(TabEnum.SOCIAL.route) {
                    {
                        FriendScreen(
                            navController = rootController
                        )
                    }
                }
                friendScreen()
            }
            composable(route = TabEnum.FIND.route) {
                val activity = LocalContext.current as Activity

                // 拦截返回键，直接退出应用
                BackHandler(enabled = true) {
                    activity.moveTaskToBack(true) // 切换到后台
                }
                Text("发现")
            }
            composable(route = TabEnum.USER.route) {
                val userScreen = screenCache.getOrPut(TabEnum.USER.route) {
                    {
                        UserScreen(
                            navController = rootController
                        )
                    }
                }
                userScreen()
            }
        }
    }
}
@Composable
fun ButtonTabItem(scope:RowScope,tab:TabEnum,selectedTab:Int,onClick:() -> Unit) {
    scope.BottomNavigationItem(
        modifier = Modifier.indication(indication = null, interactionSource = remember { MutableInteractionSource() }),
        selected = (tab.ordinal == selectedTab),
        icon = {
            Icon( modifier = Modifier
                .size(20.dp)
                .indication(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }), painter = painterResource(tab.resId), contentDescription = null, tint = if (selectedTab==tab.ordinal) IconGreen else Color.Black)
        },
        onClick = onClick,
        label = { Text( modifier = Modifier.indication(indication = null, interactionSource = remember { MutableInteractionSource() }),text = tab.label) },
        selectedContentColor = IconGreen,
        unselectedContentColor = Color.Black
    )
}







