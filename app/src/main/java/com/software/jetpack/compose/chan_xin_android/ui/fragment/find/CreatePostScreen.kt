package com.software.jetpack.compose.chan_xin_android.ui.fragment.find

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
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
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserPadding
import com.software.jetpack.compose.chan_xin_android.entity.PostContent
import com.software.jetpack.compose.chan_xin_android.entity.PostMeta
import com.software.jetpack.compose.chan_xin_android.ext.switchTab
import com.software.jetpack.compose.chan_xin_android.ui.activity.MainActivityRouteEnum
import com.software.jetpack.compose.chan_xin_android.ui.activity.Wrapper
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseButton
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseScreenItem
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.base.CanLookImage
import com.software.jetpack.compose.chan_xin_android.ui.base.CustomTextField
import com.software.jetpack.compose.chan_xin_android.ui.base.LoadingDialog
import com.software.jetpack.compose.chan_xin_android.ui.base.PlayVideo
import com.software.jetpack.compose.chan_xin_android.ui.base.extraVideoFrame
import com.software.jetpack.compose.chan_xin_android.ui.fragment.friend.MyTopBar
import com.software.jetpack.compose.chan_xin_android.ui.fragment.user.SexSingleSelect
import com.software.jetpack.compose.chan_xin_android.ui.theme.DividerColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.PlaceholderColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.SurfaceColor
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.util.Location
import com.software.jetpack.compose.chan_xin_android.util.Oss
import com.software.jetpack.compose.chan_xin_android.util.StringUtil
import com.software.jetpack.compose.chan_xin_android.vm.DynamicViewModel
import com.software.jetpack.compose.chan_xin_android.vm.SocialViewModel
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MainCreatePostScreen(navController: NavHostController, thisController: NavHostController, lookModel: Int, location:String, scopeList:List<String>, dvm: DynamicViewModel, svm: SocialViewModel, uvm: UserViewmodel = hiltViewModel()) {
    val user by uvm.myUser.collectAsState()
    val videoUri by dvm.videoUri.collectAsState()
    val photoUris by dvm.photoUris.collectAsState()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
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
        videoBitmap = extraVideoFrame(AppGlobal.getAppContext(),videoUri?: Uri.Builder().build(),0L)
    }
    var isPlay by remember { mutableStateOf(false) }
    Box {
        CreatePostScaffold("发表动态",selectedUri,lookImage&&videoUri==null, onChange = {lookImage = !lookImage;Log.e("lookImage","$lookImage")}, onCancel = {navController.navigateUp()}, onCreate = {
            if (!isLimited) {
                scope.launch(Dispatchers.IO) {
                    isLoading  =true
                    val urls = if (videoUri == null) {
                        photoUris.map {
                            Oss.uploadFile("${System.currentTimeMillis()}.${
                                StringUtil.getFileExtensionFromUri(
                                    AppGlobal.getAppContext(),it)}", uri = it)
                        }
                    }else {
                        listOf(videoUri).map { Oss.uploadFile("${System.currentTimeMillis()}.${
                            StringUtil.getFileExtensionFromUri(
                                AppGlobal.getAppContext(),it)}", uri = it) }
                    }
                    dvm.createPost(user.id, PostContent(text,urls,""), PostMeta(if (location=="所在位置") "" else location,if (lookModel>1) 2 else lookModel,
                        scopeList
                    )
                    )
                    isLoading = false
                    delay(100)
                    withContext(Dispatchers.Main) {
                        navController.navigateUp()
                    }
                }

            }else {
                Toast.makeText(AppGlobal.getAppContext(),"字数超过200", Toast.LENGTH_SHORT).show()
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
                            VideoItem(videoBitmap ?: R.drawable.default_cover, modifier = Modifier.width(100.dp).height(150.dp)) {
                                isPlay = true
                            }
                        }else {
                            PlayVideoScreen(videoUri!!){isPlay = false}
                        }
                    }

                }
                else {
                    LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = PaddingValues(
                        DefaultUserPadding
                    ), verticalArrangement = Arrangement.spacedBy(5.dp), horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.width(size*3+40.dp)) {
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
                                AsyncImage(ImageRequest.Builder(LocalContext.current).data(R.drawable.add_image).build(),contentDescription = null,
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
                CreatePostItem(R.drawable.location,location) { thisController.switchTab(
                    CreatePostEnum.SELECT_LOCATION.route) }
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = DividerColor)
                //谁可以看
                CreatePostLookItem(R.drawable.user,lookModel, svm = svm) { thisController.switchTab(
                    CreatePostEnum.SELECT_SCOPE.route) }
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
fun CreatePostScaffold(title:String="", selectedUri: Uri = Uri.Builder().build(), lookImage:Boolean=false, onCancel:()->Unit, onCreate:()->Unit, onChange:()->Unit = {}, content:@Composable ColumnScope.()->Unit) {

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
fun ImageItem(data:Any, size: Dp, onDelete:()->Unit={}, onclick:()->Unit) {
    Box {
        AsyncImage(ImageRequest.Builder(LocalContext.current).data(data).build(),contentDescription = null, modifier = Modifier
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
fun VideoItem(data: Any, modifier: Modifier = Modifier, onclick:  (() -> Unit)? = null) {
    Box(modifier = Modifier.clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
        if (onclick  != null) {
            onclick()
        }
    }) {
        AsyncImage(
            ImageRequest.Builder(LocalContext.current).data(data).build(),contentDescription = null, modifier = modifier
            .defaultMinSize(100.dp,150.dp), contentScale = ContentScale.Crop)
        Icon(
            Icons.Filled.PlayArrow,contentDescription = null, tint = Color.White , modifier = Modifier
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
            Spacer(
                Modifier
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

// 状态数据类
internal class FriendCircleState(
    val scope: CoroutineScope,
    val launcher: ManagedActivityResultLauncher<String, Uri?>,
    val activityLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    val mutableLauncher: ManagedActivityResultLauncher<String, List<@JvmSuppressWildcards Uri>>,
    val videoLauncher: ManagedActivityResultLauncher<String, Uri?>,
    val focusRequester: FocusRequester,
    private val selectedUriState: MutableState<Uri?>,
    private val urisSizeState: MutableIntState,
    private val isLoadingState: MutableState<Boolean>,
    private val selectedVideoUriState: MutableState<Uri?>,
) {
    var selectedUri: Uri?
        get() = selectedUriState.value
        set(value) {selectedUriState.value = value}
    var isLoading:Boolean
        get() = isLoadingState.value
        set(value) {isLoadingState.value = value}
    var urisSize: Int
        get() = urisSizeState.intValue
        set(value) { urisSizeState.intValue = value }
    var selectedVideoUri: Uri?
        get() = selectedVideoUriState.value
        set(value) { selectedVideoUriState.value = value }

}
@Composable
fun PlayVideoScreen(uri: Uri,onCancel: () -> Unit) {
    Box {
        PlayVideo(uri)
        Icon(Icons.Filled.Close,contentDescription = null, tint = Color.White, modifier = Modifier.clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { onCancel() })
    }
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