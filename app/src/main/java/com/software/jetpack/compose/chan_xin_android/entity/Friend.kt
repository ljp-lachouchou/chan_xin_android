package com.software.jetpack.compose.chan_xin_android.entity

import com.google.gson.annotations.SerializedName

data class Friend(
    @SerializedName("user_id") val userId: String,
    val nickname: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    val gender: UInt,
    @SerializedName("status") val friendStatusInfo: FriendStatusInfo = FriendStatusInfo()
)

data class FriendStatus(
    @SerializedName("is_muted") val isMuted: Boolean? = null,
    @SerializedName("is_topped") val isTopped: Boolean? = null,
    @SerializedName("is_blocked") val isBlocked: Boolean? = null,
    val remark: String? = null
)
data class FriendStatusInfo(
    @SerializedName("is_muted") val isMuted: Boolean = false,
    @SerializedName("is_topped") val isTopped: Boolean= false,
    @SerializedName("is_blocked") val isBlocked: Boolean=false,
    val remark: String=""
){
    constructor():this(false,false,false,"")
}