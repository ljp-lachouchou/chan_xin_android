package com.software.jetpack.compose.chan_xin_android

import android.app.Application
import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.amap.api.location.AMapLocationClient
import com.amap.api.maps.MapsInitializer
import com.amap.api.services.core.ServiceSettings
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File

@HiltAndroidApp
class MyApp:Application() {
    override fun onCreate() {
        super.onCreate()
        // 定位隐私政策同意
        AMapLocationClient.updatePrivacyShow(this,true,true)
        AMapLocationClient.updatePrivacyAgree(this,true)
        // 地图隐私政策同意
        MapsInitializer.updatePrivacyShow(this,true,true)
        MapsInitializer.updatePrivacyAgree(this,true)
        // 搜索隐私政策同意
        ServiceSettings.updatePrivacyShow(this,true,true)
        ServiceSettings.updatePrivacyAgree(this,true)
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