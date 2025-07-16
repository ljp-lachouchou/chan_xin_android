package com.software.jetpack.compose.chan_xin_android

import android.app.Application
import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp:Application() {
    override fun onCreate() {
        super.onCreate()
        AppGlobal.init(this)
    }
}