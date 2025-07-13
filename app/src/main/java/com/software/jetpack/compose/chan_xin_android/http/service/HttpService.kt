package com.software.jetpack.compose.chan_xin_android.http.service

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object HttpService {
    private val okHttpClient = OkHttpClient
        .Builder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
    private var retrofit:Retrofit? = null
    fun getService():ApiService {
        if (retrofit == null) {
            synchronized(HttpService::class.java) {
                if (retrofit == null) {
                    retrofit = Retrofit
                        .Builder()
                        .baseUrl("http://114.215.194.88:9080/")
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                        .addCallAdapterFactory(CoroutineCallAdapterFactory())
                        .build()
                }
            }
        }
        return retrofit!!.create(ApiService::class.java)
    }

}