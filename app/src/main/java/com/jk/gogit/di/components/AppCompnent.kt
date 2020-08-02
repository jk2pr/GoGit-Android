package com.jk.gogit.di.components

import com.crashlytics.android.answers.Answers
import com.jk.gogit.SearchSuggestionsProvider
import com.jk.gogit.di.modules.AppModule
import com.jk.gogit.di.modules.CacheModule
import com.jk.gogit.di.modules.DBModule
import com.jk.gogit.di.modules.NetworkModule
import com.jk.gogit.ui.view.BaseActivity
import com.jk.gogit.ui.view.SplashActivity
import com.jk.gogit.ui.view.fragments.SearchUserFragment
import com.jk.gogit.ui.viewmodel.UserViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [(AppModule::class), (NetworkModule::class),(CacheModule::class), (DBModule::class)])
interface AppComponent {
    fun inject(auth: BaseActivity)
   // fun inject(app: MainActivity)
    fun inject(app: SplashActivity)
    fun inject(dataFragment: SearchUserFragment)
  //  fun inject(dataFragment: UserProfileActivity)
    fun inject(networkModule: NetworkModule)
    fun inject(userViewModel: UserViewModel)
    fun inject(provider: SearchSuggestionsProvider)
    fun inject(provider: Answers)

}