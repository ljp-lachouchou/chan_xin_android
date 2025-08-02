package com.software.jetpack.compose.chan_xin_android

import android.app.Application
import android.content.Context
import android.graphics.ImageDecoder
import android.os.Build
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.BitmapFactoryDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.amap.api.location.AMapLocationClient
import com.amap.api.maps.MapsInitializer
import com.amap.api.services.core.ServiceSettings
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Cache
import okhttp3.Dispatcher
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
        val imageLoader = ImageLoader.Builder(this)
            // 1. 增大内存缓存（默认值可能偏小，根据设备内存调整）
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.2) // 最多使用应用可用内存的 20%
                    .build()
            }
            // 2. 启用磁盘缓存（默认开启，但可调整大小）
            .diskCache {
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("coil_disk_cache"))
                    .maxSizeBytes(512L * 1024 * 1024) // 512MB 磁盘缓存
                    .build()
            }
            // 3. 限制并发加载数量（避免线程拥堵）
            .okHttpClient {
                OkHttpClient.Builder()
                    .dispatcher(Dispatcher().apply { maxRequestsPerHost = 5 }) // 同一主机最多5个并发请求
                    .build()
            }
            // 4. 使用更高效的解码器（针对大图片）
            .components {
                add(BitmapFactoryDecoder.Factory())
            }
            .build()
        Coil.setImageLoader{
            imageLoader
//            ImageLoader.Builder(this)
//                .okHttpClient(okHttpClient)
//                .memoryCache{MemoryCache.Builder(this).maxSizePercent(0.2).build()}
//                .diskCache(DiskCache.Builder().directory(cacheDir.resolve("coil_custom_cache")).maxSizePercent(0.05).build())
//                .build()
        }
    }
}