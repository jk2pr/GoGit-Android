package com.hoppers.networkmodule.network

import android.content.SharedPreferences

object AuthManager {

    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(sharedPreferences: SharedPreferences) {
        AuthManager.sharedPreferences = sharedPreferences
    }

    fun saveAccessToken(token: String, login: String) {
        sharedPreferences.edit().putString("access_token", token).apply()
        sharedPreferences.edit().putString("login", login).apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }
    fun getLogin(): String? {
        return sharedPreferences.getString("login", null)
    }
}