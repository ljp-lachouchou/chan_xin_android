package com.software.jetpack.compose.chan_xin_android.ui.activity

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonColors
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.software.jetpack.compose.chan_xin_android.R
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultHoDivider
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultPaddingBottom
import com.software.jetpack.compose.chan_xin_android.defaultValue.DefaultPaddingTop
import com.software.jetpack.compose.chan_xin_android.defaultValue.defaultLoginImageSize
import com.software.jetpack.compose.chan_xin_android.defaultValue.defaultPlaceholderText
import com.software.jetpack.compose.chan_xin_android.ext.switchTab
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseBox
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseButton
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseText
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseTextField
import com.software.jetpack.compose.chan_xin_android.ui.base.LittleText
import com.software.jetpack.compose.chan_xin_android.ui.theme.DividerColor
import com.software.jetpack.compose.chan_xin_android.ui.theme.IconGreen
import com.software.jetpack.compose.chan_xin_android.ui.theme.PlaceholderColor
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
                BaseTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    leadingIcon = {
                        BaseText("密码")
                    }, placeholder = defaultPlaceholderText
                )
            }
            HorizontalDivider(color = DividerColor, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DefaultHoDivider))
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
fun AppCoverScreen(navController:NavHostController) {
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
                        navController.switchTab("login")
                    }, modifier = Modifier.height(50.dp).width(120.dp)) {
                        Text(text= "登录", color = Color.White)
                    }
                    BaseButton(onClick = {
                        navController.switchTab("register")
                    },modifier = Modifier.height(50.dp).width(120.dp),colors = ButtonDefaults.buttonColors(backgroundColor = SurfaceColor, contentColor = TextColor)) {
                        BaseText(text= "注册")
                    }
                }

            }
        }
    }
}
@Composable
fun AppCoverScreenNav(loginEvent:()->Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "parent") {
        composable(route="parent") {
            AppCoverScreen(navController)
        }
        composable(route = "login") {
            LoginScreen(loginEvent,navController)
        }
        composable(route="register") {
            RegisterScreen(navController)
        }
    }
}
@Composable
fun LoginScreen(loginEvent: () -> Unit, navController: NavHostController) {
    Scaffold(topBar = {AppTopBar(navController=navController)}) {
        padding->
        Text(text = "登录", modifier = Modifier.padding(padding))
    }
}
@Composable
fun RegisterScreen(navController: NavHostController) {
    var password by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var sex:Byte = 0
    var avatar = ""
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        uri: Uri? ->
        selectedImageUri = uri
    }
    Log.e("selectedUri",selectedImageUri?.path.toString())
    Scaffold(topBar = {AppTopBar(navController=navController)}) {
            padding->
        BaseBox(modifier = Modifier.fillMaxSize().padding(padding).background(color = SurfaceColor)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize().padding(top = DefaultPaddingTop)) {
                Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "手机号注册", fontSize = 20.sp)
                Column {
                    Image(painter = painterResource(R.drawable.default_avatar), contentDescription = null, modifier = Modifier.size(80.dp).padding(top = 20.dp, bottom = 5.dp).clickable {
                        launcher.launch("image/*")
                    })

                }
                Wrapper {
                    BaseTextField(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        value = nickname,
                        onValueChange = {
                            nickname = it
                        },
                        leadingIcon = {
                            BaseText("昵称")
                        }, placeholder = "例如:LJP"
                    )
                }
                sex = sexSingleSelection()
                HorizontalDivider(color = DividerColor, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DefaultHoDivider))
                Wrapper {
                    BaseTextField(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).padding(start=7.dp),
                        value = phone,
                        onValueChange = {
                            phone = it
                        },
                        leadingIcon = {
                            BaseText("手机号")
                        }, placeholder = "请填写手机号"
                    )
                }
                HorizontalDivider(color = DividerColor, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DefaultHoDivider))
                Wrapper {
                    BaseTextField(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        leadingIcon = {
                            BaseText("密码")
                        }, placeholder = defaultPlaceholderText
                    )
                }
                HorizontalDivider(color = DividerColor, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DefaultHoDivider))
                Spacer(Modifier.height(180.dp))
                BaseButton(onClick = {
                    registerService(nickname,sex,phone,password,avatar)
                }, modifier = Modifier.width(155.dp).height(55.dp)) {
                    BaseText(text = "欢迎加入", color = Color.White)
                }
            }

        }

    }
}

fun registerService(nickname: String, sex: Byte, phone: String, password: String, avatar: String) {

}

@Composable
fun sexSingleSelection(modifier: Modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)):Byte{
    var sex by remember { mutableIntStateOf(0) }
    val selection = mapOf(0 to "未知",1 to "男性",-1 to "女性")
    val (selectedOption,onOptionSelected) = remember { mutableStateOf(selection[0]) }
    Log.e("sex","$sex")
    Row(modifier = modifier.selectableGroup(), horizontalArrangement = Arrangement.SpaceBetween) {
        BaseText("性别")
       selection.forEach{
               (k, v) ->
           Row(modifier = Modifier.selectable(selected = (selectedOption==v), onClick = { onOptionSelected(v); sex = k}, role = Role.RadioButton)) {
                RadioButton(selected = (v==selectedOption), onClick = null, colors = RadioButtonDefaults.colors(selectedColor= IconGreen), modifier = Modifier.indication(indication = null, interactionSource = remember { MutableInteractionSource() }))
               BaseText(text = v)
           }
           Spacer(Modifier.width(10.dp))
       }
    }
    return sex.toByte()
}
@Composable
fun AppTopBar(title:String = "",navigationIcon: @Composable () -> Unit,actions: @Composable RowScope.() -> Unit = {}) {
    TopAppBar(title = { Text(text = title, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }, navigationIcon = navigationIcon, actions =actions,backgroundColor = SurfaceColor)
}
@Composable
fun AppTopBar(title:String = "",actions: @Composable RowScope.() -> Unit = {},navController:NavHostController) {
    TopAppBar(title = { Text(text = title, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }, navigationIcon = {
        IconButton(onClick = {navController.switchTab("parent")}) {
            Icon(Icons.Filled.Close, contentDescription = null)
        }
    }, actions =actions,backgroundColor = SurfaceColor)
}
@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        val contentAlpha = if (enabled) LocalContentAlpha.current else ContentAlpha.disabled
        CompositionLocalProvider(LocalContentAlpha provides contentAlpha, content = content)
    }
}