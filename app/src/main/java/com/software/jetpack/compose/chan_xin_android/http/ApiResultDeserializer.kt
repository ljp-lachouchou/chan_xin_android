package com.software.jetpack.compose.chan_xin_android.http

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.software.jetpack.compose.chan_xin_android.http.entity.ApiResult
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResultDeserializer<T>:JsonDeserializer<ApiResult<T>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ApiResult<T> {
        val jsonObject = json?.asJsonObject
        val code = jsonObject?.get("code")?.asInt
        val msg = jsonObject?.get("msg")?.asString

        // 解析data字段（核心逻辑）
        val dataElement = jsonObject?.get("data")
        var data: T? = null
        if (dataElement != null && dataElement.isJsonObject) {
            val dataObj = dataElement.asJsonObject
            val resultMap = mutableMapOf<String, Any?>()

            // 遍历data中的所有字段
            dataObj.entrySet().forEach { (key, value) ->
                when {
                    // 处理数组字段
                    value.isJsonArray -> {
                        val listType = object : TypeToken<List<Map<String, Any>>>() {}.type
                        resultMap[key] = context?.deserialize<List<Map<String, Any>>>(value, listType)
                    }

                    // 处理嵌套对象
                    value.isJsonObject -> {
                        val jsonType = object : TypeToken<Map<String, Any>>() {}.type
                        resultMap[key] = context?.deserialize<Map<String, Any>>(value,jsonType)
                    }

                    // 处理基本类型
                    value.isJsonPrimitive -> {
                        resultMap[key] = value.asJsonPrimitive.toString()
                    }

                    // 处理null
                    value.isJsonNull -> {
                        resultMap[key] = null
                    }
                }
            }

            data = resultMap as T
        }
        return ApiResult<T>(code,msg,data)
    }

}