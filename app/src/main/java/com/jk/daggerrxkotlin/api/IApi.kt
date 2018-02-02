package com.jk.daggerrxkotlin.api

import io.reactivex.Observable
import retrofit2.http.*


interface IApi {

    @Headers("Accept: application/json")
    @POST()
    @FormUrlEncoded
    fun getAccessToken(
            @Url url: String,
            @Field("client_id") client_id: String,
            @Field("client_secret") client_secret: String,
            @Field("redirect_uri") redirect_uri: String,
            @Field("state") state: String,
            @Field("code") code: String
            /*@Field("grant_type") grantType: String*/): Observable<AccessToken>


    @GET("search/users")
            //fun getIp(@Query("mime") number: String): Observable<User>;
    fun searchUsers(@Query("q") query: String, @Query("page") page: Int, @Query("per_page") perPage: Int): Observable<Result>


//    @GET("users/{user}/repos")
    //@Path


    @GET("/user/repos")
    fun getAllRepository(@Query("visibility") visibility:String) : Observable<String>

}