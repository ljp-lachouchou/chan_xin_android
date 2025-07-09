package com.software.jetpack.compose.chan_xin_android.ui.base

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animate
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.coroutineScope
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.SurfaceColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.TextColor
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

val maxPullPx = 200.dp

val baseBoxModifier =Modifier
    .fillMaxSize()


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BaseBox(
            content: @Composable BoxScope.() -> Unit) {
    var pullDistancePx by remember { mutableFloatStateOf(0f) }
    var refreshOffsetPx by remember { mutableFloatStateOf(0f) }
    var holdPx by remember { mutableFloatStateOf(0f) }
    with(LocalDensity.current) {
        holdPx = maxPullPx.toPx()
        refreshOffsetPx = PullRefreshDefaults.RefreshingOffset.toPx()
    }
  val animate = remember {
      Animatable(0f)
  }
    LaunchedEffect(pullDistancePx) {
        animate.animateTo(targetValue = 0f)
    }
    Box(modifier = Modifier.fillMaxSize().background(Color.Transparent)
        .pointerInput(Unit) {
            detectDragGestures(onDragStart = {
                if (animate.isRunning) {

                }
            },
                onDrag = {
                        change, dragAmount ->
                    val deltaY = dragAmount.y
                    val newDistance = pullDistancePx + deltaY
                    pullDistancePx = newDistance.coerceIn(0f, holdPx)
                    change.consume() //消费手势
                })
        }
        ) {
        Box(modifier =Modifier
                .graphicsLayer {
            translationY = pullDistancePx
            Log.e("DELTA","translationY$translationY")

        }.fillMaxSize()) {
            content()
        }
    }

}
@Composable
fun BaseBoxExample() {
    BaseBox {
        Text("你好")
    }
}

@Composable
fun BaseText(text: String,
             modifier: Modifier = Modifier,
             color: Color = TextColor,
             fontSize: TextUnit = TextUnit.Unspecified,
             fontStyle: FontStyle? = null,
             fontWeight: FontWeight? = null,
             fontFamily: FontFamily? = null,
             letterSpacing: TextUnit = TextUnit.Unspecified,
             textDecoration: TextDecoration? = null,
             textAlign: TextAlign? = null,
             lineHeight: TextUnit = TextUnit.Unspecified,
             overflow: TextOverflow = TextOverflow.Clip,
             softWrap: Boolean = true,
             maxLines: Int = 1,
             minLines: Int = 1,
             onTextLayout: ((TextLayoutResult) -> Unit)? = null,
             style: TextStyle = LocalTextStyle.current) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = style
    )
}
@Composable
fun BaseButton(onClick: () -> Unit,
               modifier: Modifier =  Modifier.indication(indication = null,interactionSource = remember { MutableInteractionSource() }),
               enabled: Boolean = true,
               interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
               elevation: ButtonElevation? = null,
               shape: Shape = RoundedCornerShape(10.dp),
               border: BorderStroke? = null,
               colors: ButtonColors = ButtonDefaults.buttonColors(backgroundColor = IconGreen, contentColor = Color.White),
               contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
               content: @Composable RowScope.() -> Unit) {

    Button(onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    elevation = elevation,
    shape = shape,
    border = border,
    colors = colors,
    contentPadding=contentPadding,
    content = content)
}