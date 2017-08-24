package com.jk.daggerrxkotlin.di.modules

import com.jk.daggerrxkotlin.api.IApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by M2353204 on 07/08/2017.
 */

@Module
public class NetworkModule{
    @Provides
    @Singleton
    fun getRetrofit(): IApi {
        return Retrofit.Builder()
                //http://ip.jsontest.com/?mime=6
                .baseUrl("https://api.github.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IApi::class.java)

    }
}