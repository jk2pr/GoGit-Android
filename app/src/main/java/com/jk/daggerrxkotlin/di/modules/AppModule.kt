package com.jk.daggerrxkotlin.di.modules

import android.app.Application
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.jk.daggerrxkotlin.application.MyApplication
import dagger.Module
import dagger.Provides
import javax.inject.Scope
import javax.inject.Singleton

@Module
public class AppModule(val app: MyApplication) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return app
    }

    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(): FirebaseAnalytics = FirebaseAnalytics.getInstance(app);

}