package com.software.jetpack.compose.chan_xin_android.cache.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.software.jetpack.compose.chan_xin_android.entity.FriendFeed
import com.software.jetpack.compose.chan_xin_android.entity.Post
import com.software.jetpack.compose.chan_xin_android.entity.PostLike

@Dao
interface IDynamicDao {
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun saveFriendFeeds(list:List<FriendFeed>):List<Long>
    @Query("SELECT * FROM friend_feed ORDER BY create_time DESC")
    fun getFriendFeedsPaged(): PagingSource<Int, Post>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePostLikes(postLikes:List<PostLike>):List<Long>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePostLike(postLike:PostLike):Long


    @Query("SELECT * FROM post_like WHERE post_id = :postId")
    suspend fun listPostLikesIdByPostId(postId:String):List<PostLike>
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun savePosts(list:List<Post>):List<Long>

}