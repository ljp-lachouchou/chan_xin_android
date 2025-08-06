package com.software.jetpack.compose.chan_xin_android.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.software.jetpack.compose.chan_xin_android.entity.PostMeta

class PostMetaConverter {
    private val gson = Gson()
    @TypeConverter
    fun postMetaToJson(postMeta: PostMeta):String {
        return gson.toJson(postMeta)
    }
    @TypeConverter
    fun jsonToPostMeta(json:String):PostMeta {
        return gson.fromJson(json,PostMeta::class.java)
    }
}