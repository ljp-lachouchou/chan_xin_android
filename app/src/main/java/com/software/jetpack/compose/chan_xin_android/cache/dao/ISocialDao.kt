package com.software.jetpack.compose.chan_xin_android.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.software.jetpack.compose.chan_xin_android.entity.FriendApply
import kotlinx.coroutines.flow.Flow

@Dao
interface ISocialDao {
    @Query("SELECT * FROM friend_apply WHERE user_id = :uid")
    fun getHandleApplyList(uid:String):Flow<List<FriendApply>>
    @Query("SELECT * FROM friend_apply WHERE applicant_id=:uid")
    fun getApplyFriendList(uid: String):Flow<List<FriendApply>>
    @Query("SELECT * FROM friend_apply WHERE applicant_id = :uid")
    suspend fun getApplyFriendList2(uid:String):List<FriendApply>
    @Query("SELECT * FROM friend_apply")
    suspend fun getAllFriendApplyList():List<FriendApply>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFriendApply(list:List<FriendApply>):List<Long>
    @Query("SELECT COUNT(1) FROM friend_apply")
    suspend fun getFriendApplyCount():Int
}