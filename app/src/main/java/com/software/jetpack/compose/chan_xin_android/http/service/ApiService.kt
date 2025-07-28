package com.software.jetpack.compose.chan_xin_android.http.service

import com.google.gson.annotations.SerializedName
import com.software.jetpack.compose.chan_xin_android.entity.Friend
import com.software.jetpack.compose.chan_xin_android.entity.FriendApply
import com.software.jetpack.compose.chan_xin_android.entity.FriendStatus
import com.software.jetpack.compose.chan_xin_android.entity.Post
import com.software.jetpack.compose.chan_xin_android.entity.PostContent
import com.software.jetpack.compose.chan_xin_android.entity.PostMeta
import com.software.jetpack.compose.chan_xin_android.entity.User
import com.software.jetpack.compose.chan_xin_android.http.entity.ApiResult
import com.software.jetpack.compose.chan_xin_android.util.StringUtil
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import java.util.UUID

interface ApiService {
    data class RegisterReq(val phone: String,val password:String,val nickname:String=UUID.randomUUID().toString(),val sex:Byte = 0,val avatar:String = "")
    data class LoginReq(val phone: String,val password:String)
    data class TokenResp(val token:String,val exp:Long)
    data class UpdateUserReq(val nickname: String? = null,val avatar:String? = null,val sex:Int? = null)
    @GET("v1/user/findUser")
    suspend fun findUser(@Query("name") name: String = "点",@Query("phone") phone:String="1",@Query("ids") ids:String=StringUtil.listToString(
        listOf("1")
    )):ApiResult<DataInfosWrapper<User>>
    @POST("v1/user/register")
    suspend fun register(@Body registerReq:RegisterReq):ApiResult<TokenResp>
    @POST("v1/user/login")
    suspend fun login(@Body loginReq:LoginReq):ApiResult<TokenResp>

    data class DataInfoWrapper<T>(val info:T)
    data class DataInfosWrapper<T>(val infos:List<T>)
    data class DataListWrapper<T>(val list:List<T>)
    data class DataFriendListWrapper<T>(@SerializedName("friend_list") val list:List<T>)
    @GET("v1/user/userinfo")
    suspend fun userInfo(@Header("Authorization") authToken: String? = "",) : ApiResult<DataInfoWrapper<User>>
    @PATCH("v1/user/update")
    suspend fun updateUser(@Header("Authorization") authToken:String? = "",@Body updateUserReq: UpdateUserReq):ApiResult<DataInfoWrapper<User>>

    data class FriendApplyRequest(
        @SerializedName("applicant_id") val applicantId: String = "1",
        @SerializedName("target_id") val targetId: String = "1",
        @SerializedName("greet_msg") val greetMsg: String = "1"
    )
    data class FriendApplyResponse(
        @SerializedName("apply_id") val applyId: String,
        @SerializedName("apply_time") val applyTime: Long
    )
    class Empty()
    @POST("/v1/social/firend/applyFriend")
    suspend fun applyFriend(@Body friendApplyRequest: FriendApplyRequest):ApiResult<FriendApplyResponse>
    @GET("/v1/social/firend/getFriendApplyList")
    suspend fun getFriendApplyList(@Query("user_id") uid:String):ApiResult<DataListWrapper<FriendApply>>
    @GET("/v1/social/firend/getHandleFriendApplyList")
    suspend fun getHandleFriendApplyList(@Query("targetId") tid:String):ApiResult<DataListWrapper<FriendApply>>
    data class FriendApplyAction(@SerializedName("applicant_id") val applicantId:String = "1",@SerializedName("target_id") val targetId:String = "1",@SerializedName("is_approved") val isApproved:Boolean)
    @POST("/v1/social/firend/handleFriendApply")
    suspend fun handleFriendApply(@Body friendApplyAction:FriendApplyAction)
    data class UpdateFriendStatus(@SerializedName("user_id") val userId:String,@SerializedName("friend_id") val friendId:String,@SerializedName("status")val status:FriendStatus)
    @PUT("/v1/social/firend/updateFriendStatus")
    suspend fun updateFriendStatus(@Body updateFriendStatus:UpdateFriendStatus)

    @GET("/v1/social/firend/getFriendList")
    suspend fun getFriendList(@Query("user_id") userId:String = "1"):ApiResult<DataFriendListWrapper<Friend>>


    data class SetCoverRequest(val userId:String,val coverUrl:String)
    data class CreatePostRequest(val userId: String,val content:PostContent,val meta:PostMeta)
    @POST("/v1/dynamics/createPost")
    suspend fun createPost(@Body createPostRequest:CreatePostRequest):ApiResult<Post>

    @PUT("/v1/dynamics/setCover")
    suspend fun setCover(@Body setCoverRequest:SetCoverRequest)



}