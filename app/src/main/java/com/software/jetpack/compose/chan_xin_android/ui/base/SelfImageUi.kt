package com.software.jetpack.compose.chan_xin_android.ui.base

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal

interface SelfImage {
    @Composable
    fun SelfImageLayout()
    @Composable
    fun FirstImageUi()
}
open class SelfImageUi(context:Context) {
    val model = ImageRequest.Builder(context)
}
abstract class AbstractSelfImageUi(context: Context) : SelfImageUi(context),SelfImage {
    @Composable
    open fun SelfImageUi() {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            FirstImageUi()
            SelfImageLayout()
        }
    }
}
class OneSelfImageUi(context: Context,private val data: List<String>):AbstractSelfImageUi(context) {
    @Composable
    override fun SelfImageLayout() {
        AsyncImage(model = model.data(data.first()).build(),contentDescription = null, modifier = Modifier.size(100.dp), contentScale = ContentScale.Crop)
    }

    @Composable
    override fun FirstImageUi() {

    }

}
@Composable
fun LazyVerticalHavVHGrid(columns: GridCells,size:Int=100,content: LazyGridScope.() -> Unit) = LazyVerticalGrid(
    columns = columns,
    verticalArrangement = Arrangement.spacedBy(3.dp),
    horizontalArrangement = Arrangement.spacedBy(3.dp),
    modifier = Modifier.size(size.dp),
    content = content
)
@Composable
fun LazyVerticalHavVHWithModifierGrid(columns: GridCells,modifier: Modifier=Modifier,content: LazyGridScope.() -> Unit) = LazyVerticalGrid(
    columns = columns,
    verticalArrangement = Arrangement.spacedBy(3.dp),
    horizontalArrangement = Arrangement.spacedBy(3.dp),
    modifier = modifier,
    content = content
)
class TwoSelfImageUi(context: Context,private val data: List<String>):AbstractSelfImageUi(context) {
    @Composable
    override fun SelfImageLayout() {
        LazyVerticalHavVHGrid(GridCells.Fixed(2)) {
            items(data) {url->
                AsyncImage(model = model.data(url).build(),contentDescription = null, modifier = Modifier.height(100.dp).width(48.dp), contentScale = ContentScale.Crop)
            }
        }
    }

    @Composable
    override fun FirstImageUi() {

    }

}
class ThreeSelfImageUi(context: Context,private val data: List<String>):AbstractSelfImageUi(context) {
    @Composable
    override fun SelfImageLayout() {
        LazyVerticalHavVHWithModifierGrid(GridCells.Fixed(1), modifier = Modifier.height(100.dp).width(50.dp)) {
            items(data.run {
                subList(1,size)
            }) {url->
                AsyncImage(model = model.data(url).build(),contentDescription = null, modifier = Modifier.height(50.dp).width(50.dp), contentScale = ContentScale.Crop)
            }
        }
    }

    @Composable
    override fun FirstImageUi() {
        AsyncImage(model = model.data(data.first()).build(),contentDescription = null, modifier = Modifier.height(100.dp).width(50.dp), contentScale = ContentScale.Crop)
    }

}
class FourSelfImageUi(context: Context,private val data: List<String>):AbstractSelfImageUi(context) {
    @Composable
    override fun SelfImageLayout() {
        LazyVerticalHavVHGrid(GridCells.Fixed(2)) {
            items(data) {url->
                AsyncImage(model = model.data(url).build(),contentDescription = null, modifier = Modifier.size(48.dp), contentScale = ContentScale.Crop)
            }
        }
    }

    @Composable
    override fun FirstImageUi() {

    }

}
class FiveSelfImageUi(context: Context,private val data: List<String>):AbstractSelfImageUi(context) {
    @Composable
    override fun SelfImageLayout() {
        LazyVerticalHavVHGrid(GridCells.Fixed(3)) {
            items(data.run {
                subList(2,size)
            }) {url->
                AsyncImage(model=model.data(url).build(),contentDescription = null, modifier = Modifier.height(32.dp).width(32.dp), contentScale = ContentScale.Crop)
            }
        }
    }

    @Composable
    override fun FirstImageUi() {
        Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)){
            AsyncImage(model=model.data(data.first()).build(),contentDescription = null, modifier = Modifier.height(64.dp).width(64.dp), contentScale = ContentScale.Crop)
            AsyncImage(model=model.data(data[1]).build(),contentDescription = null, modifier = Modifier.height(64.dp).width(32.dp), contentScale = ContentScale.Crop)
        }
    }

    @Composable
    override fun SelfImageUi() {
        Column(verticalArrangement = Arrangement.spacedBy(3.dp), horizontalAlignment = Alignment.CenterHorizontally){
            FirstImageUi()
            SelfImageLayout()
        }
    }

}
class SixSelfImageUi(context: Context,private val data: List<String>):AbstractSelfImageUi(context) {
    @Composable
    override fun SelfImageLayout() {
        LazyVerticalHavVHGrid(GridCells.Fixed(3)) {
            items(data.run {
                subList(3,size)
            }) {url->
                AsyncImage(model=model.data(url).build(),contentDescription = null, modifier = Modifier.height(32.dp).width(32.dp), contentScale = ContentScale.Crop)
            }
        }
    }

