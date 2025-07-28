package com.software.jetpack.compose.chan_xin_android.entity

data class Post(val postId:String,val userId:String,val content:PostContent,val meta:PostMeta,val isPinned:Boolean) {
    constructor():this("","",PostContent(),PostMeta(),false)
}
data class PostContent(val text:String,val imageUrls:List<String>,val emoji:String) {
    constructor():this("", emptyList(),"")
}
data class PostMeta(val location:String,val scope:Int,val visibleUserIds:List<String>) {
    constructor():this("",0, emptyList()) //可见范围:0-全部、1-私密、2-部分可见
}