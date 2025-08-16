package com.software.jetpack.compose.chan_xin_android.cache.module

import com.software.jetpack.compose.chan_xin_android.cache.dao.IChatDao
import com.software.jetpack.compose.chan_xin_android.cache.database.UserDatabase
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ChatDaoModule {
    @Provides
    fun provideChatDao():IChatDao {
        return UserDatabase.getInstance().chatDao()
    }
}