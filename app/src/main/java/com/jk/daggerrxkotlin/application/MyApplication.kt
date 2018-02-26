package com.jk.daggerrxkotlin.application


import android.app.Application
import com.crashlytics.android.Crashlytics
import com.jk.daggerrxkotlin.di.components.AppComponent
import com.jk.daggerrxkotlin.di.components.DaggerAppComponent
import com.jk.daggerrxkotlin.di.modules.AppModule
import com.jk.daggerrxkotlin.di.modules.DBModule
import com.jk.daggerrxkotlin.di.modules.NetworkModule
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