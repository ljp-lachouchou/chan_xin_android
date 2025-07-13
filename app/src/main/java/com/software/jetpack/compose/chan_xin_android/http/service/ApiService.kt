package com.software.jetpack.compose.chan_xin_android.http.service

import com.google.gson.JsonObject
import com.software.jetpack.compose.chan_xin_android.entity.User
import com.software.jetpack.compose.chan_xin_android.http.entity.ApiResult
import com.software.jetpack.compose.chan_xin_android.util.StringUtil
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.UUID

interface ApiService {
    data class RegisterReq(val phone: String,val password:String,val nickname:String=UUID.randomUUID().toString(),val sex:Byte = 0,val avatar:String = "")
    data class LoginReq(val phone: String,val password:String)
    data class TokenResp(val token:String,val exp:Long)
    @GET("v1/user/findUser")
    suspend fun findUser(@Query("name") name: String = "点",@Query("phone") phone:String="1",@Query("ids") ids:String=StringUtil.listToString(
        listOf("1")
    )):ApiResult<List<User>>
    @POST("v1/user/register")
    suspend fun register(@Body registerReq:RegisterReq):ApiResult<TokenResp>
    @POST("v1/user/login")
    suspend fun login(@Body loginReq:LoginReq):ApiResult<TokenResp>
}