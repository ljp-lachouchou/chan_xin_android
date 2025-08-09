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
    val commentId: String,
    val postId: String,
    val userId: String,
    val content: String,
    val isDeleted: Boolean
)
@Entity("comment_reply")
data class CommentReply(
    @PrimaryKey
    @ColumnInfo("comment_replie_id")
    val commentReplieId: String,
    @ColumnInfo("post_id")
    val postId: String,
    @ColumnInfo("user_id")
    val userId: String,
    @ColumnInfo("target_user_id")
    val targetUserId: String,
    @ColumnInfo("content")
    val content: String,
    @ColumnInfo("is_deleted")
    val isDeleted: Boolean
)