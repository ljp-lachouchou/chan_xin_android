package com.software.jetpack.compose.chan_xin_android.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.software.jetpack.compose.chan_xin_android.entity.Friend
import com.software.jetpack.compose.chan_xin_android.entity.FriendApply
import com.software.jetpack.compose.chan_xin_android.entity.FriendRelation
import com.software.jetpack.compose.chan_xin_android.entity.FriendStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ISocialDao {
    @Query("SELECT * FROM friend_apply WHERE user_id = :uid")
    fun getHandleApplyList(uid:String):Flow<List<FriendApply>>
    @Query("SELECT * FROM friend_apply WHERE applicant_id=:uid")
    fun getApplyFriendList(uid: String):Flow<List<FriendApply>>
    @Query("SELECT * FROM friend_apply WHERE applicant_id = :uid")
    suspend fun getApplyFriendList2(uid:String):List<FriendApply>
    @Query("DELETE FROM friend_apply")
    suspend fun deleteAll()
    @Query("DELETE FROM friend_relation WHERE user_id=:userId and friend_id=:friendId")
    suspend fun deleteOne(userId:String,friendId:String)
    @Query("SELECT * FROM friend_apply")
    suspend fun getAllFriendApplyList():List<FriendApply>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFriendApply(list:List<FriendApply>):List<Long>
    @Query("SELECT COUNT(1) FROM friend_apply")
    suspend fun getFriendApplyCount():Int

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun saveFriendRelation(list:List<FriendRelation>):List<Long>
    @Query("""
        UPDATE friend_relation 
        SET status = :status 
        WHERE user_id = :userId AND friend_id = :friendId
    """)
    suspend fun updateFriendRelation(userId: String,friendId: String,status:FriendStatus)
    @Query("SELECT COUNT(1) FROM friend_relation")
    suspend fun getFriendRelationCount():Int
    @Query("DELETE FROM friend_relation")
    suspend fun deleteAllFriendRelation()
    @Query("SELECT * FROM friend_relation")
    suspend fun getAllFriendRelationList():List<FriendRelation>
    @Query(
        """
        SELECT 
            f.friend_id AS userId,
            u.nickname AS nickname,
            u.avatar AS avatarUrl,
            u.sex AS gender,
            f.status AS friendStatus
        FROM friend_relation f
        LEFT JOIN users u ON f.friend_id = u.id
        WHERE f.user_id = :uid
        """
    )
    fun getFriendList(uid: String): Flow<List<Friend>>
    @Query(
        """
        SELECT 
            u.id AS userId,
            u.nickname AS nickname,
            u.avatar AS avatarUrl,
            u.sex AS gender,
            f.status AS friendStatus
        FROM friend_relation f
        LEFT JOIN users u ON f.friend_id = u.id
        WHERE f.user_id = :uid and f.friend_id = :friendId
        """
    )
    fun getFriendInfo(uid: String,friendId: String): Flow<Friend>
    @Query("SELECT COUNT(1) FROM friend_relation WHERE user_id = :uid and friend_id = :friendId")
    suspend fun getFriendHas(uid: String,friendId: String): Int

}