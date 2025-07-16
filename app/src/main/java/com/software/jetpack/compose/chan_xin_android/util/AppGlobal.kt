package com.software.jetpack.compose.chan_xin_android.util

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.software.jetpack.compose.chan_xin_android.util.PreferencesFileName.PHONE_KEY
import com.software.jetpack.compose.chan_xin_android.util.PreferencesFileName.USERS_FILE
import com.software.jetpack.compose.chan_xin_android.util.PreferencesFileName.USER_TOKEN
import com.software.jetpack.compose.chan_xin_android.util.PreferencesFileName.USER_TOKEN_EXP
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    name = USERS_FILE // 传入文件名
)
@SuppressLint("StaticFieldLeak")
object AppGlobal {
    private lateinit var context: Context

    // 在 Application.onCreate() 中初始化
    fun init(appContext: Context) {
        context = appContext.applicationContext

    }
    fun getAppContext():Context {
        return context
    }
    fun getPreferenceUsersData():DataStore<Preferences> {
        return context.userDataStore
    }
    suspend fun hasPhoneHistory():String {
        return context.userDataStore.data.map { preferences ->
            preferences[PHONE_KEY] ?: ""
        }.first() // 检查是否存在phoneKey
    }
    suspend fun tokenIsAva():String {
        val token = context.userDataStore.data.map { preferences ->
            preferences[USER_TOKEN] ?: ""
        }.first()
        if (token == "") {
            Log.e("tokenIsAva","1")
            return ""
        }
        val isExp = context.userDataStore.data.map { preferences ->
            preferences[USER_TOKEN_EXP] ?: 0
        }.first()
        if (isExp < System.currentTimeMillis() / 1000) {
            Log.e("tokenIsAva","$isExp ${System.currentTimeMillis() / 1000}")

            return ""
        }
        Log.e("tokenIsAva","3")
        return token
    }
    suspend fun <T> saveUserRela(key: Preferences. Key<T>, value:T) {
        context.userDataStore.edit {preferences ->
            preferences[key] = value
        }
    }
    suspend fun getUserPhone():String {
        return context.userDataStore.data.map { preferences ->
            preferences[PHONE_KEY] ?: ""
        }.first() // 检查是否存在phoneKey
    }

}