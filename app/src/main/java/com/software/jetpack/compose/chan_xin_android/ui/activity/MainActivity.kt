package com.software.jetpack.compose.chan_xin_android.ui.activity

import android.content.Context
import androidx.annotation.FloatRange
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ContentAlpha
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.LocalContentAlpha
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.LocalContentColor
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Scaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirst
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.fragment
import com.software.jetpack.compose.chan_xin_android.ext.switchTab
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseBox
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.NavigationBarColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.SurfaceColor
import kotlin.math.max
import kotlin.math.roundToInt

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
                navController.switchTab(tab.route)
            }
        }
    }

}
@Composable
fun MainActivityScreen() {
    val navController = rememberNavController()//控制器
   Scaffold(bottomBar = { BottomNavBar(navController) }) { padding->
       //设置路由
       NavHost(navController = navController, startDestination = TabEnum.HOME.route, modifier = Modifier.padding(padding)) {
           composable(route = TabEnum.HOME.route){ Text("禅信") }
           composable(route = TabEnum.SOCIAL.route){ Text("社交") }
           composable(route = TabEnum.FIND.route){ Text("发现") }
           composable(route = TabEnum.USER.route){ Text("我") }
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




