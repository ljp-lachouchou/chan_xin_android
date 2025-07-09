package com.software.jetpack.compose.chan_xin_android.ui.activity

import androidx.annotation.Px
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.default.*
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseBox
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseButton
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.theme.DividerColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.PlaceholderColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.SurfaceColor

@Composable
fun loginActivityScreen(phone:String = "19931417018",loginEvent:() -> Unit):String {
    var password by remember {
        mutableStateOf("")
    }
    BaseBox {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = DefaultPaddingTop)
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
                        unfocusedContainerColor = SurfaceColor,focusedContainerColor=SurfaceColor,
                        focusedIndicatorColor = SurfaceColor, unfocusedIndicatorColor = SurfaceColor
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
            HorizontalDivider(color = DividerColor, modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp))
            Spacer(Modifier.padding(vertical = 40.dp))
            BaseButton(onClick = loginEvent, modifier = Modifier.width(165.dp).height(45.dp)) {
                Text("登录", style = tStyle, color = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(verticalAlignment = Alignment.Bottom) {
                Text("你好")
            }
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
@Composable
fun BottomRow() {
    Row(verticalAlignment = Alignment.Bottom) {
        Text("你好")
    }
}