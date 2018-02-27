package com.jk.gogit.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.jk.gogit.application.MyApplication
import dagger.Module
import dagger.Provides
import javax.inject.Scope
import javax.inject.Singleton

@Module
class AppModule(val app: MyApplication) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return app
    }

    @Provides
    @Singleton
    fun providePreference(): SharedPreferences {
       return app.getSharedPreferences("AccessToken", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(): FirebaseAnalytics = FirebaseAnalytics.getInstance(app)

    @Provides
    @Singleton
    fun provideFirebaseAppAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

}