package com.software.jetpack.compose.chan_xin_android.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.software.jetpack.compose.chan_xin_android.CameraActivity
import com.software.jetpack.compose.chan_xin_android.LoginActivity
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultFontSize
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultPaddingBottom
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultPaddingTop
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultRoundCircleShapeDp
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserPadding
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserScreenItemDp
import com.software.jetpack.compose.chan_xin_android.entity.User
import com.software.jetpack.compose.chan_xin_android.ext.getImageBitmapByUrl
import com.software.jetpack.compose.chan_xin_android.ext.switchTab
import com.software.jetpack.compose.chan_xin_android.ui.activity.AppTopBar
import com.software.jetpack.compose.chan_xin_android.ui.activity.AppTopBarBack
import com.software.jetpack.compose.chan_xin_android.ui.activity.IconButton
import com.software.jetpack.compose.chan_xin_android.ui.activity.MainActivityRouteEnum
import com.software.jetpack.compose.chan_xin_android.ui.activity.MainActivityScreen
import com.software.jetpack.compose.chan_xin_android.ui.activity.Wrapper
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseBox
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseButton
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseTextField
import com.software.jetpack.compose.chan_xin_android.ui.base.LoadingDialog
import com.software.jetpack.compose.chan_xin_android.ui.camera.Camera
import com.software.jetpack.compose.chan_xin_android.ui.theme.ChatGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.RightArrowColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.SurfaceColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.TextColor
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.util.Oss
import com.software.jetpack.compose.chan_xin_android.util.PreferencesFileName
import com.software.jetpack.compose.chan_xin_android.util.StringUtil
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import retrofit2.HttpException
import java.lang.Exception

@Composable
fun UserScreen(navController: NavHostController) {
    val activity = LocalContext.current as Activity
    // 拦截返回键，直接退出应用
    BackHandler(enabled = true) {
        activity.moveTaskToBack(true) // 切换到后台
    }
    ParentScreen(navController)
}

@Composable
fun UserInfoScreen(navController: NavHostController,user: User,vm:UserViewmodel = hiltViewModel<UserViewmodel>()) {
    val thisNavController = rememberNavController()
    NavHost(navController = thisNavController, startDestination = UserInfoScreenRouteEnum.PARENT.route) {
        composable(UserInfoScreenRouteEnum.PARENT.route) {InfoMainScreen(navController,user,thisNavController)}
        composable(UserInfoScreenRouteEnum.AVATAR.route) {UpdateAvatarScreen(thisNavController,user,vm)}
        composable(UserInfoScreenRouteEnum.NICKNAME.route) { UpdateNicknameScreen(thisNavController,user,vm) }
        composable(UserInfoScreenRouteEnum.SEX.route) { UpdateSexScreen(thisNavController,user,vm) }
    }
}
enum class UserInfoScreenRouteEnum(val route:String) {
    PARENT("parent"),
    AVATAR("avatar"),
    NICKNAME("nickname"),
    SEX("sex")
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UpdateAvatarScreen(navController: NavHostController,user:User,vm:UserViewmodel) {
    val sheetState = androidx.compose.material.rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    var selectedUri:Uri? by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {uri: Uri? ->
        selectedUri = uri
    }
    var isLoading  by remember { mutableStateOf(false) }
    ModalBottomSheetLayout(sheetContent = {AvatarMoreSheet(launcher,scope,bottomSheetState=sheetState, user = user)},sheetState = sheetState, sheetShape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp)) {
        Scaffold(topBar = { AppTopBar(title = "头像", actions = {
            IconButton(onClick = {
                scope.launch { sheetState.show() }
            }) {
                Icon(Icons.Filled.Menu, tint = Color.White, contentDescription = null)
            }
        },navigationIcon = {
            IconButton(onClick = {navController.navigateUp()}) {
                Icon(Icons.Filled.Close, tint = Color.White, contentDescription = null)
            }
        }, backgroundColor = Color.Black,titleColor = Color.White) }, content = {padding->
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(padding), contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = ImageRequest.Builder(AppGlobal.getAppContext())
                        .data(if (selectedUri != null) selectedUri else {if (user.avatar != "") user.avatar else R.drawable.default_avatar})
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
                if (selectedUri != null) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .padding(
                            DefaultPaddingBottom * 3
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ){
                        BaseText(text = "取消", color = Color.White, modifier = Modifier
                            .clickable(indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                selectedUri = null
                            })
                        BaseText(text="更换头像", color = Color.White, modifier = Modifier
                            .clickable(indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                scope.launch(Dispatchers.IO){
                                    isLoading = true
                                    val avatar = Oss.uploadFile(System.currentTimeMillis().toString()+"."+ StringUtil.getFileExtensionFromUri(AppGlobal.getAppContext(),selectedUri),selectedUri)
                                    updateUser(vm = vm, avatar = avatar)
                                    delay(1000)
                                    isLoading = false
                                    selectedUri = null
                                }
                            })
                    }
                }
                LoadingDialog(isLoading)

            }
        })
    }

}



