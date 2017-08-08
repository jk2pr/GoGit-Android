package com.jk.daggerrxkotlin.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface IApi {
    @GET("search/users")
            //fun getIp(@Query("mime") number: String): Observable<User>;
    fun searchUsers(@Query("q") query: String, @Query("page") page: Int, @Query("per_page") perPage: Int): Observable<Result>;
}