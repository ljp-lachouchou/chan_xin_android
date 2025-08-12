package com.software.jetpack.compose.chan_xin_android.ui.fragment.find

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserPadding
import com.software.jetpack.compose.chan_xin_android.entity.Friend
import com.software.jetpack.compose.chan_xin_android.entity.Post
import com.software.jetpack.compose.chan_xin_android.ext.switchTab
import com.software.jetpack.compose.chan_xin_android.ext.toTime
import com.software.jetpack.compose.chan_xin_android.ui.activity.MainActivityRouteEnum
import com.software.jetpack.compose.chan_xin_android.ui.activity.Wrapper
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.base.CanLookImage
import com.software.jetpack.compose.chan_xin_android.ui.base.EightSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.FiveSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.FourSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.LazyColumnWithCover
import com.software.jetpack.compose.chan_xin_android.ui.base.NineSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.OneSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.SelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.SevenSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.SixSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.ThreeSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.base.TwoSelfImageUi
import com.software.jetpack.compose.chan_xin_android.ui.fragment.TopBarWithBack
import com.software.jetpack.compose.chan_xin_android.ui.theme.LittleTextColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.PlaceholderColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.SurfaceColor
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.vm.DynamicViewModel
import com.software.jetpack.compose.chan_xin_android.vm.SocialViewModel
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelfFriendCircleScreen(navController: NavHostController, svm: SocialViewModel, dvm: DynamicViewModel) {
    val uvm: UserViewmodel = hiltViewModel()
    val user by uvm.myUser.collectAsState()
    val friend by svm.clickFriend.collectAsState()
    val selfUid by remember(friend) { derivedStateOf { friend.userId } }
    val isPinPosts = dvm.isPinedSelfFlow.collectAsLazyPagingItems()
    val notPinPosts = dvm.notPinedSelfFlow.collectAsLazyPagingItems()
    val nickName by remember(friend) { derivedStateOf { if (selfUid==user.id) user.nickname else friend.displayName } }
    LaunchedEffect(selfUid) {
        if (selfUid != "") {
            dvm.setSelfCircleUid(selfUid)
        }
    }
    var filePath by remember { mutableStateOf("") }
    LaunchedEffect(selfUid) {
        if (selfUid != "") {
            filePath = AppGlobal.getFilePath(selfUid)
        }
    }
    val listState = rememberLazyListState()
    Box {
        LazyColumnWithCover(filePath,nickName,friend.displayAvatar, canChangeCover = false,onEnterFriendInfoDetail = {}, onChangeCover = {}, onRefresh = {}, listState = listState, verticalArrangement = Arrangement.spacedBy(5.dp)) {
            items(notPinPosts.itemSnapshotList.items) {post->
                SelfPostItem(post) {
                    dvm.loadCurrentPost(post.postId)
                    navController.switchTab(MainActivityRouteEnum.MAIN_POST_INFO.route)
                }
            }
        }
        TopBarWithBack(navController, backTint = Color.White, color = Color.Transparent, action = {
            if (selfUid == user.id) {
                Icon(
                    painterResource(R.drawable.more),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            navController.switchTab(MainActivityRouteEnum.MESSAGE_QUEUE_SCREEN.route)
                        }
                )
            }
        })
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelfPostItem(post: Post, onClick: () -> Unit) {
    val isAllText by remember(post) { derivedStateOf { post.content.imageUrls == null } }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = DefaultUserPadding)){
        BaseText(post.createTime.toTime("dd日MM月"))
        Spacer(modifier = Modifier.width(10.dp))
        Surface(color = if (isAllText) SurfaceColor else Color.Transparent) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(100.dp).fillMaxWidth().clickable { onClick() }
            ) {
                if (!isAllText) {
                    MediaOrImageDisplayArea(post.content.imageUrls!!, onClick = onClick)
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Text(post.content.text, modifier = Modifier.height(100.dp).weight(1f), color = Color.Black, overflow = TextOverflow.Ellipsis)
            }
        }
    }

}
@Composable
fun MediaOrImageDisplayArea(urls:List<String>,onClick: () -> Unit) {
    val context= LocalContext.current
    val selfImageUi by remember(urls) { derivedStateOf {
        when(urls.size) {
            1-> OneSelfImageUi(context,urls)
            2-> TwoSelfImageUi(context,urls)
            3-> ThreeSelfImageUi(context,urls)
            4-> FourSelfImageUi(context,urls)
            5-> FiveSelfImageUi(context,urls)
            6-> SixSelfImageUi(context,urls)
            7-> SevenSelfImageUi(context,urls)
            8-> EightSelfImageUi(context,urls)
            9-> NineSelfImageUi(context,urls)
            else -> OneSelfImageUi(context,urls)
        }
    } }
    if (urls[0].contains("mp4")) {
        VideoItem(R.drawable.default_cover,modifier = Modifier.size(100.dp)) {

            onClick()
        }
    }else {
        SelfImageUi(selfImageUi)
    }
}


