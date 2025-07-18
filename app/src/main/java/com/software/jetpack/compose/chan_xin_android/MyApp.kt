package com.software.jetpack.compose.chan_xin_android

import android.app.Application
import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File

@HiltAndroidApp
class MyApp:Application() {
    override fun onCreate() {
        super.onCreate()
        AppGlobal.init(this)
        val okHttpClient = OkHttpClient.Builder()
            .cache(cache = Cache(File(cacheDir, "coil_cache"), maxSize = 100L * 1024 * 1024)) // 100MB
            .build()
        Coil.setImageLoader{
            ImageLoader.Builder(this)
                .okHttpClient(okHttpClient)
                .memoryCache{MemoryCache.Builder(this).maxSizePercent(0.2).build()}
                .diskCache(DiskCache.Builder().directory(cacheDir.resolve("coil_custom_cache")).maxSizePercent(0.05).build())
                .build()
        }
    }
}