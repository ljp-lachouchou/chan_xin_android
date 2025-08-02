package com.software.jetpack.compose.chan_xin_android.vm

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.software.jetpack.compose.chan_xin_android.cache.dao.IUserDao
import com.software.jetpack.compose.chan_xin_android.entity.Pagination
import com.software.jetpack.compose.chan_xin_android.entity.Post
import com.software.jetpack.compose.chan_xin_android.entity.PostContent
import com.software.jetpack.compose.chan_xin_android.entity.PostMeta
import com.software.jetpack.compose.chan_xin_android.entity.toJson
import com.software.jetpack.compose.chan_xin_android.entity.toQueryMap
import com.software.jetpack.compose.chan_xin_android.http.service.ApiService
import com.software.jetpack.compose.chan_xin_android.http.service.HttpService
import com.software.jetpack.compose.chan_xin_android.ui.activity.LoginScreen
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject
@HiltViewModel
class DynamicViewModel @Inject constructor(userDao: IUserDao):ViewModel() {
    private val apiService = HttpService.getService()
    private val _videoUri = MutableStateFlow<Uri?>(null)
    private val _photoUris = MutableStateFlow<List<Uri>>(emptyList())
    private val pagingConfig = PagingConfig(
        pageSize = 6, // 每页加载数量
        prefetchDistance = 3,
        initialLoadSize = 6,
        enablePlaceholders = false // 不启用占位符（适合网络数据）
    )
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

    private val _currentUid = MutableStateFlow("")
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val phone = AppGlobal.getUserPhone()
            userDao.getUserInfoByPhone(phone).collect{
                    user->
                setCurrentUid(user.id)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingDataFlow: Flow<PagingData<Post>> = _currentUid.flatMapLatest { uid ->
        Pager(
            config = pagingConfig,
            pagingSourceFactory = { TokenPagingSource(viewerId = uid) }).flow.cachedIn(
            viewModelScope
        )
    }.catch { Log.e("DynamicViewModel_pagingDataFlow", it.message.toString()) }

    inner class TokenPagingSource(private val initPagingToken:String = "NONE",private val viewerId:String):PagingSource<String,Post>() {
        override fun getRefreshKey(state: PagingState<String, Post>): String? {
            return initPagingToken
        }

        override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
            return try {
                val currentToken = params.key ?: initPagingToken
                val response = apiService.listVisiblePosts(viewerId,params.loadSize,currentToken)
                LoadResult.Page(
                    data = response.data?.posts ?: emptyList(),
                    nextKey = if (response.data?.posts==null) null else response.data!!.nextPageToken,
                    prevKey = null
                )
            }catch (e:NoMoreDataException){
                LoadResult.Error(e)
            } catch (e:Exception) {
                Log.e("fuck_DynamicViewModel",e.message.toString())
                LoadResult.Error(e)
            }
        }

    }
    inner class NoMoreDataException(message:String):Exception(message)
    fun setCurrentUid(uid:String) {
        _currentUid.value = uid
    }
}