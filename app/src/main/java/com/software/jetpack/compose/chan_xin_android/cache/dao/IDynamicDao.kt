package com.software.jetpack.compose.chan_xin_android.cache.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.software.jetpack.compose.chan_xin_android.entity.CommentReply
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCommentReply(commentReply: CommentReply):Long

    @Delete
    suspend fun removeCommentReply(commentReply: CommentReply):Int

    @Query("delete from comment_reply where comment_replie_id = :commentReplyId")
    suspend fun removeCommentReplyByCommentReplyId(commentReplyId:String)

    @Query("SELECT * FROM comment_reply WHERE post_id = :postId and is_deleted = 0")
    suspend fun listCommentByPostId(postId:String):List<CommentReply>

    @Query("SELECT * FROM post_like WHERE post_id = :postId")
    suspend fun listPostLikesIdByPostId(postId:String):List<PostLike>
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun savePosts(list:List<Post>):List<Long>

}