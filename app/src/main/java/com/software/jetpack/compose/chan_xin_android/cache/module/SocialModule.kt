package com.software.jetpack.compose.chan_xin_android.cache.module

import com.software.jetpack.compose.chan_xin_android.cache.dao.ISocialDao
import com.software.jetpack.compose.chan_xin_android.cache.dao.IUserDao
import com.software.jetpack.compose.chan_xin_android.cache.database.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SocialModule {
    @Provides
    fun providerDao():ISocialDao {
        return UserDatabase.getInstance().socialDao()
    }
}