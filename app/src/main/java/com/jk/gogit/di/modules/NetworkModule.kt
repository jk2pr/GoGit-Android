package com.jk.gogit.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.jk.gogit.network.api.IApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.jk.gogit.application.MyApplication
import javax.inject.Inject


/**
 * Created by M2353204 on 07/08/2017.
 */

@Module
class NetworkModule {
    @Inject
    lateinit var pref: SharedPreferences

    @Provides
    @Singleton
    fun getRetrofit(): IApi {
        MyApplication.appComponent.inject(this)
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
                .addInterceptor(logging)

                .addInterceptor { chain ->
                    val ongoing = chain.request().newBuilder()
                    ongoing.addHeader("Accept", "application/json;versions=1")

//                    val preference=
                    val token= pref.getString("AccessToken","")
                    if (token!=null)
                        ongoing.addHeader("Authorization", "token "+token)

                    chain.proceed(ongoing.build())
                }.build()

        return Retrofit.Builder()
                //http://ip.jsontest.com/?mime=6
                .baseUrl("https://api.github.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(IApi::class.java)

    }


}