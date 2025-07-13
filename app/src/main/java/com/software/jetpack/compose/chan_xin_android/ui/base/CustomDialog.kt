package com.software.jetpack.compose.chan_xin_android.ui.base


import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen

@Composable
fun LoadingDialog(isShowing:Boolean,circleColor:Color = IconGreen,circleSize: Int = 50, backgroundColor: Color = Color.Black.copy(alpha = 0.3f)) {
    if (isShowing) {
        Dialog(onDismissRequest = {},properties = DialogProperties(dismissOnBackPress = false,dismissOnClickOutside = false)) {
            Box(modifier = Modifier.size((circleSize*3).dp).background(backgroundColor), contentAlignment = Alignment.Center) {
                val infiniteAnim = rememberInfiniteTransition()
                val rotation by infiniteAnim.animateFloat(initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = InfiniteRepeatableSpec(animation = tween(1000, easing = LinearEasing))
                )
                Surface(shape = CircleShape, color = Color.White,modifier = Modifier
                    .size((circleSize * 2).dp)) {
                    CircularProgressIndicator(color = circleColor,strokeWidth = 4.dp, modifier = Modifier.size(circleSize.dp).rotate(rotation).align(Alignment.Center))
                }
            }
        }
    }
}