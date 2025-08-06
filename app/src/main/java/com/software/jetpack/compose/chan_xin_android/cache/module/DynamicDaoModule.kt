package com.software.jetpack.compose.chan_xin_android.cache.module

import com.software.jetpack.compose.chan_xin_android.cache.dao.IDynamicDao
import com.software.jetpack.compose.chan_xin_android.cache.database.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DynamicDaoModule {
    @Provides
    fun providerDao():IDynamicDao {
        return UserDatabase.getInstance().dynamicDao()
    }
}