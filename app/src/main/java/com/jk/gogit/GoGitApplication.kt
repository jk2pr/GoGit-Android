package com.jk.gogit

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.hoppers.networkmodule.AuthManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class GoGitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val context = applicationContext
        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        AuthManager.initialize(sharedPreferences, onLogout = {
            sharedPreferences.edit().clear().apply()
            val firebaseAuth = FirebaseAuth.getInstance()
            //  firebaseAuth.signOut()
            showToast()
        })
        startKoin {
            androidLogger()
            androidContext(this@GoGitApplication)
            modules(appModule)
        }
    }

    private fun showToast() {
        Toast.makeText(this, "Your session logged out, Please login again ", Toast.LENGTH_SHORT)
            .show()

    }
}