package com.software.jetpack.compose.chan_xin_android.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult

suspend fun Context.getImageBitmapByUrl(url:String) : Bitmap? {
    val request = ImageRequest.Builder(this)
        .data(url)
        .allowHardware(false).build()
    val result = (imageLoader.execute(request) as SuccessResult).drawable
    return (result as BitmapDrawable).bitmap
}