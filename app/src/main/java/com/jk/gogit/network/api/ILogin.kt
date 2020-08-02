package com.jk.gogit.network.api

import com.jk.gogit.model.login.AccessToken
import com.jk.gogit.model.login.AuthRequestModel
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ILogin {
    @POST("authorizations")
    @Headers("Accept: application/json")
    fun authorizations(
            @Body authRequestModel: AuthRequestModel
    ): AccessToken
}