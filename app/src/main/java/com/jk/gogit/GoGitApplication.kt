package com.jk.gogit

import com.hoppers.networkmodule.network.AuthManager
import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class GoGitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        com.hoppers.networkmodule.network.AuthManager.initialize(sharedPreferences)
        startKoin {
            androidLogger()
            androidContext(this@GoGitApplication)
            modules(appModule)
        }
    }
}