@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AvatarMoreSheet(
    launcher: ManagedActivityResultLauncher<String, Uri?>,
    scope: CoroutineScope,
    canSaveImage:Boolean = true,
    bottomSheetState: ModalBottomSheetState,
    user: User
) {

    val context = LocalContext.current
    val intent = Intent(context,CameraActivity::class.java)

    scope.apply {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            ) {
            TextButton(onClick = {
                launch { bottomSheetState.hide() }
                context.startActivity(intent)
            }, shape = RectangleShape) {
                BaseText("拍照", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
            TextButton(onClick = {
                launch { bottomSheetState.hide() }
                launcher.launch("image/*")
            }, shape = RectangleShape) {
                BaseText("从手机相册选择", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
            if (user.avatar != "" && canSaveImage) {
                TextButton(onClick = {
                    launch {
                        bottomSheetState.hide()
                        saveImage(user.avatar)
                    }
                }, shape = RectangleShape) {
                    BaseText("保存图片", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                }
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

suspend fun saveImage(avatar: String) {
    val bitmap = AppGlobal.getAppContext().getImageBitmapByUrl(avatar)
    if (bitmap != null) {
        try {
            Camera.saveImageToGallery(AppGlobal.getAppContext(),bitmap)
            withContext(Dispatchers.Main) {
                Toast.makeText(AppGlobal.getAppContext(),"图片保存到系统相册",Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(AppGlobal.getAppContext(),"保存图片失败",Toast.LENGTH_SHORT).show()
            }
        }
    }else {
        withContext(Dispatchers.Main) {
            Toast.makeText(AppGlobal.getAppContext(),"保存图片失败",Toast.LENGTH_SHORT).show()
        }
    }
}

//修改昵称界面
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UpdateNicknameScreen(thisNavController: NavHostController,user:User,vm: UserViewmodel) {
    var nickname by remember { mutableStateOf(user.nickname) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val topBar:@Composable ()->Unit = {
        AppTopBar(title = "设置昵称", navigationIcon = {
            BaseText("取消", modifier = Modifier.clickable (interactionSource = remember { MutableInteractionSource() }, indication = null){
                thisNavController.navigateUp()
            })
        }, actions = {
            BaseButton(onClick = {scope.launch {
                updateUser(vm=vm, nickname = nickname)
                thisNavController.navigateUp()
            }}, enabled = (nickname != "" && nickname != user.nickname)) {
                BaseText(text = "完成", color = Color.White)
            }
        })
    }
    Scaffold(topBar = topBar, content = {padding ->
        Box( modifier = Modifier
            .fillMaxSize()
            .background(SurfaceColor)){
            BaseTextField(value = nickname, onValueChange = {
                nickname = it
                }, placeholder = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .background(color = Color.White)
                    .focusRequester(focusRequester),
                trailingIcon = {
                if (nickname != "") Icon(Icons.Filled.Close, tint = IconGreen, contentDescription = null, modifier = Modifier.clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { nickname = "" })
            }, unfocusedContainerColor = Color.White, focusedContainerColor = Color.White, keyboardActions = KeyboardActions(onGo = {
                Log.e("keyboardActions","keyboardActions")
                scope.launch {
                    updateUser(vm=vm, nickname = nickname)
                    thisNavController.navigateUp()
                }}), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go, keyboardType = KeyboardType.Text),singleLine=true //必须设置,防止多行文本拦截事件
            )
        }
    })
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
        keyboardController?.show()
    }
}
//修改性别界面
@Composable
fun UpdateSexScreen(thisNavController: NavHostController,user:User,vm: UserViewmodel) {
    var sex by remember { mutableIntStateOf(user.sex.toInt()) }
    val scope = rememberCoroutineScope()
    val topBar:@Composable ()->Unit = {
        AppTopBar(title = "设置性别", navigationIcon = {
            BaseText("取消", modifier = Modifier.clickable (interactionSource = remember { MutableInteractionSource() }, indication = null){
                thisNavController.navigateUp()
            })
        }, actions = {
            BaseButton(onClick = {scope.launch {
                updateUser(vm=vm,sex = sex)
                thisNavController.navigateUp()
            }}) {
                BaseText(text = "完成", color = Color.White)
            }
        })
    }
    Scaffold(topBar = topBar, content = {padding ->
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(padding)) {
            Wrapper { SexSingleSelect("未知",sex == 0) {sex = 0} }
            Wrapper { SexSingleSelect("男",sex == 1) {sex = 1} }
            Wrapper { SexSingleSelect("女",sex == 2) {sex = 2} }
        }
    })
}
suspend fun updateUser(vm:UserViewmodel,sex:Int?=null,nickname:String?=null,avatar:String? = null) {
    try {
        vm.updateUser(sex = sex,nickname=nickname,avatar=avatar)
    }catch (e:HttpException) {
        withContext(Dispatchers.Main) {
            Toast.makeText(AppGlobal.getAppContext(),"更新失败",Toast.LENGTH_SHORT).show()
        }
    }catch (e:Exception) {
        withContext(Dispatchers.Main) {
            Toast.makeText(AppGlobal.getAppContext(),"网络可能出了些问题",Toast.LENGTH_SHORT).show()
        }
    }
}
@Composable
fun SexSingleSelect(title:String="",selected:Boolean,onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(DefaultUserScreenItemDp)
        .padding(DefaultUserPadding)
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }) { onClick() }) {
        BaseText(text = title, fontSize = DefaultFontSize)
        Spacer(modifier = Modifier.weight(1f))
        if (selected) Icon(imageVector= Icons.Filled.Check, contentDescription = null, tint = IconGreen)
    }
}
@Composable
fun InfoMainScreen(navController:NavHostController,user:User,thisNavController:NavHostController) {
    var sex by remember { mutableStateOf("") }
    sex = when(user.sex.toInt()) {
        0 -> "未知"
        1-> "男"
        2->"女"
        else -> ""
    }
    Scaffold(topBar = { AppTopBarBack(title = "个人资料", navController = navController) }, content = {padding->
        BoxWithConstraints {
            val width = constraints.maxWidth.dp
            val height = constraints.maxHeight.dp
            BaseBox(modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Gray.copy(0.3f))) {
                Column(modifier = Modifier.background(Color.White)){
                    UserInfoScreenItem(label = "头像", onClick = {thisNavController.switchTab("avatar")}) {
                        AsyncImage(model = ImageRequest.Builder(context = LocalContext.current).data(if (user.avatar == "") R.drawable.default_avatar else user.avatar).build(),contentDescription = null, modifier = Modifier
                            .size(30.dp)
                            .clip(
                                RoundedCornerShape(DefaultRoundCircleShapeDp / 2)
                            ), contentScale = ContentScale.Crop)
                    }
                    UserInfoScreenItem(label = "昵称", onClick = {
                        thisNavController.switchTab("nickname")
                    }) {
                        BaseText(text = user.nickname)
                    }
                    UserInfoScreenItem(label = "性别", onClick = {
                        thisNavController.switchTab("sex")
                    }) {
                        BaseText(text = sex)
                    }
                    UserInfoScreenItem(label = "手机号", onClick = {
                        Toast.makeText(AppGlobal.getAppContext(),"手机号:${user.phone}",Toast.LENGTH_SHORT).show()
                    }) {
                        Wrapper {
                            Text(text = buildAnnotatedString {
                                val pre = user.phone.substring(0 .. 2)
                                val mid = String((3..<user.phone.length - 2).map { '*' }.toCharArray())
                                val suf = user.phone.substring(user.phone.length - 2..<user.phone.length)
                                append(pre)
                                append(mid)
                                append(suf)
                            }, color = TextColor)
                        }
                    }
                    UserInfoScreenItem(label = "禅信号", onClick = {}) {
                        BaseText(user.id)
                    }
                }

            }
        }
    })
}
@Composable
fun AboutChanXinScreen(navController: NavHostController) {
    Scaffold(topBar = {AppTopBarBack(title = "关于我们", navController=navController)}, content = {padding->
        Column (verticalArrangement = Arrangement.Center){
            Spacer(Modifier.height(DefaultPaddingTop*2))
            BaseText("禅信:一款android端仿微信开源项目app", modifier = Modifier
                .fillMaxSize()
                .padding(padding), textAlign = TextAlign.Center)
        }
    })
}

//设置界面
@Composable
fun SettingScreen(navController:NavHostController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Scaffold(topBar = { AppTopBar(title = "设置", navController = navController) }, content = {padding->
        Column(modifier = Modifier
            .fillMaxSize()
            .background(SurfaceColor)
            .padding(padding)) {
            Spacer(Modifier
                .height(40.dp)
                .fillMaxWidth()
                .background(SurfaceColor))
            UserInfoScreenItem(label = "个人资料", onClick = {navController.switchTab("userInfo")})
            Spacer(Modifier
                .height(20.dp)
                .fillMaxWidth()
                .background(SurfaceColor))
            UserInfoScreenItem(label = "退出登录", onClick = {
                scope.launch { AppGlobal.saveUserRela(PreferencesFileName.USER_TOKEN,"") }
                scope.launch { AppGlobal.saveUserRela(PreferencesFileName.USER_TOKEN_EXP,0) }
                val intent = Intent(context,LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                (context as Activity).finish()
            })
        }

    })
}
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ParentScreen(navController: NavHostController, vm: UserViewmodel= hiltViewModel()) {
    val user by vm.myUser.collectAsState()
    Log.e("UserScreen",vm.myUser.value.toString())
    val sexPainter = when(user.sex.toInt()) {
        0 -> R.drawable.unknow
        1-> R.drawable.man
        2->R.drawable.woman
        else -> R.drawable.unknow
    }
    Surface(modifier = Modifier.fillMaxSize(), color = SurfaceColor) {
        BaseBox(modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(top = DefaultPaddingTop)) {
                UserSimpleItem(user, imageClick = {navController.switchTab(MainActivityRouteEnum.USER_INFO_IN_USER.route)}, sexPainter = sexPainter){navController.switchTab(MainActivityRouteEnum.USER_INFO_IN_USER.route)}
                Spacer(modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
                    .background(SurfaceColor))
                UserScreenItem(imageVector = painterResource(R.drawable.friend_circle), label = "朋友圈") {
                    //todo:朋友圈
                }
                Spacer(modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
                    .background(SurfaceColor))
                UserScreenItem(imageVector = Icons.Outlined.Settings, label = "设置", tintColor = Color.Blue) {
                    navController.switchTab(MainActivityRouteEnum.SETTING_IN_USER.route)
                }
                Spacer(modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
                    .background(SurfaceColor))
                UserScreenItem(imageVector = Icons.Outlined.MailOutline, label = "关于我们", tintColor = Color.Blue) {
                    navController.switchTab(MainActivityRouteEnum.ABOUT_IN_USER.route)
                }
            }
        }
    }
}

@Composable
fun UserSimpleItem(user:User,sexPainter:Int,imageClick:(()->Unit)?=null,heightDp:Dp = DefaultUserScreenItemDp * 3,color: Color = Color.White,onClick: () -> Unit = {}) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(heightDp)
        .background(color)
        .padding(DefaultUserPadding)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) { onClick() }) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(if (user.avatar == "") R.drawable.default_avatar else user.avatar).build(),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(
                    RoundedCornerShape(DefaultRoundCircleShapeDp)
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) { if (imageClick != null) imageClick() },
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.width(DefaultUserPadding))
        Column {
            BaseText(text = user.nickname, style = TextStyle(fontWeight = FontWeight.Bold), fontSize = 24.sp)
            Row {
                BaseText("性别: ")
                Image(painter = painterResource(sexPainter), modifier = Modifier.size(25.dp), contentDescription = null, contentScale = ContentScale.Crop)
            }
        }
    }
}

