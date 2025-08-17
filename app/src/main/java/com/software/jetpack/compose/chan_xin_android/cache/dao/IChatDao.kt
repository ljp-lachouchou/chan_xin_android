package com.software.jetpack.compose.chan_xin_android.cache.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.software.jetpack.compose.chan_xin_android.entity.ChatLog
import kotlinx.coroutines.flow.Flow

@Dao
interface IChatDao {
    @Query("SELECT * FROM chat_log")
    suspend fun allChatLogs():List<ChatLog>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveChatLogs(chatLogs:List<ChatLog>):List<Long>
    @Query("""
        SELECT MAX(send_time) FROM chat_log WHERE conversation_id = :conversationId;
    """)
    suspend fun getMaxSendTime(conversationId: String):Long
    @Query("""
        SELECT * FROM chat_log 
        WHERE conversation_id = :conversationId 
        AND send_time BETWEEN :startTime AND :endTime 
        ORDER BY send_time DESC 
        LIMIT :count
    """)
    fun getChatLogsFlow(conversationId:String,startTime:Long = 0L,endTime:Long = System.currentTimeMillis(),count:Int = 10): Flow<List<ChatLog>>
    @Query("""
        SELECT * FROM chat_log 
        WHERE conversation_id = :conversationId 
        AND send_time BETWEEN :startTime AND :endTime 
        AND is_local = 0 
        ORDER BY send_time DESC 
        LIMIT :count
    """)
    suspend fun getChatLogs(conversationId:String,startTime:Long = 0L,endTime:Long = System.currentTimeMillis(),count:Int = 10): List<ChatLog>
    @Query("DELETE FROM chat_log")
    suspend fun deleteAllChatLog()
}