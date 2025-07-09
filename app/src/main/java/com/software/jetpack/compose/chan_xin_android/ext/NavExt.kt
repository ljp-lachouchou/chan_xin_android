package com.software.jetpack.compose.chan_xin_android.ext

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

@SuppressLint("RestrictedApi")
fun NavHostController.switchTab(route:String, arg : Bundle? = null) {
    val list = currentBackStack.value.filter {
        it.destination.route == route
    }
    if (list.isEmpty()) {
        navigateTo(route)
    }else {
        popBackStack(route,false,false)
    }
}
fun NavController.navigateTo(route: String,
                             navOptions: NavOptions? = null,
                             navigatorExtras: Navigator.Extras? = null) {
    navigate(route,navOptions,navigatorExtras)
}