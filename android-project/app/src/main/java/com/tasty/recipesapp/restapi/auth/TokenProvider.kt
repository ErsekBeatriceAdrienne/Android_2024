package com.tasty.recipesapp.restapi.auth

import android.content.Context

class TokenProvider(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun setAuthToken(token: String?) {
        sharedPreferences.edit().putString("access_token", token).apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }
}

