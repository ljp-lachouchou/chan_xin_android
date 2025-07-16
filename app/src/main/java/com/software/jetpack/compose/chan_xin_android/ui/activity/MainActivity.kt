package com.software.jetpack.compose.chan_xin_android.ui.activity

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.software.jetpack.compose.chan_xin_android.ui.fragment.AboutChanXinScreen
import com.software.jetpack.compose.chan_xin_android.ui.fragment.SettingScreen
import com.software.jetpack.compose.chan_xin_android.ui.fragment.UserInfoScreen
import com.software.jetpack.compose.chan_xin_android.ui.fragment.UserScreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.NavigationBarColor
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel

enum class TabEnum(val label:String,val imageVector: ImageVector,val route:String) {
    HOME("禅信",Icons.Filled.Home,"chat_fragment"),
    SOCIAL("朋友",Icons.Filled.AccountBox,"social_fragment"),
    FIND("发现",Icons.Filled.Favorite,"find_fragment"),
    USER("我",Icons.Filled.Person,"user_fragment"),
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
@Composable
fun MainActivityScreen(vm: UserViewmodel) {
    val rootNavController = rememberNavController()
    NavHost(navController = rootNavController, startDestination = "parent") {
        composable("parent") { MainScreen(rootNavController,vm) }
        composable("about") { AboutChanXinScreen(rootNavController) }
        composable("userInfo") {UserInfoScreen(navController = rootNavController,vm = vm)}
        composable("setting") {SettingScreen(navController = rootNavController,vm=vm)}
    }

}
@Composable
fun MainScreen(rootController:NavHostController,vm:UserViewmodel) {
    val mainController = rememberNavController()
    Scaffold(bottomBar = { BottomNavBar(mainController) }) { padding->

        //设置路由
        NavHost(navController = mainController, startDestination = TabEnum.HOME.route, modifier = Modifier.padding(padding), enterTransition = { EnterTransition.None }, exitTransition = { ExitTransition.None }) {
            composable(route = TabEnum.HOME.route){
                val activity = LocalContext.current as Activity

                // 拦截返回键，直接退出应用
                BackHandler(enabled = true) {
                    activity.moveTaskToBack(true) // 切换到后台
                }
                Text("禅信") }
            composable(route = TabEnum.SOCIAL.route){
                val activity = LocalContext.current as Activity

                // 拦截返回键，直接退出应用
                BackHandler(enabled = true) {
                    activity.moveTaskToBack(true) // 切换到后台
                }
                Text("社交") }
            composable(route = TabEnum.FIND.route){
                val activity = LocalContext.current as Activity

                // 拦截返回键，直接退出应用
                BackHandler(enabled = true) {
                    activity.moveTaskToBack(true) // 切换到后台
                }
                Text("发现") }
            composable(route = TabEnum.USER.route){ UserScreen(navController = rootController,vm = vm) }
        }
    }
}
@Composable
fun ButtonTabItem(scope:RowScope,tab:TabEnum,selectedTab:Int,onClick:() -> Unit) {
    scope.BottomNavigationItem(
        modifier = Modifier.indication(indication = null, interactionSource = remember { MutableInteractionSource() }),
        selected = (tab.ordinal == selectedTab),
        icon = {
            Icon( modifier = Modifier.indication(indication = null, interactionSource = remember { MutableInteractionSource() }),imageVector = tab.imageVector, contentDescription = null, tint = if (selectedTab==tab.ordinal) IconGreen else Color.Black)
        },
        onClick = onClick,
        label = { Text( modifier = Modifier.indication(indication = null, interactionSource = remember { MutableInteractionSource() }),text = tab.label) },
        selectedContentColor = IconGreen,
        unselectedContentColor = Color.Black
    )
}







