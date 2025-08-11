package com.software.jetpack.compose.chan_xin_android.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.ui.fragment.VideoItem
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.util.StringUtil

@Preview
@Composable
fun ExampleDisplayScreen() {
    val images = listOf("https://chan-xin.oss-cn-beijing.aliyuncs.com/chan_xin/image/1754546851946.mp4", "https://chan-xin.oss-cn-beijing.aliyuncs.com/chan_xin/image/1754546802048.jpg", "https://chan-xin.oss-cn-beijing.aliyuncs.com/chan_xin/image/1754546803379.jpg", "https://chan-xin.oss-cn-beijing.aliyuncs.com/chan_xin/image/1754546804803.jpg")
    Row(verticalAlignment = Alignment.CenterVertically, modifier =Modifier.height(70.dp), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        images.forEach { image->
            if (!image.contains("mp4")) {
                AsyncImage(
                    model = ImageRequest.Builder(AppGlobal.getAppContext()).data(image).build(),
                    contentDescription = null,
                    modifier= Modifier.size(55.dp),
                    contentScale = ContentScale.Crop
                )
            }else {
                var bitmap:Bitmap? = null
                LaunchedEffect(Unit) {
                    bitmap = AppGlobal.getBitmapFromUrl(image)
                }
                VideoItem(bitmap ?: R.drawable.default_cover, modifier = Modifier.size(55.dp)) { }
            }
        }
    }
}