@Composable
fun UserScreenItem(imageVector: ImageVector, label:String,tintColor:Color = IconGreen,onClick:()->Unit) {
    Box(modifier = Modifier.clickable {
        onClick()
    }) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(DefaultUserScreenItemDp)
            .padding(DefaultUserPadding)) {
            Icon(imageVector,contentDescription = null, tint = tintColor)
            Spacer(modifier = Modifier.width(DefaultUserPadding))
            BaseText(text = label, fontSize = DefaultFontSize)
            Spacer(modifier = Modifier.weight(1f))
            Icon(imageVector= Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = RightArrowColor)
        }
    }
}
@Composable
fun UserScreenItem(imageVector: Painter, label:String,onClick:()->Unit) {
    LabelScreenItem(imageVector,label) { onClick() }
}
@Composable
fun LabelScreenItem(imageVector: Painter, label:String,defaultSize:Dp = 24.dp,onClick:()->Unit) {
    Box(modifier = Modifier.fillMaxWidth().background(Color.White).clickable {
        onClick()
    }) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(DefaultUserScreenItemDp)
            .padding(DefaultUserPadding)) {
            Image(imageVector,contentDescription = null, modifier = Modifier.size(defaultSize))
            Spacer(modifier = Modifier.width(DefaultUserPadding))
            BaseText(text = label, fontSize = DefaultFontSize)
            Spacer(modifier = Modifier.weight(1f))
            Icon(imageVector= Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = RightArrowColor)
        }
    }
}
@Composable
fun UserInfoScreenItem(label:String,onClick:()->Unit,heightDp: Dp  = DefaultUserScreenItemDp,content:@Composable ()->Unit = {}) {
    Box(modifier = Modifier
        .background(Color.White)
        .clickable {
            onClick()
        }) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(heightDp), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(DefaultUserPadding))
            BaseText(text = label, fontSize = DefaultFontSize)
            Spacer(modifier = Modifier.weight(1f))
            content()
            Spacer(modifier = Modifier.width(DefaultUserPadding))
            Icon(imageVector= Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = RightArrowColor)
        }
    }
}
