package com.jk.daggerrxkotlin.di.components

import com.jk.daggerrxkotlin.DataFragment
import com.jk.daggerrxkotlin.MainActivity
import com.jk.daggerrxkotlin.Splash
import com.jk.daggerrxkotlin.UserProfileFragment
import com.jk.daggerrxkotlin.di.modules.AppModule
import com.jk.daggerrxkotlin.di.modules.DBModule
import com.jk.daggerrxkotlin.di.modules.NetworkModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by M2353204 on 07/08/2017.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class, DBModule::class))
interface AppComponent {
    fun inject(app: MainActivity)
    fun inject(app: Splash)
    fun inject(dataFragment: DataFragment)
    fun inject(dataFragment: UserProfileFragment)
    fun inject(networkModule: NetworkModule)
}