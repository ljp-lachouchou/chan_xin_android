package com.software.jetpack.compose.chan_xin_android.ui

import android.content.Context
import com.alibaba.fastjson.JSON
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.lang.Exception

object AppConfig {


    private fun parseFile(context: Context, flieName: String): String {
        val assets = context.assets
        val inputStream = assets.open(flieName)
        val bd  = StringBuilder()
        try {
            inputStream.use {
                    stream->
                BufferedReader(InputStreamReader(stream)).use {
                        br->
                    var line:String? = null
                    while ((br.readLine().also { line = it }) != null) {
                        bd.append(line)
                    }
                }
            }
        }catch (e : Exception) {
            e.printStackTrace()
        }
        return bd.toString()
    }
}