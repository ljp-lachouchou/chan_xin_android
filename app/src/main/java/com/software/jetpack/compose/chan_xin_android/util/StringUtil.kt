package com.software.jetpack.compose.chan_xin_android.util

object StringUtil {
    fun listToString(ids:List<String>): String {
        if (ids.isEmpty()) {
            return ""
        }
        return ids.joinToString(",")
    }
}