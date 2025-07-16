package com.software.jetpack.compose.chan_xin_android

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.software.jetpack.compose.chan_xin_android.lifecycle.CameraActivityObserver
import com.software.jetpack.compose.chan_xin_android.ui.activity.Wrapper
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseActivity
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseButton
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.base.LoadingDialog
import com.software.jetpack.compose.chan_xin_android.ui.camera.Camera
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.util.Oss
import com.software.jetpack.compose.chan_xin_android.util.StringUtil
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraActivity:BaseActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LifecycleComponent()
            val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
            when {
                permissionState.status.isGranted -> {
                    CameraX(sharedViewModel)
                }
                permissionState.status.shouldShowRationale-> {
                    PermissionRationaleUI{
                        permissionState.launchPermissionRequest()
                    }
                }
                else -> {
                    PermissionDeniedUI {
                        // 打开应用设置页面
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.fromParts("package", packageName, null)
                        startActivity(intent)
                    }
                }
            }
            LaunchedEffect(Unit) {
                if (!permissionState.status.isGranted && !permissionState.status.shouldShowRationale) {
                    delay(50) // 延迟一小段时间，确保组件初始化完成
                    permissionState.launchPermissionRequest()
                }
            }
        }
    }
    override fun getDefaultLifeCycle(): DefaultLifecycleObserver {
        return CameraActivityObserver()
    }
}
@Composable
fun CameraX(vm:UserViewmodel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    var uri:Uri? by remember { mutableStateOf<Uri?>(null) }
    val previewView = remember {
        PreviewView(context).apply {
            id =  R.id.preview_view
        }
    }
    val imageCapture = remember {
        ImageCapture.Builder().build()
    }
    var isLoading by remember { mutableStateOf(false) }
    val scope= rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val animateSize = remember { Animatable(initialValue = 100f) }
    scope.apply {when(interactionSource.collectIsPressedAsState().value) {
        true -> launch { animateSize.animateTo(90f) }
        else-> launch { animateSize.animateTo(100f) }
    }  }
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    LaunchedEffect(cameraSelector) {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(lifecycleOwner,cameraSelector,preview,imageCapture)

            }catch (e:Exception) {
                Log.e("exception",e.toString())
            }
        },ContextCompat.getMainExecutor(context))
    }
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Column {
            if (uri != null) {
                Box(modifier = Modifier.fillMaxWidth().weight(0.8f).background(Color.Gray.copy(0.4f)), contentAlignment = Alignment.Center) {
                    AsyncImage(modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                        model = ImageRequest
                            .Builder(AppGlobal.getAppContext()).data(uri)
                            .build(),
                        contentDescription = null, contentScale = ContentScale.Crop
                    )
                }
            }else {
                AndroidView(factory = {previewView}, modifier = Modifier.fillMaxWidth().weight(0.8f))
            }
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp, top = 5.dp).background(Color.Black),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment =Alignment.CenterVertically
                ) {
                if (uri != null) {
                    BaseText(text = "取消", modifier = Modifier.clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { uri= null }, color = Color.White)
                }
                Wrapper(contentAlignment=Alignment.Center){
                    Surface(color = Color.White,modifier = Modifier.size(animateSize.value.dp).background(color = Color.White, shape = CircleShape).clickable(indication = null, interactionSource = interactionSource, enabled = (uri==null)) {
                        Log.e("imageCapture","拍摄照片")
                        val outputOptions = ImageCapture.OutputFileOptions.Builder(
                            context.contentResolver,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            createContentValues() // 设置图片信息（名称、类型等）
                        ).build()
                        imageCapture.takePicture(
                            outputOptions,
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                    // 拍照成功，获取Uri（outputFileResults.savedUri即为图片Uri）
                                    uri = outputFileResults.savedUri
                                    Log.e("outputFileResults.savedUri",uri.toString())
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    Toast.makeText(AppGlobal.getAppContext(),"拍照失败: ${exception.message}",Toast.LENGTH_SHORT).show()
                                    Log.e("拍照失败",exception.message.toString())
                                }
                            }
                        )
                    }, shape = CircleShape){}
                }
                if (uri != null) {

                    Wrapper {
                        BaseText(text = "完成", modifier = Modifier.clickable(indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            isLoading = true
                            scope.launch(Dispatchers.IO) {
                                val avatar = Oss.uploadFile(
                                    System.currentTimeMillis().toString()+"."+ StringUtil.getFileExtensionFromUri(AppGlobal.getAppContext(),uri),uri)
                                updateAvatar(avatar,vm,context)
                                delay(1000)
                                isLoading = false
                            }
                        }, color = Color.White)
                    }
                }
            }
        }
        Icon(Icons.Filled.Close, contentDescription = null, tint = Color.White, modifier = Modifier.padding(10.dp).clickable(indication = null, interactionSource = remember { MutableInteractionSource() }){
            (context as Activity).finish()
        })
        Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White, modifier = Modifier.padding(10.dp).align(Alignment.TopEnd).clickable(indication = null, interactionSource = remember { MutableInteractionSource() }, enabled = (uri == null)){
            cameraSelector = if (cameraSelector==CameraSelector.DEFAULT_FRONT_CAMERA) CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA
        })
        LoadingDialog(isLoading)

    }
}

suspend fun updateAvatar(avatar:String,vm:UserViewmodel,context:Context) {

    withContext(Dispatchers.Main) {
        if (avatar == "") {
            Toast.makeText(AppGlobal.getAppContext(),"更新头像失败,请稍后在试",Toast.LENGTH_SHORT).show()
        }else {
            withContext(Dispatchers.IO) {
                vm.updateUser(avatar=avatar)
                withContext(Dispatchers.Main) {
                    Toast.makeText(AppGlobal.getAppContext(),"更新头像成功",Toast.LENGTH_SHORT).show()
                    (context as Activity).finish()
                }

            }
        }
    }
}

@Composable
private fun PermissionRationaleUI(onRequest: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("需要相机权限才能拍照")
        BaseButton(onClick = onRequest) {
            Text(text = "授予权限", color = Color.White)
        }
    }
}
@Composable
private fun PermissionDeniedUI(onOpenSettings: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("相机权限已被拒绝，请在设置中开启")
        BaseButton(onClick = onOpenSettings) {
            Text(text = "去设置", color = Color.White)
        }
    }
}

private fun createContentValues(): ContentValues {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    return ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_$timeStamp.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.IS_PENDING, 1) // 标记为临时文件（后续需更新）
        }
    }
}
