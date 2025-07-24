package com.software.jetpack.compose.chan_xin_android.ext

import com.software.jetpack.compose.chan_xin_android.util.PinAYinUtil

fun String.getGroupByFirstLetter():String {
    if (isEmpty()) return "#"
    val firstChar = PinAYinUtil.getFirstLetter(this)
    val lowercase = firstChar.uppercase()
    if (lowercase.first() in ('A'..'Z')) {
        return lowercase.first().toString()
    }
    return "#"
}