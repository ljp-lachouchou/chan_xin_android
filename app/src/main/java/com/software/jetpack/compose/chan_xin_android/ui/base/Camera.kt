package com.software.jetpack.compose.chan_xin_android.ui.base

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.browse.MediaBrowser.MediaItem
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.media3.common.PlaybackException
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.source.MediaSource
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import io.sanghun.compose.video.RepeatMode
import io.sanghun.compose.video.VideoPlayer
import io.sanghun.compose.video.controller.VideoPlayerControllerConfig
import io.sanghun.compose.video.uri.VideoPlayerMediaItem


@Composable
fun PlayVideo(videoUri:Uri,defaultHeight:Dp = 150.dp,defaultAspectRatio:Float = 0f) {
    var modifier = Modifier.fillMaxWidth()
    modifier = if (defaultAspectRatio!= 0f) modifier.aspectRatio(defaultAspectRatio)
    else modifier.height(defaultHeight)
    VideoPlayer(
        mediaItems = listOf(
            VideoPlayerMediaItem.StorageMediaItem(
                storageUri = videoUri
            ),
        ),
        handleLifecycle = true,
        autoPlay = true,
        usePlayerController = true,
        enablePip = true,
        handleAudioFocus = true,
        controllerConfig = VideoPlayerControllerConfig(
            showSpeedAndPitchOverlay = false,
            showSubtitleButton = false,
            showCurrentTimeAndTotalTime = true,
            showBufferingProgress = false,
            showForwardIncrementButton = true,
            showBackwardIncrementButton = true,
            showBackTrackButton = true,
            showNextTrackButton = true,
            showRepeatModeButton = true,
            controllerShowTimeMilliSeconds = 5_000,
            controllerAutoShow = true,
            showFullScreenButton = false,
        ),
        volume = 1f,  // volume 0.0f to 1.0f
        repeatMode = RepeatMode.NONE,       // or RepeatMode.ALL, RepeatMode.ONE
        onCurrentTimeChanged = { // long type, current player time (millisec)
            Log.e("CurrentTime", it.toString())
        },
        playerInstance = { // ExoPlayer instance (Experimental)
            addAnalyticsListener(
                object : AnalyticsListener {
                    // player logger
                    @OptIn(UnstableApi::class)
                    override fun onTracksChanged(
                        eventTime: AnalyticsListener.EventTime,
                        tracks: Tracks
                    ) {
                        super.onTracksChanged(eventTime, tracks)

                        for (i in 0 until tracks.groups.size) {
                            val group = tracks.groups[i]
                            for (j in 0 until group.length) {
                                val format = group.getTrackFormat(j)
                                Log.d("VideoPlayer", "轨道类型：${format.sampleMimeType}，是否音频：${format.sampleMimeType?.startsWith("audio/")}")
                            }
                        }
                    }

                    @OptIn(UnstableApi::class)
                    override fun onPlayerError(
                        eventTime: AnalyticsListener.EventTime,
                        error: PlaybackException
                    ) {
                        super.onPlayerError(eventTime, error)
                        Log.e("VideoPlayerError", "播放错误：${error.message}")
                    }
                }
            )
        },
        modifier = modifier,
    )
}
@Composable
fun rememberVideoFrame(context:Context,videoUri:Uri,timeUs:Long = 0):Bitmap? {
    return extraVideoFrame(context,videoUri,timeUs)
}
fun extraVideoFrame(context:Context,videoUri:Uri,timeUs:Long):Bitmap? {
    return try {
        MediaMetadataRetriever().use {retriever->
            retriever.setDataSource(context,videoUri)
            retriever.getFrameAtTime(timeUs,MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        }
    }catch (e:Exception) {
        e.printStackTrace()
        null
    }
}
