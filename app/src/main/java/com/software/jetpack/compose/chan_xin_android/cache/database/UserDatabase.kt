package com.software.jetpack.compose.chan_xin_android.cache.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.software.jetpack.compose.chan_xin_android.cache.dao.IUserDao
import com.software.jetpack.compose.chan_xin_android.entity.User
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
internal const val DATABASE_NAME = "chan_xin.db"
@Database(entities = [User::class], version = 1)
abstract class UserDatabase:RoomDatabase() {
    abstract fun userDao():IUserDao
    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance:UserDatabase? = null

        fun getInstance(): UserDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase().also { instance = it }
            }
        }

        private fun buildDatabase(): UserDatabase {
            return Room.databaseBuilder(context = AppGlobal.getAppContext(), klass =  UserDatabase::class.java, name =  DATABASE_NAME)
                .build()
        }
    }

}