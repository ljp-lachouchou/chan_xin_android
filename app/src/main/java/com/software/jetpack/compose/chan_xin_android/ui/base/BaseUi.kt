package com.software.jetpack.compose.chan_xin_android.ui.base

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.MotionEvent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.MotionDurationScale
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultFontSize
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserPadding
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserScreenItemDp
import com.software.jetpack.compose.chan_xin_android.defaultValue.defaultLittleSize
import com.software.jetpack.compose.chan_xin_android.defaultValue.defaultPlaceholderText
import com.software.jetpack.compose.chan_xin_android.ext.toComposeColor
import com.software.jetpack.compose.chan_xin_android.ui.activity.AppTopBarBack
import com.software.jetpack.compose.chan_xin_android.ui.activity.Wrapper
import com.software.jetpack.compose.chan_xin_android.ui.theme.ChatGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.LittleTextColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.PlaceholderColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.PressedLittleTextColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.RightArrowColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.TextColor
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.abs

val maxPullPx = 200.dp

val baseBoxModifier =Modifier
    .fillMaxSize()


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BaseBox(modifier: Modifier = Modifier,enabledFoot:Boolean = true,
            content: @Composable BoxScope.() -> Unit) {
    var pullDistancePx by remember { mutableFloatStateOf(0f) }
    var refreshOffsetPx by remember { mutableFloatStateOf(0f) }
    var holdPx by remember { mutableFloatStateOf(0f) }

    val scope = rememberCoroutineScope()
    with(LocalDensity.current) {
        holdPx = maxPullPx.toPx()
        refreshOffsetPx = PullRefreshDefaults.RefreshingOffset.toPx()
    }
    val animate = remember {
        Animatable(0f)
    }

    Box(modifier = modifier
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = {
                if (animate.isRunning) {
                    scope.launch {
                        animate.stop()
                    }
                }
            },
                onDrag = {
                        change, dragAmount ->
                    val deltaY = dragAmount.y
                    val newDistance = pullDistancePx + deltaY
                    pullDistancePx = newDistance.coerceIn(if (enabledFoot) -holdPx else 0f, holdPx)
                    change.consume() //消费手势
                },
                onDragEnd = {
                    Log.e("onDragEnd","start")
                    pullDistancePx = 0f
                    scope.launch {
                        Log.e("onDragEnd","Run")
                        animate.animateTo(0f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessHigh))
                    }
                }
            )
        }
        ) {
        Box(modifier =Modifier
            .graphicsLayer {
                translationY = pullDistancePx
                Log.e("DELTA", "translationY$translationY")
            }
            .fillMaxSize()) {
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
@Composable
fun LittleText(text: String,
               event:()->Unit = {},
               modifier: Modifier = Modifier,
               fontSize: TextUnit = defaultLittleSize,
               fontStyle: FontStyle? = null,
               fontWeight: FontWeight? = null,
               fontFamily: FontFamily? = null,
               letterSpacing: TextUnit = TextUnit.Unspecified,
               textDecoration: TextDecoration? = null,
               textAlign: TextAlign? = null,
               lineHeight: TextUnit = TextUnit.Unspecified,
               overflow: TextOverflow = TextOverflow.Clip,
               softWrap: Boolean = true,
               maxLines: Int = Int.MAX_VALUE,
               minLines: Int = 1,
               onTextLayout: ((TextLayoutResult) -> Unit)? = null,
               style: TextStyle = LocalTextStyle.current) {
    val interactionSource = remember { MutableInteractionSource() }
    val textColor  = when (interactionSource.collectIsPressedAsState().value){
        true -> PressedLittleTextColor
        else -> LittleTextColor
    }
    Text(
        text = text,
        modifier = modifier.clickable(indication = null, interactionSource = interactionSource) { event() },
        color = textColor,
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
fun BaseTransBetweenScreens(firstScreen: @Composable() (AnimatedVisibilityScope.() -> Unit)?,secondScreen: @Composable() AnimatedVisibilityScope.() -> Unit) {
    val stateCover = remember {
        MutableTransitionState(true)
    }
    val stateScreen = remember {
        if (firstScreen != null) MutableTransitionState(false) else MutableTransitionState(true)
    }
    LaunchedEffect(Unit) {
        delay(2000)
        if (firstScreen != null) {
            stateCover.targetState = false
            stateScreen.targetState = true
            return@LaunchedEffect
        }
    }
   if (firstScreen != null) {
       AnimatedVisibility (visibleState = stateCover) {
           firstScreen()
       }
   }
    if (firstScreen == null) {
        AnimatedVisibility(visibleState = stateScreen, enter = expandHorizontally()) {
            secondScreen()
        }
    }else {
        AnimatedVisibility(visibleState = stateScreen) {
            secondScreen()
        }
    }

}
@Composable
fun BaseTextField(value: String,
                  onValueChange: (String) -> Unit,
                  modifier: Modifier = Modifier,
                  enabled: Boolean = true,
                  readOnly: Boolean = false,
                  textStyle: TextStyle = androidx.compose.material3.LocalTextStyle.current,
                  label: @Composable (() -> Unit)? = null,
                  placeholder: String,
                  leadingIcon: @Composable (() -> Unit)? = null,
                  trailingIcon: @Composable (() -> Unit)? = null,
                  prefix: @Composable (() -> Unit)? = null,
                  suffix: @Composable (() -> Unit)? = null,
                  supportingText: @Composable (() -> Unit)? = null,
                  isError: Boolean = false,
                  visualTransformation: VisualTransformation = VisualTransformation.None,
                  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
                  keyboardActions: KeyboardActions = KeyboardActions.Default,
                  singleLine: Boolean = false,
                  maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
                  minLines: Int = 1,
                  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
                  shape: Shape = TextFieldDefaults.shape,unfocusedContainerColor:Color = Color.Transparent,focusedContainerColor:Color = Color.Transparent) {
    TextField(
        colors = TextFieldDefaults.colors(
            cursorColor = IconGreen,
            unfocusedContainerColor = unfocusedContainerColor, focusedContainerColor = focusedContainerColor,
            focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
        ),
        value = value,
        onValueChange = onValueChange,
        leadingIcon = leadingIcon,
        placeholder = { Text(placeholder, color = PlaceholderColor) },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape
    )
}
@Composable
fun BaseTextField(value: String,
                  onValueChange: (String) -> Unit,
                  modifier: Modifier = Modifier,
                  enabled: Boolean = true,
                  readOnly: Boolean = false,
                  textStyle: TextStyle = androidx.compose.material3.LocalTextStyle.current,
                  label: @Composable (() -> Unit)? = null,
                  placeholder: String,
                  leadingIcon: @Composable (() -> Unit)? = null,
                  trailingIcon: @Composable (() -> Unit)? = null,
                  prefix: @Composable (() -> Unit)? = null,
                  suffix: @Composable (() -> Unit)? = null,
                  supportingText: @Composable (() -> Unit)? = null,
                  isError: Boolean = false,
                  visualTransformation: VisualTransformation = VisualTransformation.None,
                  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
                  keyboardActions: KeyboardActions = KeyboardActions.Default,
                  singleLine: Boolean = false,
                  maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
                  minLines: Int = 1,
                  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
                  shape: Shape = TextFieldDefaults.shape) {
    TextField(
        colors = TextFieldDefaults.colors(
            cursorColor = IconGreen,
            unfocusedContainerColor = Color.Transparent, focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
        ),
        value = value,
        onValueChange = onValueChange,
        leadingIcon = leadingIcon,
        placeholder = { Text(placeholder, color = PlaceholderColor) },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape
    )
}
@Composable
fun Triangle(
    modifier: Modifier = Modifier,
    color: Color = Color.Blue,
    isFilled: Boolean = true, // 是否填充
    size: Int = 40 // 三角形大小
) {
    Canvas(modifier = modifier.size(size.dp)) {
        val path = Path().apply {
            // 定义三角形的三个顶点
            moveTo(size / 2f, 0f) // 顶部顶点
            lineTo(size.toFloat(), size.toFloat()) // 右下角
            lineTo(0f, size.toFloat()) // 左下角
            close() // 闭合路径形成三角形
        }

        // 根据是否填充选择绘制方式
        if (isFilled) {
            drawPath(path, color) // 填充颜色
        } else {
            drawPath(path, color, style = Stroke(width = 2f)) // 仅描边
        }
    }
}


enum class BubbleDirection { LEFT, RIGHT }

@Composable
fun ChatBubble(
    message: String,
    direction: BubbleDirection = BubbleDirection.LEFT,
    modifier: Modifier = Modifier,
    maxWidthPercentage: Float = 0.7f,
    tailWidth: Dp = 10.dp,
    tailHeight: Dp = 12.dp,
    bubbleColor: Color = ChatGreen,
    textStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        color = if (direction == BubbleDirection.LEFT) Color.Black else Color.Black
    )
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    Box(
        modifier = modifier
            .widthIn(
                max =screenWidthDp * maxWidthPercentage
            )
    ) {
        // 绘制气泡背景
        Canvas(modifier = Modifier.matchParentSize()) {
            val width = size.width
            val height = size.height

            val bubblePath = Path().apply {
               if (direction == BubbleDirection.RIGHT) {
                   moveTo(tailWidth.toPx(), 0f)
                   // 右侧边
                   lineTo(width - tailWidth.toPx() / 2, 0f)
                   lineTo(width - tailWidth.toPx() / 2, height)
                   moveTo(width - tailWidth.toPx() / 2, height)
                   lineTo(tailWidth.toPx(), height)
                   lineTo(tailWidth.toPx(),0f)
                   moveTo(width - tailWidth.toPx() / 2, (height - tailHeight.toPx())  / 2)
                   lineTo(width+tailWidth.toPx()/ 2,height / 2)
                   lineTo(width - tailWidth.toPx() / 2, (height + tailHeight.toPx())  / 2)
               }else {
                   moveTo(tailWidth.toPx(),0f)
                   lineTo(width - tailWidth.toPx() / 2, 0f)
                   lineTo(width - tailWidth.toPx() / 2, height)
                   moveTo(width - tailWidth.toPx() / 2, height)
                   lineTo(tailWidth.toPx(), height)
                   lineTo(tailWidth.toPx(),0f)
                   moveTo(0f,height / 2)
                   lineTo(tailWidth.toPx(),(height - tailHeight.toPx()) / 2)
                   lineTo(tailWidth.toPx(),(height + tailHeight.toPx()) / 2)
               }
            }

            drawPath(path = bubblePath, color = bubbleColor)
        }

        // 气泡内的文本内容
        Text(
            text = message,
            style = textStyle,
            modifier = Modifier
                .padding(
                    start = 12.dp + if (direction == BubbleDirection.LEFT) tailWidth else 0.dp,
                    end = 12.dp + if (direction == BubbleDirection.RIGHT) tailWidth else 0.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
                .align(Alignment.CenterStart)
        )
    }
}

@Composable
fun BaseScreenItem(
    preContent: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
    tailContent: (@Composable () -> Unit)? = null,
    backgroundColor:Color = Color.White,
    indication: Indication?= LocalIndication.current,
    interactionSource: MutableInteractionSource= remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(DefaultUserScreenItemDp)
        .background(backgroundColor)
        .clickable(indication = indication, interactionSource = interactionSource) { onClick();Log.e("iiii", "iiiii") }
        .padding(horizontal = 15.dp), contentAlignment = Alignment.Center) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            if (preContent != null) {
                Wrapper {
                    preContent()
                }
            }
            Spacer(modifier = Modifier.width(DefaultUserPadding))
            Wrapper {
                content()
            }
            Spacer(modifier = Modifier.weight(1f))
            if (tailContent != null) {
                Wrapper{
                    tailContent()
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ChatBubblePreview() {
    Log.e("ppp222","ppp")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 左侧气泡（对方）
        ChatBubble(
            message = "你好！",
            direction = BubbleDirection.LEFT
        )

        // 右侧气泡（自己）
        ChatBubble(
            message = "这是一个右侧气泡，通常用于自己发送的消息。",
            direction = BubbleDirection.RIGHT
        )

        // 长文本气泡
        ChatBubble(
            message = "这是一个很长的消息，测试气泡的自动换行功能。Jetpack Compose 是 Android 官方推荐的现代 UI 工具包，用它开发界面非常高效，代码也更简洁。aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab",
            direction = BubbleDirection.LEFT
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RefreshLazyColumn(
    modifier: Modifier = Modifier, state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true, isRefresh:Boolean = false,refreshEvent:()->Unit = {},content: LazyListScope.() -> Unit
) {
    var pullDistancePx by remember { mutableFloatStateOf(0f) }
    var refreshOffsetPx by remember { mutableFloatStateOf(0f) }
    var holdPx by remember { mutableFloatStateOf(0f) }

    val scope = rememberCoroutineScope()
    with(LocalDensity.current) {
        holdPx = maxPullPx.toPx()
        refreshOffsetPx = PullRefreshDefaults.RefreshingOffset.toPx()
    }
    val animate = remember {
        Animatable(0f)
    }

    Box(
        modifier = modifier
            .pullRefresh(onPull = { delta ->
                val newDistance = pullDistancePx + delta
                pullDistancePx = newDistance.coerceIn( 0f, holdPx)
                pullDistancePx
            }, onRelease = {
                if (isRefresh) refreshEvent()
                pullDistancePx = 0f
                scope.launch {
                    Log.e("onDragEnd", "Run")
                    animate.animateTo(
                        0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessHigh
                        )
                    )
                }
                it
            })
    ) {
        LazyColumn(
            modifier = modifier.graphicsLayer { translationY=pullDistancePx },
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled
        ) {
            content()
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CanLookImage(data:Any,isSelected:Boolean = false,onChange:()->Unit,content:@Composable ()->Unit) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val verticalThreshold = screenHeight * 0.3f
    if (!isSelected) {
        content()
    } else {
        BackHandler {
            onChange()
        }
        Scaffold(topBar = {
            AppTopBarBack(tint = Color.White, color = Color.Black,onChange = onChange)
        }) { _->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clipToBounds(),
                contentAlignment = Alignment.Center
            ) {
                ImageViewer(data) {
                    if (it > verticalThreshold.value) onChange()

                }
            }
        }
    }
}

@Composable
fun ImageViewer(data:Any,onVerticalDrag:(Float)->Unit) {
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var isGestureInProgress by remember { mutableStateOf(false) }
    var actualOffset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, panChange, _ ->
        if (!isGestureInProgress) isGestureInProgress = true
        scale = (scale * zoomChange).coerceIn(0.5f,5f)
        offset = Offset(
            (offset.x + panChange.x).coerceAtLeast(getMinOffsetX(imageSize, scale))
                .coerceAtMost(
                    getMaxOffsetX(imageSize, scale)
                ),
            (offset.y + panChange.y).coerceAtLeast(getMinOffsetY(imageSize, scale))
                .coerceAtMost(
                    getMaxOffsetY(imageSize, scale)
                )
        )
        actualOffset = Offset(actualOffset.x+panChange.x,actualOffset.y+panChange.y)
    }

    val scaleAnimate by animateFloatAsState(targetValue = scale)


    AsyncImage(
        model = ImageRequest.Builder(AppGlobal.getAppContext()).data(data).build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .graphicsLayer {
                scaleX = scaleAnimate
                scaleY = scaleAnimate
                translationY = offset.y
                translationX = offset.x
            }
            .transformable(state)
            .onGloballyPositioned { coordinates ->
                imageSize = coordinates.size
            }
    )
    if (!isGestureInProgress && scale < 1f) {
        scale = 1f
    }
    if (!isGestureInProgress && scale >= 1f) {
        onVerticalDrag(abs(actualOffset.y-offset.y))
    }
    if (state.isTransformInProgress.not() && isGestureInProgress) {
        isGestureInProgress = false
    }
}
private fun getMinOffsetX(imageSize: IntSize, scale: Float): Float {
    if (imageSize.width == 0) return 0f
    val scaledWidth = imageSize.width * scale
    return if (scaledWidth <= imageSize.width) {
        0f // 如果图片缩小到小于等于原始宽度，不允许向左偏移
    } else {
        -(scaledWidth-imageSize.width) / 2 // 限制最大向左偏移
    }
}

private fun getMaxOffsetX(imageSize: IntSize, scale: Float): Float {
    if (imageSize.width == 0) return 0f
    val scaledWidth = imageSize.width * scale
    return if (scaledWidth <= imageSize.width) {
        0f // 如果图片缩小到小于等于原始宽度，不允许向右偏移
    } else {
        (scaledWidth-imageSize.width) / 2 // 限制最大向右偏移
    }
}

private fun getMinOffsetY(imageSize: IntSize, scale: Float): Float {
    if (imageSize.height == 0) return 0f
    val scaledHeight = imageSize.height * scale
    return if (scaledHeight <= imageSize.height) {
        0f // 如果图片缩小到小于等于原始高度，不允许向上偏移
    } else {
        -(scaledHeight - imageSize.height) / 2 // 限制最大向上偏移
    }
}

private fun getMaxOffsetY(imageSize: IntSize, scale: Float): Float {
    if (imageSize.height == 0) return 0f
    val scaledHeight = imageSize.height * scale
    return if (scaledHeight <= imageSize.height) {
        0f // 如果图片缩小到小于等于原始高度，不允许向下偏移
    } else {
        (scaledHeight - imageSize.height) / 2 // 限制最大向下偏移
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun selectCharRows(chars:String,surfaceUnselectColor:Color = Color.Gray.copy(0.1f),surfaceSelectColor:Color = IconGreen):List<Boolean> {
    val booleanArray = remember { mutableStateListOf<Boolean>() }.apply {
        repeat(chars.length){add(false)}
    }
    FlowRow(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp)) {
        chars.forEachIndexed { i,char->
            val (checked, onCheckedChange) = remember { mutableStateOf(false) }
            SelectCharItem(checked,char.toString(),surfaceSelectColor=surfaceSelectColor, surfaceUnselectColor = surfaceUnselectColor) {
                booleanArray[i] = !checked
                onCheckedChange(!checked)
            }
            if (i<chars.length - 1) {
                Spacer(modifier = Modifier.width(20.dp))
            }
        }
    }
    return booleanArray
}

@Composable
fun SelectCharItem(
    selected: Boolean = false,
    dir: String,
    surfaceSize: Dp = 20.dp,
    textSize: Float = 12f,
    surfaceUnselectColor: Color = Color.Gray.copy(0.1f),
    surfaceSelectColor: Color = IconGreen,
    onClick: () -> Unit
) {
    Surface(color = if (selected) surfaceSelectColor else surfaceUnselectColor,
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .size(surfaceSize)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick() }) {
        BaseText(dir, fontSize = textSize.sp, textAlign = TextAlign.Center)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = androidx.compose.material3.LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.colors()
) {
    // If color is not provided via the text style, use content color as a default
    val textColor = textStyle.color
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    BasicTextField(
        value = value,
        modifier = modifier
            .defaultMinSize(
                minWidth = TextFieldDefaults.MinWidth,
                minHeight = TextFieldDefaults.MinHeight
            ),
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(colors.cursorColor),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        decorationBox = @Composable { innerTextField ->
            // places leading icon, text field with label and placeholder, trailing icon
            TextFieldDefaults.DecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = placeholder,
                label = label,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                prefix = prefix,
                suffix = suffix,
                supportingText = supportingText,
                shape = shape,
                singleLine = singleLine,
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = colors,
                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 16.dp)
            )
        }
    )
}

@Composable
fun GeneratePaletteFromImage(data: Any,onGenerated: (Palette) -> Unit) {
    val resources = LocalContext.current.resources
    LaunchedEffect(data) {
        val bitmap = when(data) {
            is Int -> {
                BitmapFactory.decodeResource(resources,data)
            }
            else -> {
                val url = data as String
                AppGlobal.getBitmapFromFilePath(url) ?: BitmapFactory.decodeResource(resources,R.drawable.default_cover)
            }
        }
        Palette.from(bitmap).generate {
            it?.let { onGenerated(it) }
        }
    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LazyColumnWithCover(data: Any,nickname:String,displayAvatar:Any,listState:LazyListState,onChangeCover:()->Unit,onEnterFriendInfoDetail:()->Unit,content:LazyListScope.() -> Unit) {
    var dominantColor by remember { mutableStateOf(listOf(Color.Cyan.copy(0.5f),Color.Cyan.copy(0.4f))) }
    var dominantColorReverse by remember { mutableStateOf(listOf(Color.Cyan.copy(0.5f),Color.Cyan.copy(0.4f))) }
    var alpha by remember { mutableFloatStateOf(1f) }
    var paletteHeight by remember { mutableFloatStateOf(0f) }
    var isAnimating by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val paletteHeightAnimate by animateFloatAsState(paletteHeight, animationSpec = tween(400, easing = FastOutSlowInEasing), finishedListener = {
        scope.launch {
            delay(100)
            isAnimating = false
        }
    })
    var changeCoverPosition by remember { mutableStateOf(Offset.Zero) }
    val alphaAnimate by animateFloatAsState(alpha, animationSpec = tween(400, easing = FastOutSlowInEasing), finishedListener = {
        scope.launch {
            delay(100)
            isAnimating = false
        }
    })
    GeneratePaletteFromImage(data) {palette: Palette ->
        dominantColor = palette.swatches.map { it.rgb.toComposeColor() }.sortedBy { it.value }.reversed().take(if (palette.swatches.size > 3) 3 else palette.swatches.size)
        dominantColorReverse = dominantColor.reversed()
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInteropFilter { event ->
            when (event.action) {
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (alpha == 0f) {
                        if (!isAnimating) {
                            isAnimating = true
                            paletteHeight = if (paletteHeight == 0f) 85f else 0f
                            alpha = if (alpha == 0f) 1f else 0f
                        }
                    }
                    alpha == 0f && event.rawX !in (changeCoverPosition.x-50..changeCoverPosition.x+50)&& event.rawY !in (changeCoverPosition.y-50..changeCoverPosition.y+50)
                }

                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    alpha == 0f && event.rawX !in (changeCoverPosition.x-50..changeCoverPosition.x+50)&& event.rawY !in (changeCoverPosition.y-50..changeCoverPosition.y+50)
                }

                else -> false
            }
        }) {
        LazyColumn (state = listState){
            item {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(paletteHeightAnimate.dp)
                            .background(brush = Brush.verticalGradient(dominantColor))
                    )
                    AsyncImage(
                        model = ImageRequest.Builder(AppGlobal.getAppContext())
                            .data(data).build(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.3f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                if (!isAnimating) {
                                    isAnimating = true
                                    paletteHeight = if (paletteHeight == 0f) 85f else 0f
                                    alpha = if (alpha == 0f) 1f else 0f
                                }
                            },
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(paletteHeightAnimate.dp)
                            .background(brush = Brush.verticalGradient(dominantColorReverse)),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Column(Modifier
                            .height(100.dp)
                            .padding(DefaultUserPadding)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                if (alpha == 0f) {
                                    onChangeCover()
                                    Log.e("能看见吗", "嫩模刚看见")
                                }
                            }
                            .onGloballyPositioned { layoutCoordinates ->
                                changeCoverPosition = layoutCoordinates.positionInWindow()
                            }, horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(painterResource(R.drawable.change_cover),contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp)

                            )
                            Text("更换封面", fontSize = 10.sp, color = Color.White)
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .height(85.dp)
                        .padding(horizontal = DefaultUserPadding)
                        .offset(y = (-55).dp)
                        .alpha(alphaAnimate)) {
                        Spacer(Modifier.weight(1f))
                        BaseText(
                            nickname,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.width(10.dp))
                        AsyncImage(
                            model = ImageRequest.Builder(AppGlobal.getAppContext()).data(displayAvatar)
                                .build(), contentDescription = null, modifier = Modifier
                                .size(60.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    if(alpha == 1f) {
                                        onEnterFriendInfoDetail()
                                        Log.e(
                                            "nengkanjian",
                                            "ssss"
                                        )
                                    }
                                }
                                .clip(
                                    RoundedCornerShape(5.dp)
                                ), contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            content()
        }
    }
}
@Composable
fun ClickableOutlineCircle(
    diameter: Dp = 20.dp,
    outlineColor: Color = Color.Black,
    outlineWidth: Dp = 0.5.dp,
    fillColor: Color = Color.Transparent,
    onClick: () -> Unit={}
) {
    Box(
        modifier = Modifier
            .size(diameter)
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        val outlineWidthPx = with(LocalDensity.current) { outlineWidth.toPx() }

        Canvas(modifier = Modifier.size(diameter)) {
            val canvasSize = size.minDimension

            // 绘制填充色
            drawCircle(
                color = fillColor,
                radius = (canvasSize - outlineWidthPx) / 2,
                center = Offset(size.width / 2, size.height / 2)
            )

            // 绘制轮廓
            drawCircle(
                color = outlineColor,
                radius = canvasSize / 2,
                style = Stroke(width = outlineWidthPx),
                center = Offset(size.width / 2, size.height / 2)
            )
        }
    }
}
@Preview
@Composable
fun ClickableOutlineCirclePreview() {
    var isFilled by remember { mutableStateOf(false) }
    ClickableOutlineCircle(
        fillColor = if (isFilled) ChatGreen else Color.Transparent,
        onClick = { isFilled = !isFilled }
    )
}
@SuppressLint("ModifierFactoryUnreferencedReceiver", "UnnecessaryComposedModifier")
fun Modifier.clickableWithPosition(
    onClick: (Offset) -> Unit
): Modifier = composed {
    pointerInput(Unit) {
        detectTapGestures(
            onTap = { offset ->
                onClick(offset)
            }
        )
    }
}




