package com.software.jetpack.compose.chan_xin_android.lifecycle

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.media.audiofx.EnvironmentalReverb.Settings
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.software.jetpack.compose.chan_xin_android.MainActivity
import com.software.jetpack.compose.chan_xin_android.ui.activity.AppTopBar
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.util.Location
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import kotlinx.coroutines.launch
import java.lang.RuntimeException

class MainActivityObserver : DefaultLifecycleObserver, AMapLocationListener {
    private var mLocationClient: AMapLocationClient? = null
    private var mLocationOption: AMapLocationClientOption? = null
    private val logTag = javaClass.simpleName
    private var locationLauncher: ActivityResultLauncher<String>? = null // 允许为null，避免未初始化使用

    // 记录当前的Activity（用于权限请求和定位）
    private var activity: ComponentActivity? = null

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        // 安全转换为ComponentActivity，避免类型转换异常
        if (owner is ComponentActivity) {

            this.activity = owner
            // 注册权限请求Launcher（此时owner处于CREATED状态，符合注册时机要求）
            locationLauncher = owner.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                Log.e("MainL","$isGranted")
                Log.e("$logTag 申请结果", "$isGranted")
                if (isGranted) {
                    // 权限授予后，检查定位服务是否开启
                    checkLocationServiceEnabled(owner)
                } else {
                    // 权限被拒绝，判断是否为"不再询问"
                    if (!owner.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // 用户勾选了"不再询问"，引导到应用设置页面
                        showPermissionSettingDialog(owner)
                    } else {
                        // 仅拒绝，未勾选"不再询问"，提示用户需要权限
                        Toast.makeText(owner, "需要定位权限才能使用定位功能", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            locationLauncher?.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            // 初始化定位参数
            initLocation()
        } else {
            Log.e(logTag, "LifecycleOwner is not a ComponentActivity, cannot register launcher")
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        val activity = this.activity ?: return
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startLocation()
        } else {
            locationLauncher?.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        // 暂停时停止定位，节省电量
        stopLocation()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        // 销毁时释放资源
        mLocationClient?.stopLocation()
        mLocationClient?.onDestroy()
        mLocationClient = null
        locationLauncher = null
        activity = null
    }

    private fun startLocation() {
        if (mLocationClient != null) {
            mLocationClient!!.startLocation()
        }
    }

    private fun stopLocation() {
        if (mLocationClient != null) {
            mLocationClient!!.stopLocation()
        }
    }

    private fun initLocation() {
        try {
            val context = AppGlobal.getAppContext()
            mLocationClient = AMapLocationClient(context)
            mLocationClient?.setLocationListener(this)
            mLocationOption = AMapLocationClientOption().apply {
                locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy // 高精度模式
                isOnceLocationLatest = true // 获取最近3s内精度最高的定位结果
                isNeedAddress = true // 返回地址信息
                httpTimeOut = 6000 // 超时时间6秒
            }
            mLocationClient?.setLocationOption(mLocationOption)
        } catch (e: Exception) {
            Log.e(logTag, "初始化定位失败", e)
            throw RuntimeException("初始化定位失败: ${e.message}")
        }
    }

    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        if (aMapLocation == null) {
            Toast.makeText(AppGlobal.getAppContext(), "定位失败", Toast.LENGTH_SHORT).show()
            return
        }
        if (aMapLocation.errorCode == 0) {
            val latitude = aMapLocation.latitude
            val longitude = aMapLocation.longitude
            val address = aMapLocation.address
            val city = aMapLocation.city
            val province = aMapLocation.province
            Location.city = city
            Location.province = province
            Location.address = address
            Log.e(logTag, "定位成功: 纬度=$latitude, 经度=$longitude, 地址=$address, 城市=$city, 省份=$province")
        } else {
            // 定位失败，打印错误信息
            Log.e(logTag, "定位失败: 错误码=${aMapLocation.errorCode}, 错误信息=${aMapLocation.errorInfo}")
        }
    }


    private fun checkLocationServiceEnabled(context: Context) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 判断GPS或网络定位是否开启
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (isGpsEnabled || isNetworkEnabled) {
            // 定位服务已开启，启动定位
            startLocation()
        } else {
            // 定位服务未开启，引导用户到系统设置页面开启
            showLocationServiceDialog(context)
        }
    }

    // 显示开启定位服务的对话框
    private fun showLocationServiceDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("定位服务未开启")
            .setMessage("请在设置中开启位置信息，以便获取当前位置")
            .setPositiveButton("去设置") { _, _ ->
                // 跳转到系统位置信息设置页面
                context.startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("取消") { _, _ ->
                // 用户取消，定位功能无法使用
                Toast.makeText(context, "定位服务未开启，无法获取位置", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
    // 显示权限设置对话框
    private fun showPermissionSettingDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("权限申请")
            .setMessage("定位权限已被拒绝，请在设置中开启")
            .setPositiveButton("去设置") { _, _ ->
                // 跳转到应用设置页面
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            }
            .setNegativeButton("取消", null)
            .show()
    }
}