    @Composable
    override fun FirstImageUi() {
        Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)){
            AsyncImage(model=model.data(data.first()).build(),contentDescription = null, modifier = Modifier.height(64.dp).width(64.dp), contentScale = ContentScale.Crop)
            LazyVerticalHavVHWithModifierGrid(GridCells.Fixed(1), modifier = Modifier.height(64.dp).width(32.dp)) {
                items(data.run {
                    subList(1,3)
                }) {url->
                    AsyncImage(model=model.data(url).build(),contentDescription = null, modifier = Modifier.height(32.dp).width(32.dp), contentScale = ContentScale.Crop)
                }
            }
        }
    }
    @Composable
    override fun SelfImageUi() {
        Column(verticalArrangement = Arrangement.spacedBy(3.dp), horizontalAlignment = Alignment.CenterHorizontally){
            FirstImageUi()
            SelfImageLayout()
        }
    }

}

class SevenSelfImageUi(context: Context,private val data: List<String>):AbstractSelfImageUi(context) {
    @Composable
    override fun SelfImageLayout() {
        LazyVerticalHavVHGrid(GridCells.Fixed(3)) {
            items(data.run {
                subList(4,size)
            }) {url->
                AsyncImage(model=model.data(url).build(),contentDescription = null, modifier = Modifier.height(32.dp).width(32.dp), contentScale = ContentScale.Crop)
            }
        }
    }

    @Composable
    override fun FirstImageUi() {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            LazyVerticalHavVHGrid(GridCells.Fixed(2), size = 64) {
                items(data.run {
                    subList(0,2)
                }) {url->
                    AsyncImage(model=model.data(url).build(),contentDescription = null, modifier = Modifier.height(64.dp).width(32.dp), contentScale = ContentScale.Crop)
                }
            }
            LazyVerticalHavVHWithModifierGrid(GridCells.Fixed(1), modifier = Modifier.height(64.dp).width(32.dp)) {
                items(data.run {
                    subList(2,4)
                }) {url->
                    AsyncImage(model=model.data(url).build(),contentDescription = null, modifier = Modifier.height(32.dp).width(32.dp), contentScale = ContentScale.Crop)
                }
            }
        }
    }
    @Composable
    override fun SelfImageUi() {
        Column(verticalArrangement = Arrangement.spacedBy(3.dp), horizontalAlignment = Alignment.CenterHorizontally){
            FirstImageUi()
            SelfImageLayout()
        }
    }

}

class EightSelfImageUi(context: Context,private val data: List<String>):AbstractSelfImageUi(context) {
    @Composable
    override fun SelfImageLayout() {
        LazyVerticalHavVHGrid(GridCells.Fixed(3)) {
            items(data.run {
                subList(5,size)
            }) {url->
                AsyncImage(model=model.data(url).build(),contentDescription = null, modifier = Modifier.height(32.dp).width(32.dp), contentScale = ContentScale.Crop)
            }
        }
    }

    @Composable
    override fun FirstImageUi() {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            AsyncImage(model=model.data(data.first()).build(),contentDescription = null, modifier = Modifier.height(64.dp).width(32.dp), contentScale = ContentScale.Crop)
            LazyVerticalHavVHGrid(GridCells.Fixed(2), size = 64) {
                items(data.run {
                    subList(1,5)
                }) {url->
                    AsyncImage(model=model.data(url).build(),contentDescription = null, modifier = Modifier.height(32.dp).width(32.dp), contentScale = ContentScale.Crop)
                }
            }
        }
    }
    @Composable
    override fun SelfImageUi() {
        Column(verticalArrangement = Arrangement.spacedBy(3.dp), horizontalAlignment = Alignment.CenterHorizontally){
            FirstImageUi()
            SelfImageLayout()
        }
    }

}

class NineSelfImageUi(context: Context,private val data: List<String>):AbstractSelfImageUi(context) {
    @Composable
    override fun SelfImageLayout() {
        LazyVerticalHavVHGrid(GridCells.Fixed(3)) {
            items(data) {url->
                AsyncImage(model = model.data(url).build(),contentDescription = null, modifier = Modifier.size(31.dp), contentScale = ContentScale.Crop)
            }
        }
    }

    @Composable
    override fun FirstImageUi() {

    }

}
@Composable
fun SelfImageUi(selfImageUi: AbstractSelfImageUi) {
    selfImageUi.SelfImageUi()
}
@Preview
@Composable
fun Example() {
    val data = listOf("https://chan-xin.oss-cn-beijing.aliyuncs.com/chan_xin/image/1754546802048.jpg","https://chan-xin.oss-cn-beijing.aliyuncs.com/chan_xin/image/1754546803379.jpg","https://chan-xin.oss-cn-beijing.aliyuncs.com/chan_xin/image/1754546804803.jpg","https://chan-xin.oss-cn-beijing.aliyuncs.com/chan_xin/image/1754546806363.jpg","https://chan-xin.oss-cn-beijing.aliyuncs.com/chan_xin/image/1754546808266.jpg","https://chan-xin.oss-cn-beijing.aliyuncs.com/chan_xin/image/1754546809831.jpg","https://chan-xin.oss-cn-beijing.aliyuncs.com/chan_xin/image/1754546811713.jpg","https://chan-xin.oss-cn-beijing.aliyuncs.com/chan_xin/image/1754546812769.jpg","https://chan-xin.oss-cn-beijing.aliyuncs.com/chan_xin/image/1754546814259.jpg")
    val selfImageUi = NineSelfImageUi(LocalContext.current, data = data)
    SelfImageUi(selfImageUi)
}