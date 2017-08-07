package com.jk.daggerrxkotlin.api

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface IApi {
    @GET()
    fun getIp(@Query("mime") number: String): Observable<DataResponse>;
}