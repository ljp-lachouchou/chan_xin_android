package com.software.jetpack.compose.chan_xin_android.util

import android.net.Uri
import android.util.Log
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.ObjectMetadata
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import java.io.BufferedReader
import java.io.InputStreamReader


object Oss {
    suspend fun uploadFile(fileName:String,uri: Uri?) :String{
        var endpoint = ""
        var accessKeyId = ""
        var accessKeySecret = ""

        var securityToken = ""
        BufferedReader(InputStreamReader(AppGlobal.getAppContext().assets.open("oss.txt"))).use { br ->
            var line:String? = null
            var count = 0
            while ((br.readLine().also { line = it }) != null) {
                when(count) {
                    0-> {endpoint = line!!}
                    1->{ accessKeyId = line!!}
                    2-> {accessKeySecret = line!!}
                    3-> {securityToken = line!!}
                }
                count++
            }
        }


        val region = "cn-beijing"

        val credentialProvider: OSSCredentialProvider =
            OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, securityToken)
        val config = ClientConfiguration()

        val oss = OSSClient(AppGlobal.getAppContext(), endpoint, credentialProvider)
        oss.setRegion(region)

        val put = PutObjectRequest(
            "chan-xin",
            "chan_xin/image/$fileName",
            uri
        )
        val metadata = ObjectMetadata()

        metadata.setHeader("x-oss-object-acl", "public-read")
        metadata.setHeader("x-oss-storage-class", "Standard")
        put.metadata = metadata

        try {
            //oss.putObject(put)
            val putResult: PutObjectResult = oss.putObject(put)

            Log.d("PutObject", "UploadSuccess")
            Log.d("ETag", if(putResult.eTag != null) putResult.eTag else "null")
            Log.d("RequestId", if(putResult.requestId != null) putResult.requestId else "null")
            return "https://chan-xin.oss-cn-beijing.aliyuncs.com/chan_xin/image/$fileName"
        } catch (e: ClientException) {
            // 客户端异常，例如网络异常等。
            Log.e("网络异常",e.toString())
            e.printStackTrace()
            return ""
        } catch (e: ServiceException) {
            // 服务端异常。
            Log.e("RequestId", e.requestId)
            Log.e("ErrorCode", e.errorCode)
            Log.e("HostId", e.hostId)
            Log.e("RawMessage", e.rawMessage)
            return ""
        }
    }

}