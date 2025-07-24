package com.software.jetpack.compose.chan_xin_android.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.software.jetpack.compose.chan_xin_android.entity.FriendStatusInfo

class FriendStatusInfoConverter {
    private val gson = Gson()
    @TypeConverter
    fun friendStatusInfoToString(status: FriendStatusInfo):String {
        return gson.toJson(status)
    }
    @TypeConverter
    fun stringToFriendStatusInfo(json: String): FriendStatusInfo {
        val type = object : TypeToken<FriendStatusInfo>() {}.type
        return gson.fromJson(json, type)
    }
}