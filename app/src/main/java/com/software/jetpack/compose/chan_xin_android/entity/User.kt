package com.software.jetpack.compose.chan_xin_android.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id:String = "",
    val phone:String,
    var password:String,var avatar:String="",var nickname:String="",var sex:Byte = 0,var status:Byte = 0

) {
    constructor():this(phone = "",password = "")
    constructor(nickname: String,avatar:String,sex:Byte):this("","","",avatar,nickname,sex,0)
}
