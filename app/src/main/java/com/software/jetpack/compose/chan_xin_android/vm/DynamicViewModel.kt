package com.software.jetpack.compose.chan_xin_android.vm

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.software.jetpack.compose.chan_xin_android.entity.Post
import com.software.jetpack.compose.chan_xin_android.entity.PostContent
import com.software.jetpack.compose.chan_xin_android.entity.PostMeta
import com.software.jetpack.compose.chan_xin_android.http.service.ApiService
import com.software.jetpack.compose.chan_xin_android.http.service.HttpService
import com.software.jetpack.compose.chan_xin_android.ui.activity.LoginScreen
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class DynamicViewModel:ViewModel() {
    private val apiService = HttpService.getService()
    private val _videoUri = MutableStateFlow<Uri?>(null)
    private val _photoUris = MutableStateFlow<List<Uri>>(emptyList())
    val videoUri:StateFlow<Uri?>
        get() = _videoUri
    val photoUris:StateFlow<List<Uri>>
        get() = _photoUris
    fun loadVideoUri(uri:Uri?) {
        _videoUri.value = uri
    }
    fun loadPhotoUris(uris:List<Uri>) {
        _photoUris.value = uris
    }
    suspend fun setCover(userId:String = "22",coverUrl:String = "22") {
        try {
            apiService.setCover(ApiService.SetCoverRequest(userId,coverUrl))
            withContext(Dispatchers.Main) {
                Toast.makeText(AppGlobal.getAppContext(),"更新封面成功",Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(AppGlobal.getAppContext(),"网络有些问题",Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun createPost(userId:String,content:PostContent,meta:PostMeta):Post? {
        return try {
            val post = apiService.createPost(ApiService.CreatePostRequest(userId,content,meta)).data
            withContext(Dispatchers.Main) {
                Toast.makeText(AppGlobal.getAppContext(),"发布成功",Toast.LENGTH_SHORT).show()
            }
            post
        }catch (e:HttpException) {
            Log.e("fuck_createPost_exception",e.message.toString())
            withContext(Dispatchers.Main) {
                Toast.makeText(AppGlobal.getAppContext(),"网络有些问题",Toast.LENGTH_SHORT).show()
            }
            null
        } catch (e:Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(AppGlobal.getAppContext(),"网络有些问题",Toast.LENGTH_SHORT).show()
            }
            null
        }
    }
}