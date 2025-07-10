package com.software.jetpack.compose.chan_xin_android.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.Surface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.coroutineScope
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultPaddingBottom
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultPaddingTop
import com.software.jetpack.compose.chan_xin_android.defaultValue.defaultLittleSize
import com.software.jetpack.compose.chan_xin_android.defaultValue.defaultLoginImageSize
import com.software.jetpack.compose.chan_xin_android.defaultValue.defaultPlaceholderText
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseBox
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseButton
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.base.LittleText
import com.software.jetpack.compose.chan_xin_android.ui.theme.ButtonColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.ChatGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.DividerColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.LittleTextColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.PlaceholderColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.PressedLittleTextColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.SurfaceColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.TextColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun loginActivityScreen(phone:String = "19931417018",loginEvent:() -> Unit,
                        retrievePasswordEvent:() -> Unit):String {
    var password = ""
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val bottomSheetContent:@Composable ColumnScope.() -> Unit = {
        BottomSheetContent(bottomSheetState,scope)
    }
    val moreEvent:()->Unit = {
        scope.launch { bottomSheetState.show() }
    }
    ModalBottomSheetLayout(sheetContent = bottomSheetContent,sheetState = bottomSheetState, sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)) {
        password = mainLoginContent(phone,loginEvent,retrievePasswordEvent,moreEvent)
    }
    return password
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetContent(bottomSheetState: ModalBottomSheetState,scope: CoroutineScope) {
    scope.apply {
        Column(modifier = Modifier.fillMaxWidth().background(color = Color.Transparent)) {
            TextButton(onClick = {
                launch { bottomSheetState.hide() }
            }, shape = RectangleShape) {
                BaseText("登录其他账号", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
            TextButton(onClick = {
                launch { bottomSheetState.hide() }
            }, shape = RectangleShape) {
                BaseText("注册", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
            Spacer(Modifier.fillMaxWidth().height(10.dp).background(SurfaceColor))
            TextButton(onClick = {
                launch { bottomSheetState.hide() }
            }, shape = RectangleShape) {
                BaseText(text = "取消", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }


    }
}
@Composable
fun mainLoginContent(
    phone: String,
    loginEvent: () -> Unit,
    retrievePasswordEvent: () -> Unit,
    moreEvent: () -> Unit
) :String{
    var password by remember {
        mutableStateOf("")
    }
    BaseBox {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = DefaultPaddingTop, bottom = DefaultPaddingBottom)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(modifier = Modifier.size(defaultLoginImageSize),
                painter = painterResource(R.drawable.default_avatar),
                contentDescription = null
            )
            Spacer(Modifier.padding(vertical = 5.dp))
            val tStyle = TextStyle(fontSize = 20.sp)
            Wrapper {
                BaseText(text = phone, style = tStyle)
            }
            Spacer(Modifier.padding(vertical = 20.dp))
            Wrapper {
                TextField(
                    colors = TextFieldDefaults.colors(cursorColor = IconGreen,
                        unfocusedContainerColor = Color.Transparent,focusedContainerColor=Color.Transparent,
                        focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                    ),
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    leadingIcon = {
                        BaseText("密码")
                    }, placeholder = { Text(defaultPlaceholderText, color = PlaceholderColor) }
                )
            }
            HorizontalDivider(color = DividerColor, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp))
            Spacer(Modifier.weight(0.55f))
            BaseButton(onClick = loginEvent, modifier = Modifier
                .width(165.dp)
                .height(45.dp)) {
                Text("登录", style = tStyle, color = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f))
            BottomRow(retrievePasswordEvent, moreEvent)
        }
    }
    return password
}

@Composable
fun Wrapper(content:@Composable () -> Unit){
    Box {
        content()
    }
}
@Preview
@Composable
fun BaseButtonExample() {
    val tStyle = TextStyle(fontSize = 20.sp)
    BaseButton(onClick = { } , modifier = Modifier
        .width(165.dp)
        .height(45.dp)) {
        Text("登录", style = tStyle, color = Color.White)
    }
}
@Composable
fun BottomRow(retrievePasswordEvent: () -> Unit,
              moreEvent: () -> Unit) {

    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceAround) {
        LittleText(text = "找回密码",event=retrievePasswordEvent)
        Spacer(Modifier.padding(horizontal = 5.dp))
        VerticalDivider(color = DividerColor, modifier = Modifier.height(20.dp))
        Spacer(Modifier.padding(horizontal = 5.dp))
        LittleText(text = "更多",event =  moreEvent)
    }
}
val LocalDialogTextModifier = compositionLocalOf {
    Modifier
}


@Composable
fun AppCoverScreen() {
    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(
        colorResource(R.color.purple_700),
        colorResource(R.color.purple_500),
        colorResource(R.color.purple_200),
    )))) {
        BaseBox {
            Column(modifier = Modifier.padding(bottom = DefaultPaddingBottom*2.5f).background(Color.Transparent), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.weight(0.5f))
                Image(painter = painterResource(R.drawable.icon),contentDescription = null)
                Spacer(Modifier.weight(0.4f))
                BaseText(text = "禅信·让天边尽在眼前", fontSize = 20.sp)
                Spacer(Modifier.weight(1f))
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    BaseButton(onClick = {
                    }, modifier = Modifier.height(50.dp).width(120.dp)) {
                        Text(text= "登录", color = Color.White)
                    }
                    BaseButton(onClick = {
                    },modifier = Modifier.height(50.dp).width(120.dp),colors = ButtonDefaults.buttonColors(backgroundColor = SurfaceColor, contentColor = TextColor)) {
                        BaseText(text= "注册")
                    }
                }

            }
        }
    }


}