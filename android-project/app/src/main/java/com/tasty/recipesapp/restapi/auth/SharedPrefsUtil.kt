package com.tasty.recipesapp.restapi.auth

import android.content.Context

object SharedPrefsUtil {
    private const val PREFS_NAME = "app_prefs"
    private const val TOKEN_KEY = "access_token"

    fun getAccessToken(context: Context): String? {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getString(TOKEN_KEY, null)
    }

    fun saveAccessToken(context: Context, token: String) {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    fun isLoggedIn(context: Context): Boolean {
        return getAccessToken(context) != null
    }
}
