package com.software.jetpack.compose.chan_xin_android.util

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesFileName {
    const val USERS_FILE = "users"
    val PHONE_KEY = stringPreferencesKey("user_phone")
    val USER_TOKEN = stringPreferencesKey("user_token")
    val USER_TOKEN_EXP = longPreferencesKey("user_token_exp")
}