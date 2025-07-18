package com.software.jetpack.compose.chan_xin_android.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
private const val DEFAULT_INTENSITY = 200
class VibratorHelper(private val context: Context) {
    // 简单振动（默认200ms）
    fun vibrate(duration: Long = 200) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            vibrateNewApi(duration)
        } else {
            @Suppress("DEPRECATION")
            vibrateOldApi(duration)
        }
    }

    // 检查设备是否支持调节震动强度
    fun isVibrationIntensitySupported(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.hasAmplitudeControl()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // API 26-30：假设支持，但实际可能不支持
            true
        } else {
            // API < 26：不支持
            false
        }
    }
    // 震动强度范围：1-255（255为最大强度）


    // 震动方法（带强度控制）
    @RequiresApi(Build.VERSION_CODES.O)
    fun vibrateWithIntensity(context: Context = AppGlobal.getAppContext(), duration: Long=200, intensity: Int = DEFAULT_INTENSITY) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            // 检查强度是否在有效范围内
            val validIntensity = intensity.coerceIn(1, 255)

            try {
                val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    vibratorManager.defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                }

                // 创建带强度的震动效果
                val effect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // API 29+：使用新的builder
                    VibrationEffect.createOneShot(
                        duration,
                        validIntensity
                        // 可选的预设效果
                    )
                } else {
                    // API 26-28：直接指定强度
                    VibrationEffect.createOneShot(duration, validIntensity)
                }

                vibrator.vibrate(effect)
            } catch (e: Exception) {
                // 处理不支持的情况（例如设备声称支持但实际不支持）
                e.printStackTrace()
                // 降级到默认强度
                @Suppress("DEPRECATION")
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                    context.getSystemService(Context.VIBRATOR_SERVICE)
                        ?.let { (it as Vibrator).vibrate(duration) }
                }
            }
        } else {
            // API < 26：不支持强度控制，使用默认强度
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE)
                ?.let { (it as Vibrator).vibrate(duration) }
        }
    }



    // 自定义振动模式（pattern: 暂停-振动-暂停... 的毫秒数组，amplitude: 强度数组）
    @RequiresApi(Build.VERSION_CODES.O)
    fun vibratePattern(
        pattern: LongArray,
        amplitudes: IntArray? = null,
        repeat: Int = -1 // -1表示不重复，0表示从头开始重复
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            vibratePatternNewApi(pattern, amplitudes, repeat)
        } else {
            @Suppress("DEPRECATION")
            vibratePatternOldApi(pattern, repeat)
        }
    }

    @SuppressLint("ServiceCast")
    @RequiresApi(Build.VERSION_CODES.S)
    private fun vibrateNewApi(duration: Long) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator
        val effect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(effect)
    }

    @SuppressLint("ServiceCast")
    @Suppress("DEPRECATION")
    private fun vibrateOldApi(duration: Long) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(duration)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun vibratePatternNewApi(
        pattern: LongArray,
        amplitudes: IntArray?,
        repeat: Int
    ) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator

        val effect = if (amplitudes != null) {
            VibrationEffect.createWaveform(pattern, amplitudes, repeat)
        } else {
            VibrationEffect.createWaveform(pattern, repeat)
        }

        vibrator.vibrate(effect)
    }

    @SuppressLint("ServiceCast")
    @Suppress("DEPRECATION")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun vibratePatternOldApi(pattern: LongArray, repeat: Int) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            val effect = VibrationEffect.createWaveform(pattern, repeat)
            vibrator.vibrate(effect)
        }
    }
}