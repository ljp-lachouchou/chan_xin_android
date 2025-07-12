package com.software.jetpack.compose.chan_xin_android.ui.base

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.software.jetpack.compose.chan_xin_android.defaultValue.defaultLittleSize
import com.software.jetpack.compose.chan_xin_android.defaultValue.defaultPlaceholderText
import com.software.jetpack.compose.chan_xin_android.ui.activity.loginActivityScreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.LittleTextColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.PlaceholderColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.PressedLittleTextColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.TextColor
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

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
@Composable
fun LittleText(text: String,
               event:()->Unit,
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