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
import com.alibaba.sdk.android.oss.signer.SignVersion


object Oss {
    fun initOss() {
        val accessKey = "LTAI5tGEXmyuqj8BQC1oc7QZ"
        val secretKey = "AGScZRu3cbFaX9Df4wOz3PRu6jOqFm"
        val endpoint = "oss-cn-beijing.aliyuncs.com"
    }
    fun uploadFile(fileName:String,uri: Uri?) :String{

        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        val endpoint = "oss-cn-beijing.aliyuncs.com"

// 从STS服务获取的临时访问密钥（AccessKey ID和AccessKey Secret）。
        val accessKeyId = "STS.NZqvvcd2U55pXUd61vgYDSUhT"
        val accessKeySecret = "Ep6zgF1Rk37yjEEynC7fQjgUqdHkg7A42DeuLD7CoJ9A"

// 从STS服务获取的安全令牌（SecurityToken）。
        val securityToken = "CAISygJ1q6Ft5B2yfSjIr5nEPczXie10gvebWnPV0jEja9ZovJDDtjz2IHhMe3VrCegas/o+mG5X6PYflr54S5ZDAFzFa/F36pls/Bi6Yo3HncWw4OTVfhk0jjLBZSTg1er+Ps/bLrqECNrPBnnAkihsu8iYERypQ12iN7CQlJdjda55dwKkbD1Adrw0T0kY3618D3bKMuu3ORPHm3fZCFES2jBxkmRi86+ysKb+g1j89ASmkLVL/9irfcn7P5k8YchFPo3rjLAsRM3oyzVN7hVGzqBygZFf9C3P1tPnWAEJvEndY7KIr4M+cFEmOPhnAc5PrPH60O15vPwCFW5pPsXmVws8clM8JOjIqKOscIsihsch31FsFV75c8FdF1gEgvI14EhhQCdgUfRPhPG+x1LiZJ80BGGplq5E7GXHaeyxtItStz4dUApVu4fiTzAagAGwpOb2RUwDcGcElz/yBQAXLtHJ1UyMQhXFlEelOP5fvL3fNFvLSJkTR/WgDddpcgHsPJ7mir3i1BIh+JQjNfKrO26ZkYyJw3nmb6sCMGisf4QzIm7YVXwvUHaLsMjbXaHBhRR9S7wpGa6HW3qQMWxT231J7pw4Y31NVhMv9TEMlyAA"

// yourEndpoint填写Bucket所在地域。以华东1（杭州）为例，region填写为cn-hangzhou。
        val region = "cn-beijing"

        val credentialProvider: OSSCredentialProvider =
            OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, securityToken)
        val config = ClientConfiguration()

// 创建OSSClient实例。
        val oss = OSSClient(AppGlobal.getAppContext(), endpoint, credentialProvider)
        oss.setRegion(region)

        // 构造上传请求。
// 依次填写Bucket名称（例如examplebucket）、Object完整路径（例如exampledir/exampleobject.txt）和本地文件完整路径（例如/storage/emulated/0/oss/examplefile.txt）。
// Object完整路径中不能包含Bucket名称。
        val put = PutObjectRequest(
            "chan-xin",
            "chan_xin/image/$fileName",
            uri
        )


        // 设置文件元数据为可选操作。
        val metadata = ObjectMetadata()


// metadata.setContentType("application/octet-stream"); // 设置content-type。
// metadata.setContentMD5(BinaryUtil.calculateBase64Md5(uploadFilePath)); // 校验MD5。
// 设置Object的访问权限为私有。
        metadata.setHeader("x-oss-object-acl", "public-read")

// 设置Object的存储类型为标准存储。
        metadata.setHeader("x-oss-storage-class", "Standard")


// 设置禁止覆盖同名Object。
// metadata.setHeader("x-oss-forbid-overwrite", "true");
// 指定Object的对象标签，可同时设置多个标签。
// metadata.setHeader("x-oss-tagging", "a:1");
// 指定OSS创建目标Object时使用的服务器端加密算法 。
// metadata.setHeader("x-oss-server-side-encryption", "AES256");
// 表示KMS托管的用户主密钥，该参数仅在x-oss-server-side-encryption为KMS时有效。
// metadata.setHeader("x-oss-server-side-encryption-key-id", "9468da86-3509-4f8d-a61e-6eab1eac****");
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