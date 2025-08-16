package com.software.jetpack.compose.chan_xin_android.ext

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.TimeInput
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toTime(pattern: String = "yyyy-MM-dd HH:mm:ss",unit:TimeUnit = TimeUnit.SECONDS):String {
    if (this == 0L) return ""
    val instant = when(unit) {
        TimeUnit.SECONDS -> {
            Instant.ofEpochSecond(this)
        }
        TimeUnit.MILLISECONDS -> {
            Instant.ofEpochMilli(this)
        }
        else ->{
            Instant.ofEpochSecond(this)
        }
    }
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return localDateTime.format(formatter)
}