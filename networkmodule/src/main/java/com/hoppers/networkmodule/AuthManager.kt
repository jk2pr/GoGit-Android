package com.hoppers.networkmodule

import android.content.SharedPreferences

object AuthManager {

    private lateinit var sharedPreferences: SharedPreferences
    private var onLogout: (() -> Unit) = { }

    fun initialize(
        sharedPreferences: SharedPreferences,
        onLogout: () -> Unit
    ) {
        this.onLogout = onLogout
        AuthManager.sharedPreferences = sharedPreferences
    }

    fun saveAccessToken(token: String, avatarUrl: String, login: String) {
        sharedPreferences.edit().putString("access_token", token).apply()
        sharedPreferences.edit().putString("avatarUrl", avatarUrl).apply()
        sharedPreferences.edit().putString("login", login).apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    fun getLogin(): String? {
        return sharedPreferences.getString("login", null)
    }

    fun getAvatarUrl(): String? {
        return sharedPreferences.getString("avatarUrl", null)
    }

    fun logout() = onLogout.invoke()

}