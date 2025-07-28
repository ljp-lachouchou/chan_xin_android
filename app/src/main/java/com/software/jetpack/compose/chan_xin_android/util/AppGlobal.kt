package com.software.jetpack.compose.chan_xin_android.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.software.jetpack.compose.chan_xin_android.util.PreferencesFileName.PHONE_KEY
import com.software.jetpack.compose.chan_xin_android.util.PreferencesFileName.USERS_FILE
import com.software.jetpack.compose.chan_xin_android.util.PreferencesFileName.USER_COVER_FILE_PATH
import com.software.jetpack.compose.chan_xin_android.util.PreferencesFileName.USER_TOKEN
import com.software.jetpack.compose.chan_xin_android.util.PreferencesFileName.USER_TOKEN_EXP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

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
    suspend fun getBitmapFromUrl(src:String):Bitmap? {
        try {
            val url = URL(src)
            return withContext(Dispatchers.IO) {
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()
                connection.inputStream.use {`is`->
                    BitmapFactory.decodeStream(`is`)
                }
            }

        }catch (e:Exception){
            return null
        }
    }
    suspend fun getBitmapFromFilePath(path:String):Bitmap? {
        return try {
            withContext(Dispatchers.IO) {
                BitmapFactory.decodeFile(path)
            }
        }catch (e:Exception) {
            null
        }
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
    fun isNetworkValid():Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?:return false
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }

    suspend fun saveBitmapToFile(src:String,fileName:String):String {
        val file = File(context.filesDir,fileName)
        val url = URL(src)
        val connection = withContext(Dispatchers.IO) {
            url.openConnection()
        } as HttpURLConnection
        withContext(Dispatchers.IO) {
            connection.connect()
            // 检查响应状态
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                Log.e("DownloadError", "HTTP 状态码: ${connection.responseCode}")
                return@withContext null
            }

            // 下载并保存文件
            connection.inputStream.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
        }
        return file.absolutePath
    }
    suspend fun getFilePath():String {
        return context.userDataStore.data.map { preferences->
            preferences[USER_COVER_FILE_PATH] ?:""
        }.first()
    }

}