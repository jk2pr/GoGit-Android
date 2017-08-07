package com.jk.daggerrxkotlin.application

import android.app.Application

import di.components.AppComponent
import di.components.DaggerAppComponent


import di.modules.AppModule
import di.modules.NetworkModule


/**
 * Created by M2353204 on 02/08/2017.
 */
class MyApplication : Application() {
    companion object {
         lateinit var appComponent: AppComponent
    }
    override fun onCreate() {
        super.onCreate()
        appComponent= DaggerAppComponent.builder().appModule(AppModule(this))
                .networkModule(NetworkModule())
                .build()



    }
}