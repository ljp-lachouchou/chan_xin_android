package com.software.jetpack.compose.chan_xin_android.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.software.jetpack.compose.chan_xin_android.CameraActivity
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserPadding
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserScreenItemDp
import com.software.jetpack.compose.chan_xin_android.entity.PostContent
import com.software.jetpack.compose.chan_xin_android.entity.PostMeta
import com.software.jetpack.compose.chan_xin_android.ext.switchTab
import com.software.jetpack.compose.chan_xin_android.ui.activity.MainActivityRouteEnum
import com.software.jetpack.compose.chan_xin_android.ui.activity.Wrapper
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseBox
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseButton
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseScreenItem
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.base.CanLookImage
import com.software.jetpack.compose.chan_xin_android.ui.base.CustomTextField
import com.software.jetpack.compose.chan_xin_android.ui.base.LazyColumnWithCover
import com.software.jetpack.compose.chan_xin_android.ui.base.LoadingDialog
import com.software.jetpack.compose.chan_xin_android.ui.theme.DividerColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.PlaceholderColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.SurfaceColor
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
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

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FriendCircleScreen(navController:NavHostController,uvm:UserViewmodel = hiltViewModel(),dvm:DynamicViewModel = hiltViewModel()) {
    var filePath by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        filePath = AppGlobal.getFilePath()
    }
    val user by uvm.myUser.collectAsState()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var urisSize by remember { mutableIntStateOf(0) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        uri: Uri? ->
        selectedUri = uri
    }
    val videoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            uri: Uri? ->
        dvm.loadVideoUri(uri)
        dvm.loadPhotoUris(emptyList())
        navController.switchTab(MainActivityRouteEnum.CREATE_POST_SCREEN.route)

    }
    var isLoading by remember { mutableStateOf(false) }

    val mutableLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        urisSize = uris.size
        if (urisSize <= 9) {
            dvm.loadPhotoUris(uris)
            dvm.loadVideoUri(null)
            navController.switchTab(MainActivityRouteEnum.CREATE_POST_SCREEN.route)
        }
    }
    if (urisSize>9) {
        Toast.makeText(AppGlobal.getAppContext(),"图片选择超出限制",Toast.LENGTH_SHORT).show()
    }
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(sheetState = sheetState, sheetContent = { DynamicCreateSheet(scope,sheetState, onClickPhoto={
        mutableLauncher.launch("image/*")
    },onClickVideo = {
        videoLauncher.launch("video/*")
    }) }) {
        Box {
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
                                    }) { selectedUri = null })
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
                                    AppGlobal.saveUserRela(PreferencesFileName.USER_COVER_FILE_PATH,path)
                                    filePath = path
                                    delay(100)
                                    selectedUri = null
                                    isLoading = false
                                }
                            })
                        }
                    }
                }
            }else {
                LazyColumnWithCover(if (filePath=="") R.drawable.default_cover else filePath,user.nickname,user.displayAvatar, onChangeCover = {
                    launcher.launch("image/*")
                }, onEnterFriendInfoDetail = {
                    //todo:进入朋友详情页
                }) {

                }
                TopBarWithBack(navController, action = {
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
                }, color = Color.Transparent, backTint = Color.White)
            }
            LoadingDialog(isLoading)
        }
    }
}
enum class CreatePostEnum(val route:String) {
    MAIN_CREATE("main_create"),
    SELECT_LOCATION("select_location"),
    SELECT_SCOPE("select_scope")
}
@Composable
fun CreatePostScreen(navController: NavHostController,dvm: DynamicViewModel,svm:SocialViewModel) {
    val thisController = rememberNavController()
    var lookModel by remember { mutableIntStateOf(0) }
    var location by remember { mutableStateOf("所在位置") }
    var scopeList by remember { mutableStateOf<List<String>>(emptyList()) }
    NavHost(navController=thisController, startDestination = CreatePostEnum.MAIN_CREATE.route) {
        composable(CreatePostEnum.MAIN_CREATE.route){
            MainCreatePostScreen(navController,thisController,lookModel,location,scopeList,dvm, svm)
        }
        composable(CreatePostEnum.SELECT_LOCATION.route) {
            SelectLocationToCreatePostScreen(thisController) {
                location = it
            }
        }
        composable(CreatePostEnum.SELECT_SCOPE.route) {

        }
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
                    dvm.createPost(user.id, PostContent(text,urls,""), PostMeta(if (location=="所在位置") "" else location,0,
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
            Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
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
                if (videoUri != null) {

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
                CreatePostLookItem(R.drawable.user,lookModel) { thisController.switchTab(CreatePostEnum.SELECT_SCOPE.route) }
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
fun SelectLocationToCreatePostScreen(thisController: NavHostController,onChange:(String)->Unit) {
    var location by remember { mutableStateOf("") }
    CreatePostScaffold("所在位置", onCancel = {thisController.navigateUp()}, onCreate = {onChange(location)}) {

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
fun CreatePostLookItem(icon:Int, lookModel:Int, list:List<String> = emptyList(), onclick: () -> Unit) {
    var title by remember { mutableStateOf("谁可以看") }
    val isCanLook by remember(lookModel) { derivedStateOf { lookModel==3 } }
    val sb = StringBuilder()
    list.forEach {displayName->
        sb.append("$displayName ")
    }
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
            BaseText("-朋友:$sb", color = PlaceholderColor, fontSize = 10.sp, modifier = Modifier.padding(start = DefaultUserPadding))
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
                action = { BaseButton(onClick =  { onCreate()}, modifier = Modifier
                    .height(30.dp)
                    .width(60.dp)) { BaseText("发表", color = Color.White, fontSize = 12.sp) } })
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
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DynamicCreateSheet(scope: CoroutineScope,bottomSheetState: ModalBottomSheetState,onClickPhoto:()->Unit,onClickVideo:()->Unit){
    val context = LocalContext.current
    val intent = Intent(context, CameraActivity::class.java)
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
