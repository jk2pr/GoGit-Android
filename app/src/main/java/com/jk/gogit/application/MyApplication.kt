package com.jk.gogit.application


import android.app.Application
import android.content.Context
import com.jk.gogit.di.components.AppComponent
import com.jk.gogit.di.components.DaggerAppComponent
import com.jk.gogit.di.modules.AppModule
import com.jk.gogit.di.modules.CacheModule
import com.jk.gogit.di.modules.DBModule
import com.jk.gogit.di.modules.NetworkModule


/**
 * Created by Jitendra on 02/08/2017.
 */
class MyApplication : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        appComponent = DaggerAppComponent.builder()
                .networkModule(NetworkModule())
                .appModule(AppModule(base))
                .dBModule(DBModule(base))
                .cacheModule(CacheModule(base))
                .build()

    }

}