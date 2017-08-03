package com.jk.daggerrxkotlin.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * News API
 *
 * @author juancho.
 */
class NewsAPI {

    companion object {


        fun getRetrofit(): IApi {
            return Retrofit.Builder()
                    .baseUrl("https://www.reddit.com")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                    .create(IApi::class.java)

        }
    }
}