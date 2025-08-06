package com.software.jetpack.compose.chan_xin_android.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.software.jetpack.compose.chan_xin_android.entity.PostContent

class PostContentConverter {
    private val gson = Gson()
    @TypeConverter
    fun postContentToJson(postContent: PostContent):String {
        return gson.toJson(postContent)
    }
    @TypeConverter
    fun jsonToPostContent(json:String):PostContent {
        return gson.fromJson(json,PostContent::class.java)
    }
}