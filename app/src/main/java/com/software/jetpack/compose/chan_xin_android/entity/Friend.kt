package com.software.jetpack.compose.chan_xin_android.entity

import androidx.compose.runtime.Stable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.software.jetpack.compose.chan_xin_android.R

@Entity("friend_relation")
data class FriendRelation(
    @PrimaryKey(true) val id: Long,
    @ColumnInfo("user_id") val userId: String,
    @ColumnInfo("friend_id") val friendId: String,
    val status: FriendStatus
)
@Stable
data class Friend(
    @SerializedName("user_id") val userId: String="",
    val nickname: String="",
    @SerializedName("avatar_url") val avatarUrl: String="",
    val gender: Int=0,
    @SerializedName("status") val friendStatus: FriendStatus = FriendStatus()
) {
    val displayName: String
        get() = friendStatus.remark?.ifEmpty { nickname } ?: ""

    // 预计算头像资源（优先用网络URL，无则用默认图）
    val displayAvatar: Any // Any 可兼容 String（URL）和 Int（资源ID）
        get() = avatarUrl.ifEmpty { R.drawable.default_avatar }
}

data class FriendStatus(
    @SerializedName("is_muted") val isMuted: Boolean? = null,
    @SerializedName("is_topped") val isTopped: Boolean? = null,
    @SerializedName("is_blocked") val isBlocked: Boolean? = null,
    val remark: String? = null
){
    constructor():this(false,false,false,"")
}
@Stable
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
    constructor(uid:String,nickname:String,avatar:String,sex:Int):this(0,uid,"",nickname,avatar,sex,"",0)
}