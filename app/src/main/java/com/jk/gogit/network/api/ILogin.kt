package com.jk.gogit.network.api

import com.jk.gogit.model.Login.AccessToken
import com.jk.gogit.model.Login.AuthRequestModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ILogin {
    @POST("authorizations")
    @Headers("Accept: application/json")
     fun authorizations(
            @Body authRequestModel: AuthRequestModel
    ): Observable<Response<AccessToken>>

    /*@POST("login/oauth/access_token")
    @Headers("Accept: application/json")
     fun getAccessToken(
            @Query("client_id") clientId: String,
            @Query("client_secret") clientSecret: String,
            @Query("code") code: String,
            @Query("state") state: String
    ): Observable<Response<AccessToken>>*/
}