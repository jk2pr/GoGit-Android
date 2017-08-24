package com.jk.daggerrxkotlin.application


import android.app.Application
import com.jk.daggerrxkotlin.di.components.AppComponent
import com.jk.daggerrxkotlin.di.components.DaggerAppComponent
import com.jk.daggerrxkotlin.di.modules.AppModule
import com.jk.daggerrxkotlin.di.modules.NetworkModule


/**
 * Created by M2353204 on 02/08/2017.
 */
class MyApplication : Application() {
    companion object {
         lateinit var appComponent: AppComponent
    }
    override fun onCreate() {
        super.onCreate()
        appComponent= DaggerAppComponent.builder()
                .networkModule(NetworkModule())
                .appModule(AppModule(this))
                .build()



    }
}