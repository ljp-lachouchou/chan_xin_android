package com.software.jetpack.compose.chan_xin_android.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColor
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserPadding
import com.software.jetpack.compose.chan_xin_android.ext.switchTab
import com.software.jetpack.compose.chan_xin_android.ext.toComposeColor
import com.software.jetpack.compose.chan_xin_android.ui.activity.LoginScreen
import com.software.jetpack.compose.chan_xin_android.ui.activity.MainActivityRouteEnum
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseBox
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.base.GeneratePaletteFromImage
import com.software.jetpack.compose.chan_xin_android.ui.base.LazyColumnWithCover
import com.software.jetpack.compose.chan_xin_android.ui.theme.DividerColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.SurfaceColor
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FriendCircleScreen(navController:NavHostController,uvm:UserViewmodel = hiltViewModel()) {
    val user by uvm.myUser.collectAsState()
    Box {
        LazyColumnWithCover(user.displayAvatar,user.nickname,user.displayAvatar) {

        }
        TopBarWithBack(navController, action = {
            Icon(painterResource(R.drawable.photo),contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        }, color = Color.Transparent, backTint = Color.White)
    }
}