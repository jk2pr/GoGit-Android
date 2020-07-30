package com.jk.gogit.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.crashlytics.android.answers.Answers
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.jk.gogit.R
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return app
    }

    @Provides
    @Singleton
    fun providePreference(): SharedPreferences {
        return app.getSharedPreferences(app.getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(): FirebaseAnalytics = FirebaseAnalytics.getInstance(app)

    @Provides
    @Singleton
    fun provideFirebaseAppAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAnswers(): Answers {
        return Answers.getInstance()
    }

}