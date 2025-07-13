package com.software.jetpack.compose.chan_xin_android.util

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap


object StringUtil {
    fun listToString(ids:List<String>): String {
        if (ids.isEmpty()) {
            return ""
        }
        return ids.joinToString(",")
    }
    fun validatePassword(password: String): String? {
        // 条件1：长度8-16字符
        if (password.length < 8) {
            return "密码长度需至少8个字符"
        }
        if (password.length > 16) {
            return "密码长度需至多16个字符"
        }

        // 条件2：包含小写字母
        if (!password.contains(Regex("[a-z]"))) {
            return "密码需包含至少1个小写字母"
        }

        // 条件3：包含大写字母
        if (!password.contains(Regex("[A-Z]"))) {
            return "密码需包含至少1个大写字母"
        }

        // 条件4：包含数字
        if (!password.contains(Regex("[0-9]"))) {
            return "密码需包含至少1个数字"
        }

        // 条件5：包含特殊字符（!@#$%^&*）
        if (!password.contains(Regex("[!@#$%^&*]"))) {
            return "密码需包含至少1个特殊字符(!@#$%^&*)"
        }

        // 所有条件都满足
        return null
    }
    fun getFileExtensionFromUri(context: Context, uri: Uri?): String? {
        val contentResolver = context.contentResolver
        if (uri==null) {
            return null
        }
        val mimeType = contentResolver.getType(uri)
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(mimeType)
    }

}