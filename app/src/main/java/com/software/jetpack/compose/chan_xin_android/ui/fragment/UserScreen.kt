package com.software.jetpack.compose.chan_xin_android.ui.fragment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultFontSize
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultPaddingTop
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserPadding
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultUserScreenItemDp
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseBox
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.theme.ChatGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.RightArrowColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.SurfaceColor

@Composable
fun UserScreen(avatar:String = "",nickname:String = "",sex:Byte = 0) {
    Surface(modifier = Modifier.fillMaxSize(), color = SurfaceColor) {
        BaseBox(modifier = Modifier.fillMaxSize().background(Color.Transparent)) {
            Column(modifier = Modifier.fillMaxWidth().background(color = Color.White).padding(top = DefaultPaddingTop)) {
                Row(modifier = Modifier.fillMaxWidth().height(DefaultUserScreenItemDp).padding(DefaultUserPadding)) {
                    AsyncImage(model = ImageRequest.Builder(context = LocalContext.current).data(if (avatar == "") R.drawable.default_avatar else avatar).size(80).build(),contentDescription = null)
                    Spacer(modifier = Modifier.width(DefaultUserPadding))
                    BaseText(text = nickname, style = TextStyle(fontWeight = FontWeight.Bold), fontSize = 32.sp)
                }
                Spacer(modifier = Modifier.height(40.dp).fillMaxWidth().background(SurfaceColor))
                UserScreenItem(imageVector = Icons.Outlined.FavoriteBorder, label = "朋友圈", tintColor = ChatGreen) { }
                Spacer(modifier = Modifier.height(40.dp).fillMaxWidth().background(SurfaceColor))
                UserScreenItem(imageVector = Icons.Outlined.Settings, label = "设置", tintColor = Color.Blue) {

                }
            }
        }
    }
}
@Composable
fun UserScreenItem(imageVector: ImageVector, label:String,tintColor:Color,onClick:()->Unit) {
    Box(modifier = Modifier.clickable {
        onClick()
    }) {
        Row(modifier = Modifier.fillMaxWidth().height(DefaultUserScreenItemDp).padding(DefaultUserPadding)) {
            Icon(imageVector,contentDescription = null, tint = tintColor)
            Spacer(modifier = Modifier.width(DefaultUserPadding))
            BaseText(text = label, fontSize = DefaultFontSize)
            Spacer(modifier = Modifier.weight(1f))
            Icon(imageVector= Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = RightArrowColor)
        }
    }

}