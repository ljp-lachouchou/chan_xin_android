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
import com.software.jetpack.compose.chan_xin_android.cache.dao.IDynamicDao
import com.software.jetpack.compose.chan_xin_android.cache.dao.IUserDao
import com.software.jetpack.compose.chan_xin_android.entity.FriendFeed
import com.software.jetpack.compose.chan_xin_android.entity.Pagination
import com.software.jetpack.compose.chan_xin_android.entity.Post
import com.software.jetpack.compose.chan_xin_android.entity.PostContent
import com.software.jetpack.compose.chan_xin_android.entity.PostLike
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject
@HiltViewModel
class DynamicViewModel @Inject constructor(private val userDao: IUserDao,private val dynamicDao: IDynamicDao):ViewModel() {
    private val apiService = HttpService.getService()
    private val _videoUri = MutableStateFlow<Uri?>(null)
    private val _photoUris = MutableStateFlow<List<Uri>>(emptyList())
    private val pagingConfig = PagingConfig(
        pageSize = 6, // 每页加载数量
        initialLoadSize = 4,
        enablePlaceholders = false // 不启用占位符（适合网络数据）
    )
    data class UidWithVersion(val uid: String, val version: Int)

    // 初始化 StateFlow
    private var version = 0
    private val _currentUid = MutableStateFlow(UidWithVersion("初始uid", version))
    private val likeIdsMutableFlowCache = mutableMapOf<String, MutableStateFlow<List<String>>>()
    private val likeIdsFlowCache = mutableMapOf<String, StateFlow<List<String>>>()
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

    suspend fun deletePost(userId: String,postId:String) {
        try {
            apiService.deletePost(userId,postId)
        }catch (e:Exception) {
            Log.e("dynamic_delete_post_fuck",e.toString())
        }
    }

    suspend fun userLikedPost(userId: String,postId: String):Boolean {
        return try {
            val userLikedPost = apiService.userLikedPost(userId,postId)
            userLikedPost.data?.isLiked ?: false
        }catch (e:Exception) {
            false
        }
    }

    //获取此动态点赞ids
    fun listLikeByPostId(postId: String):StateFlow<List<String>> {
        return likeIdsMutableFlowCache.getOrPut(postId) {
            MutableStateFlow<List<String>>(emptyList()).also { flow ->
                // 首次加载数据
                loadInitialLikeIds(postId, flow)
            }
        }
    }
    private fun loadInitialLikeIds(postId: String, flow: MutableStateFlow<List<String>>) {
        viewModelScope.launch {
            if (AppGlobal.isNetworkValid()) {
                try {
                    val resp = apiService.listLikeByPostId(postId)
                    val newValue = resp.data?.ids ?: emptyList()
                    flow.value = newValue
                } catch (e: Exception) {
                    Log.e("listLikeByPostId", e.toString())
                }
            }else {
                val newValue = dynamicDao.listPostLikesIdByPostId(postId).map { it.userId }
                flow.value = newValue
            }
        }
    }
    fun addLikeId(postId: String, userId: String) {
        val flow = likeIdsMutableFlowCache[postId] ?: return
        val originalIds = flow.value
        // 本地立即更新（UI会实时刷新）
        val currentIds = flow.value.toMutableList()
        if (!currentIds.contains(userId)) {
            currentIds.add(userId)
            flow.value = currentIds
        }
        viewModelScope.launch {
            dynamicDao.savePostLike(PostLike(postId = postId, userId = userId, isDeleted = false))
            try {
                apiService.toggleLike(ApiService.LikeAction(postId,userId,false))
            } catch (e: Exception) {
                Log.e("addLikeId", "点赞失败", e)
                flow.value = originalIds // 回滚
            }
        }
    }
    fun removeLikeId(postId: String, userId: String) {
        val flow = likeIdsMutableFlowCache[postId] ?: return
        val originalIds = flow.value
        val currentIds = flow.value.toMutableList()
        if (currentIds.contains(userId)) {
            currentIds.remove(userId)
            flow.value = currentIds // 本地立即更新
        }
        // 同步网络请求（失败回滚）
        viewModelScope.launch {
            dynamicDao.savePostLike(PostLike(postId = postId, userId = userId, isDeleted = true))
            try {
                apiService.toggleLike(ApiService.LikeAction(postId,userId,true))
            } catch (e: Exception) {
                Log.e("removeLikeId", "取消点赞失败", e)
                flow.value = originalIds // 回滚
            }
        }
    }
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
    val pagingDataFlow: Flow<PagingData<Post>> = _currentUid.flatMapLatest { (uid,_) ->
        Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                if (AppGlobal.isNetworkValid()) TokenPagingSource(viewerId = uid) else dynamicDao.getFriendFeedsPaged()
            }).flow.cachedIn(
            viewModelScope
        )
    }.catch { Log.e("DynamicViewModel_pagingDataFlow", it.toString()) }

    inner class TokenPagingSource(private val initPagingToken:String = "NONE",private val viewerId:String):PagingSource<String,Post>() {
        override fun getRefreshKey(state: PagingState<String, Post>): String? {
            return initPagingToken
        }

        override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
            return try {
                val currentToken = params.key ?: initPagingToken
                val response = apiService.listVisiblePosts(viewerId,params.loadSize,currentToken)
                val list = response.data?.posts ?: emptyList()
                dynamicDao.savePosts(list)
                dynamicDao.saveFriendFeeds(list.map { FriendFeed(it.postId,it.userId,it.content,it.meta,it.isPinned,it.createTime) })
                LoadResult.Page(
                    data = list,
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
        version = (version + 1) % 10
        Log.e("oooooo_fuck1",uid)
        _currentUid.value = UidWithVersion(uid,version)
    }
}