package com.software.jetpack.compose.chan_xin_android.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.software.jetpack.compose.chan_xin_android.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface IUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user:User):Long

    @Query("SELECT * FROM users WHERE `id` = :id")
    suspend fun getUserInfoById(id:String):User
    @Query("SELECT * FROM users WHERE `phone` = :phone")
    fun getUserInfoByPhone(phone:String):Flow<User>
    @Query("SELECT COUNT(1) FROM users")
    fun getUserCount():Int
    @Query("SELECT `avatar` FROM users WHERE `phone` = :phone")
    suspend fun getUserAvatarByPhone(phone:String):String
}