package com.jk.gogit.network.api

import com.jk.gogit.model.Feed
import com.jk.gogit.model.Repo
import com.jk.gogit.model.UserProfile
import com.jk.gogit.model.Users
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



    @GET("users/{user}/received_events")
    fun getFeed(@Path("user") user: String,@Query("page") page: Int, @Query("per_page") perPage: Int): Observable<List<Feed>>


//    @GET("users/{user}/repos")
    //@Path

    @GET("/user")
    fun getMyProfile(): Observable<UserProfile>



    @GET("/users/{user}")
    fun getUserProfile(@Path("user") user: String): Observable<UserProfile>


    @GET("/user/repos")
    fun getMyRepository(@Query("visibility") visibility: String): Observable<List<Repo>>

    @GET("/users/{user}/repos")
    fun getUserAllRepository(@Path("user") user: String,@Query("visibility") visibility: String): Observable<List<Repo>>


    @GET("/user/followers")
    fun getMyFollowers(): Observable<List<Users>>

    @GET("/users/{user}/followers")
    fun getUserFollowers(@Path("user") user: String): Observable<List<Users>>

    @GET("/user/following")
    fun getMyFollowing(): Observable<List<Users>>

    @GET("/users/{user}/following")
    fun getUserFollowing(@Path("user") user: String): Observable<List<Users>>

}