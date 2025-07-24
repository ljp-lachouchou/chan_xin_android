package com.software.jetpack.compose.chan_xin_android.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.software.jetpack.compose.chan_xin_android.entity.Friend
import com.software.jetpack.compose.chan_xin_android.entity.FriendApply
import com.software.jetpack.compose.chan_xin_android.entity.FriendRelation
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
    @Query("SELECT * FROM friend_apply")
    suspend fun getAllFriendApplyList():List<FriendApply>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFriendApply(list:List<FriendApply>):List<Long>
    @Query("SELECT COUNT(1) FROM friend_apply")
    suspend fun getFriendApplyCount():Int

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun saveFriendRelation(list:List<FriendRelation>):List<Long>
    @Query("SELECT COUNT(1) FROM friend_relation")
    suspend fun getFriendRelationCount():Int
    @Query("SELECT * FROM friend_relation")
    suspend fun getAllFriendRelationList():List<FriendRelation>
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
        WHERE f.user_id = :uid
        """
    )
    fun getFriendList(uid: String): Flow<List<Friend>>
}