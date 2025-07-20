package com.software.jetpack.compose.chan_xin_android.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
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
@Entity("friend_apply", indices = [Index("user_id","applicant_id", unique = true)])
data class FriendApply(
    @PrimaryKey(true)
    val id:Int,
    @SerializedName("user_id") @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo("applicant_id") val applicantId:String,
    val nickname: String,
    @SerializedName("avatar_url") @ColumnInfo(name = "avatar_url") val avatar: String,
    val gender: Int,
    @SerializedName("greet_msg") @ColumnInfo(name = "greet_msg") val greetMsg: String,
    val status: Int
){
    constructor():this(0,"","","","",0,"",0)
}