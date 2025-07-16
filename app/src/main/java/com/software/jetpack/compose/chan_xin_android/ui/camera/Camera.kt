package com.software.jetpack.compose.chan_xin_android.ui.camera

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class Camera {
    companion object {
        fun saveImageToGallery(context:Context,bitmap:Bitmap):Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME,"chan_xin_image_${System.currentTimeMillis()}.jpg")
                    put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH,"Pictures/Saved Images")

                }
                val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)
                    ?: throw Exception("图片保存失败")
                uri.let {
                    resolver.openOutputStream(it).use { outputStream->
                        if (!bitmap.compress(Bitmap.CompressFormat.JPEG,95,outputStream!!)) {
                            throw IOException("无法保存图片")
                        }
                    }
                }
                return true
            }else {

                if (ContextCompat.checkSelfPermission(AppGlobal.getAppContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AppGlobal.getAppContext(),"没有授予保存图片的权限，保存失败",Toast.LENGTH_SHORT).show()
                    return false
                }
                try {
                    val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    if (!storageDir.exists()) {
                        storageDir.mkdirs()
                    }

                    val fileName = "chan_xin_image_${System.currentTimeMillis()}.jpg"
                    val imageFile = File(storageDir, fileName)

                    // 写入文件
                    val fos = FileOutputStream(imageFile)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
                    fos.flush()
                    fos.close()

                    // 插入到MediaStore（可选，增强兼容性）
                    MediaStore.Images.Media.insertImage(
                        context.contentResolver,
                        imageFile.absolutePath,
                        fileName,
                        "Image captured by chan_xin"
                    )

                    // 通知媒体库更新
                    context.sendBroadcast(
                        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile))
                    )

                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                    return false
                }
            }
        }
    }
}