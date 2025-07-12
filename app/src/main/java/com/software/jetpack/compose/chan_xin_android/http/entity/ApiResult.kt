package com.software.jetpack.compose.chan_xin_android.http.entity

class ApiResult<T>(code: Int?, msg: String?, data: T?) {
    var code = 0
    var msg:String = ""
    var data:T? = null
}