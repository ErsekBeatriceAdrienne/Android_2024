package com.tasty.recipesapp.restapi.auth

import android.content.Context
import android.content.SharedPreferences

class TokenProvider(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun getAuthToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    fun setAuthToken(token: String) {
        sharedPreferences.edit().putString("auth_token", token).apply()
    }
}