@Composable
fun DisplayImagesScreen(modifier: Modifier = Modifier, imageCount:Int? = 4, svm: SocialViewModel, dvm: DynamicViewModel = hiltViewModel()) {
    val friend by svm.clickFriend.collectAsState()
    val selfUid by remember(friend) { derivedStateOf { friend.userId } }
    val notPinPosts = dvm.notPinedSelfFlow.collectAsLazyPagingItems()
    LaunchedEffect(selfUid) {
        if (selfUid != "") {
            dvm.setSelfCircleUid(selfUid)
        }
    }
    val posts by remember(notPinPosts) { derivedStateOf { notPinPosts.itemSnapshotList.items } }
    val images by remember(posts) {
        derivedStateOf {
            val list = posts.mapNotNull { it.content.imageUrls }.flatten()
            if (imageCount != null) {
                list.take(imageCount)
            } else {
                list.take(6)
            }
        }
    }
    Log.e("DisplayImagesScreen_images",posts.toString())
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        images.forEach { image->
            if (!image.contains("mp4")) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(image).build(),
                    contentDescription = null,
                    modifier= Modifier.size(55.dp),
                    contentScale = ContentScale.Crop
                )
            }else {
                var bitmap: Bitmap? = null
                LaunchedEffect(Unit) {
                    bitmap = AppGlobal.getBitmapFromUrl(image)
                }
                VideoItem(bitmap ?: R.drawable.default_cover, modifier = Modifier.size(55.dp))
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainPostInfoScreen(navController: NavHostController,dvm: DynamicViewModel,svm: SocialViewModel) {
    val clickFriend by svm.clickFriend.collectAsState()
    var isSelected by remember { mutableStateOf(false) }
    val post by dvm.postInfo.collectAsState()
    var lookImage by remember { mutableStateOf<Any>(R.drawable.default_avatar) }
    CanLookImage(lookImage,isSelected, onChange = {
        isSelected = false
    }) {
        Scaffold(topBar = {
            TopBarWithBack(navController, title = "详情", color = SurfaceColor, action = {
                BaseText("")
            })
        }) {padding->
            PostInfoDetail(post,clickFriend, modifier = Modifier.padding(padding), onContentClick = {}, onDelete = {}, onLikeClick = {}) {
                lookImage = it
                Log.e("sdsdas_image",lookImage.toString())
                isSelected = true
            }
        }
    }

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostInfoDetail(post: Post,friend:Friend,modifier: Modifier=Modifier,onLikeClick:()->Unit,onDelete:()->Unit,onContentClick:()->Unit,onImageClick: (Any) -> Unit) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uvm:UserViewmodel = hiltViewModel()
    val user by uvm.myUser.collectAsState()
    Box(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().padding(
            DefaultUserPadding)) {
            UserAvatar(friend.displayAvatar, lifecycle) { onImageClick(friend.displayAvatar) }
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(post.content.text, color = Color.Black)
                if (post.content.imageUrls != null) MediaContent(post.content,false,true,lifecycle,onImageClick={
                    onImageClick(it)
                }) {

                }
                BottomDynamicArea(postOwnerId = post.postId, mid = user.id, createTime = post.createTime, location = post.meta.location,onLikeClick = onLikeClick, onDelete = onDelete, onContentClick = onContentClick)

            }
        }
    }
}

@Composable
fun PostInfoItem() {

}