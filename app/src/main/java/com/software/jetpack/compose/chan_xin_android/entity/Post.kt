package com.software.jetpack.compose.chan_xin_android.entity

import com.google.gson.Gson

data class Post(val postId:String,val userId:String,val content:PostContent,val meta:PostMeta,val isPinned:Boolean,val createTime:Long) {
    constructor():this("","",PostContent(),PostMeta(),false,0)
}
data class PostContent(val text:String,val imageUrls:List<String>?,val emoji:String) {
    constructor():this("", emptyList(),"")
}
data class PostMeta(val location:String,val scope:Int,val visibleUserIds:List<String>) {
    constructor():this("",0, emptyList()) //可见范围:0-全部、1-私密、2-部分可见
}
data class Pagination(val pageSize:Int = 10,val pageToken:String)
fun Pagination.toQueryMap():Map<String,Any?> {
    val params:Map<String,Any?> =mapOf("pagination.pageSize" to pageSize,"pagination.pageToken" to pageToken)
    return params
}
fun Pagination.toJson(): String {
    return Gson().toJson(this) // 使用 Gson 序列化
}