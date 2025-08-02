package com.software.jetpack.compose.chan_xin_android.ext

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toTime(pattern: String = "yyyy-MM-dd HH:mm:ss"):String {
    val instant = Instant.ofEpochSecond(this)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return localDateTime.format(formatter)
}