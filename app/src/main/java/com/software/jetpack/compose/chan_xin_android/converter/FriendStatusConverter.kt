package com.software.jetpack.compose.chan_xin_android.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.software.jetpack.compose.chan_xin_android.entity.FriendStatus

class FriendStatusConverter {
    private val gson = Gson()
    @TypeConverter
    fun friendStatusToString(status: FriendStatus):String {
        return gson.toJson(status)
    }
    @TypeConverter
    fun stringToFriendStatus(json: String):FriendStatus {
        val type = object : TypeToken<FriendStatus>() {}.type
        return gson.fromJson(json, type)
    }
}