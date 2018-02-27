package com.jk.gogit.application


import android.app.Application
import com.crashlytics.android.Crashlytics
import com.jk.gogit.di.components.AppComponent
import com.jk.gogit.di.components.DaggerAppComponent
import com.jk.gogit.di.modules.AppModule
import com.jk.gogit.di.modules.DBModule
import com.jk.gogit.di.modules.NetworkModule
import io.fabric.sdk.android.Fabric


/**
 * Created by M2353204 on 02/08/2017.
 */
class MyApplication : Application() {
    companion object {
         lateinit var appComponent: AppComponent
    }
    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        appComponent= DaggerAppComponent.builder()
                .networkModule(NetworkModule())
                .appModule(AppModule(this))
                .dBModule(DBModule(this))
                .build()



    }
}