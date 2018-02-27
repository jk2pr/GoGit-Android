package com.jk.gogit.di.components

import com.jk.gogit.di.modules.AppModule
import com.jk.gogit.di.modules.DBModule
import com.jk.gogit.di.modules.NetworkModule
import com.jk.gogit.model.UserProfile
import com.jk.gogit.ui.view.*
import com.jk.gogit.ui.viewmodel.UserViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Created by M2353204 on 07/08/2017.
 */
@Singleton
@Component(modules = [(AppModule::class), (NetworkModule::class), (DBModule::class)])
interface AppComponent {
    fun inject(auth: BaseActivity)
   // fun inject(app: MainActivity)
   // fun inject(app: Splash)
    fun inject(dataFragment: DataFragment)
  //  fun inject(dataFragment: UserProfileActivity)
    fun inject(networkModule: NetworkModule)
    fun inject(userViewModel: UserViewModel)

}