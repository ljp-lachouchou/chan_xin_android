package com.software.jetpack.compose.chan_xin_android

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioRecordingConfiguration
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
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.video.impl.VideoCaptureConfig
import androidx.camera.view.PreviewView
import androidx.camera.view.video.AudioConfig
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.software.jetpack.compose.chan_xin_android.lifecycle.CameraActivityObserver
import com.software.jetpack.compose.chan_xin_android.ui.activity.Wrapper
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseActivity
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseButton
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.base.LoadingDialog
import com.software.jetpack.compose.chan_xin_android.ui.base.PlayVideo
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.util.CameraUtil
import com.software.jetpack.compose.chan_xin_android.util.Oss
import com.software.jetpack.compose.chan_xin_android.util.StringUtil
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import io.sanghun.compose.video.VideoPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraActivity:BaseActivity() {
    private var cameraModel:Int = 0
    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraModel = intent.getIntExtra("camera_model",0)
        setContent {
            LifecycleComponent()
            val permissionState = rememberMultiplePermissionsState(listOf(Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO))
            when {
                permissionState.allPermissionsGranted -> {
                    CameraX(cameraModel)
                }
                permissionState.shouldShowRationale-> {
                    PermissionRationaleUI{
                        permissionState.launchMultiplePermissionRequest()
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
                if (!permissionState.allPermissionsGranted && !permissionState.shouldShowRationale) {
                    delay(50) // 延迟一小段时间，确保组件初始化完成
                    permissionState.launchMultiplePermissionRequest()
                }
            }
        }
    }
    override fun getDefaultLifeCycle(): DefaultLifecycleObserver {
        return CameraActivityObserver()
    }
}
@Composable
fun CameraX(cameraModel:Int,vm:UserViewmodel= hiltViewModel()) {
    var isRecording by remember { mutableStateOf(false) }
    var recordingTime by remember { mutableLongStateOf(0L) }
    val stopChannel = remember { Channel<Unit>(Channel.CONFLATED) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    var uri:Uri? by remember { mutableStateOf(null) }
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var recording by remember { mutableStateOf<Recording?>(null) }
    val previewView = remember {
        PreviewView(context).apply {
            id =  R.id.preview_view
        }
    }
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    val imageCapture = remember {
        ImageCapture.Builder().build()
    }
    val recorder = remember { Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.HIGHEST)).build() }
    val videoCapture by remember(recorder) { derivedStateOf { VideoCapture.withOutput(recorder) } }
    var isLoading by remember { mutableStateOf(false) }
    val scope= rememberCoroutineScope()
    val animateSize = remember { Animatable(initialValue = 100f) }
    val maxTime = 60_000L
    fun startRecording(awaitRelease:suspend ()-> Unit) {
        if (isRecording) return
        isRecording = true
        val startTime = System.currentTimeMillis()
        val timeJob = scope.launch {
            while (isActive) {
                recordingTime = System.currentTimeMillis() - startTime
                delay(100)
            }
        }
        scope.launch {
            val name = "VID_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}"
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/ComposeCamera")
                }
            }

            val outputOptions = MediaStoreOutputOptions
                .Builder(context.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                .setContentValues(contentValues)
                .build()
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                recording = videoCapture.output.prepareRecording(context, outputOptions)
                    .withAudioEnabled()
                    .start(ContextCompat.getMainExecutor(context)) {recordEvent->
                        when(recordEvent) {
                            is VideoRecordEvent.Finalize ->{
                                if (recordEvent.hasError()) {
                                    recording?.close()
                                    recording = null
                                } else {
                                    videoUri = recordEvent.outputResults.outputUri
                                }
                            }
                        }

                    }
            }
            val result = withTimeoutOrNull(maxTime) {
                select<Unit>{
                    //松手事件
                    async {
                        awaitRelease()
                    }.onAwait {}
                    stopChannel.onReceive { }
                }
            }
            recording?.stop()
            recording = null
            isRecording = false
            timeJob.cancel()
            recordingTime = 0L
            Log.e("开始录制", "录制结束了")
            if (result == null) {
                Log.e("开始录制", "录制结束了2")
            }
        }
    }
    fun stopRecording() {
        if (!isRecording) return
        scope.launch { stopChannel.send(Unit) }
    }
    scope.apply {when(isRecording) {
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
                cameraProvider.bindToLifecycle(lifecycleOwner,cameraSelector,preview,imageCapture,videoCapture)

            }catch (e:Exception) {
                Log.e("exception",e.toString())
            }
        },ContextCompat.getMainExecutor(context))
    }
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Column {
            if (uri != null || videoUri != null) {
                Box(modifier = Modifier.fillMaxWidth().weight(0.8f).background(Color.Gray.copy(0.4f)), contentAlignment = Alignment.Center) {
                    if (uri != null) {
                        AsyncImage(modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                            model = ImageRequest
                                .Builder(AppGlobal.getAppContext()).data(uri)
                                .build(),
                            contentDescription = null, contentScale = ContentScale.Crop
                        )
                    }else if (videoUri != null){
                        PlayVideo(videoUri!!, defaultAspectRatio = 1f)
                    }
                }
            }else {
                AndroidView(factory = {previewView}, modifier = Modifier.fillMaxWidth().weight(0.8f))
            }
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp, top = 5.dp).background(Color.Black),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment =Alignment.CenterVertically
                ) {
                if (uri != null || videoUri != null) {
                    BaseText(text = "取消", modifier = Modifier.clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { uri= null;videoUri=null }, color = Color.White)
                }
                Wrapper(contentAlignment=Alignment.Center){
                    Surface(color = Color.White,modifier = Modifier.size(animateSize.value.dp).background(color = Color.White, shape = CircleShape).pointerInput(Unit) {
                            detectTapGestures(onPress = {
                                try {
                                    val releasedInTime = withTimeoutOrNull(500) {
                                        awaitRelease() // 等待用户抬起，若在短时间抬起，返回true
                                        true
                                    }
                                    if (releasedInTime==true) {
                                        if (uri != null) return@detectTapGestures
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
                                }else {
                                    if (videoUri ==null && cameraModel==1) {
                                        Log.e("开始录制","开始录制")
                                        startRecording { awaitRelease() }
                                        try {
                                            awaitRelease()
                                        }finally {
                                            stopRecording()
                                        }
                                    }
                                }
                            }finally {

                            }
                    })
                }, shape = CircleShape){
                        if (cameraModel == 1) {
                            CircularProgressIndicator(progress = recordingTime * 1.0f / maxTime,color = IconGreen)
                        }
                    }
                }
                if (uri != null || videoUri != null) {
                    Wrapper {
                        BaseText(text = "完成", modifier = Modifier.clickable(indication = null,interactionSource= remember { MutableInteractionSource() }) {         when(cameraModel) {
                            0->{
                                isLoading = true
                                scope.launch(Dispatchers.IO) {
                                    val avatar = Oss.uploadFile(
                                        System.currentTimeMillis().toString()+"."+ StringUtil.getFileExtensionFromUri(AppGlobal.getAppContext(),uri),uri)
                                    updateAvatar(avatar,vm,context)
                                    delay(1000)
                                    isLoading = false
                                    uri = null
                                }
                            }
                            1->{
                                val activity = context as Activity
                                val resultIntent = Intent()
                                if (uri != null) {
                                    resultIntent.putExtra(CameraUtil.IMAGE_URI, uri)
                                    activity.setResult(CameraUtil.IMAGE_URI_CODE,resultIntent)
                                }
                                if (videoUri != null) {
                                    resultIntent.putExtra(CameraUtil.VIDEO_URI,videoUri)
                                    activity.setResult(CameraUtil.VIDEO_URI_CODE,resultIntent)
                                }
                                activity.finish()
                            }
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
