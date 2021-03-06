package com.jk.gogit.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.jk.gogit.R
import com.jk.gogit.application.MyApplication.Companion.appComponent
import com.jk.gogit.exception.UserUnAuthorizedException
import com.jk.gogit.network.api.IApi
import com.jk.gogit.network.api.ILogin
import com.jk.gogit.utils.HeaderHandler
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by M2353204 on 07/08/2017.
 */

@Module
class NetworkModule {
    @Inject
    lateinit var pref: SharedPreferences
    @Inject
    lateinit var app: Context
    @Inject
    lateinit var cache: Cache

    @Provides
    @Singleton
    fun getLoginRetrofit(): ILogin {
        appComponent.inject(this)
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.NONE
        val gSon = GsonBuilder()
                .setLenient()
                .create()
        val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor { chain ->
                    val ongoing = chain.request().newBuilder()
                    ongoing.addHeader("Accept", "application/json;versions=1")
                    val token = pref.getString("initToken", "")
                    if (token != null)
                        ongoing.addHeader("Authorization", token)
                    ongoing.addHeader("User-Agent", app.resources.getString(R.string.app_name))

                    chain.proceed(ongoing.build())
                }.addInterceptor { chain
                    ->
                    val request = chain.request()
                    val response = chain.proceed(request)
                    when (response.code()) {
                        401 ->
                            throw UserUnAuthorizedException(app.resources.getString(R.string.invalid_credential))
                    }
                    response
                }

                .build()
        return Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gSon))
                .client(client)
                .build()
                .create(ILogin::class.java)
    }


    @Provides
    @Singleton
    fun getRetrofit(): IApi {
        appComponent.inject(this)
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY


        val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    //var ongoing = chain.request().newBuilder()
                    val ongoing = HeaderHandler.handleCommonHeader(pref, app, chain)
                    chain.proceed(ongoing)
                }.addInterceptor { chain ->
                    HeaderHandler.handleGetPutDeleteRequest(chain)
                    val request = chain.request()
                    val response = chain.proceed(request)
                    HeaderHandler.handleResponseErrors(response)
                    response
                }
                .addInterceptor(logging)
                .cache(cache)
                .build()
        val gSon = GsonBuilder()
                .setLenient()
                .create()

        return Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ToStringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gSon))
                .client(client)
                .build()
                .create(IApi::class.java)

    }

    inner class ToStringConverterFactory : Converter.Factory() {
        override fun responseBodyConverter(type: Type, annotations: Array<Annotation>,
                                           retrofit: Retrofit): Converter<ResponseBody, *>? {
            return if (String::class.java == type) {
                Converter<ResponseBody, String> { value -> value.string() }
            } else null
        }

        override fun requestBodyConverter(type: Type, parameterAnnotations: Array<Annotation>,
                                          methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<*, RequestBody>? {

            return if (String::class.java == type) {
                Converter<String, RequestBody> { value -> RequestBody.create(MEDIA_TYPE, value) }
            } else null
        }


    }

    private val MEDIA_TYPE = MediaType.parse("text/plain")

}