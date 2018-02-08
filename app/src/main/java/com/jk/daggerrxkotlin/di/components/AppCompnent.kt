package com.jk.daggerrxkotlin.di.components

import com.jk.daggerrxkotlin.*
import com.jk.daggerrxkotlin.di.modules.AppModule
import com.jk.daggerrxkotlin.di.modules.DBModule
import com.jk.daggerrxkotlin.di.modules.NetworkModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by M2353204 on 07/08/2017.
 */
@Singleton
@Component(modules = [(AppModule::class), (NetworkModule::class), (DBModule::class)])
interface AppComponent {
    fun inject(app: MainActivity)
    fun inject(app: Splash)
    fun inject(dataFragment: DataFragment)
    fun inject(dataFragment: UserProfileActivity)
    fun inject(networkModule: NetworkModule)
}