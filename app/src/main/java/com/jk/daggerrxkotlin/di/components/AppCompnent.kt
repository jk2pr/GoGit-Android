package com.jk.daggerrxkotlin.di.components

import com.jk.daggerrxkotlin.di.modules.AppModule
import com.jk.daggerrxkotlin.di.modules.DBModule
import com.jk.daggerrxkotlin.di.modules.NetworkModule
import com.jk.daggerrxkotlin.model.UserProfile
import com.jk.daggerrxkotlin.ui.view.DataFragment
import com.jk.daggerrxkotlin.ui.view.MainActivity
import com.jk.daggerrxkotlin.ui.view.Splash
import com.jk.daggerrxkotlin.ui.view.UserProfileActivity
import com.jk.daggerrxkotlin.ui.viewmodel.UserViewModel
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
    fun inject(userViewModel: UserViewModel)
}