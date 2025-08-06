package com.software.jetpack.compose.chan_xin_android.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity("post_like", indices = [Index("post_id","user_id", unique = true)])
data class PostLike(
    @PrimaryKey(true)
    val id: Long = 0,
    @ColumnInfo("post_id")
    val postId: String,
    @ColumnInfo("user_id")
    val userId: String,
    @ColumnInfo("is_deleted")
    val isDeleted: Boolean)

data class Comment(
    val id: Long = 0,
    val postId: String,
    val userId: String,
    val content: String,
    val isDeleted: Boolean
)

data class CommentReply(
    val id: Long = 0,
    val commentId: Long,
    val userId: String,
    val targetUserId: String,
    val content: String,
    val isDeleted: Boolean
)