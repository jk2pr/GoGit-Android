package com.jk.gogit.utils

import android.content.Context
import android.content.SharedPreferences
import com.jk.gogit.BuildConfig
import com.jk.gogit.R
import com.jk.gogit.exception.*
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

object HeaderHandler {

    fun handleCommonHeader(pref: SharedPreferences, app: Context, chain: Interceptor.Chain): Request {
        var ongoing = chain.request().newBuilder()
        val token = pref.getString("AccessToken", "")
        if (token.isNullOrBlank()) {
            //This only required if user is not logged in user
            val orgRequest = chain.request()
            val org = chain.request().url()
            val url = org.newBuilder()
                    .addQueryParameter("client_id", BuildConfig.client_id)
                    .addQueryParameter("client_secret", BuildConfig.client_secret)
                    .build()
            ongoing = orgRequest.newBuilder().url(url)
        } else {
            ongoing.addHeader("Authorization", "token $token")
        }
        ongoing.addHeader("User-Agent", app.resources.getString(R.string.app_name))
        val noCache = chain.request().header("no-cache")
        if (noCache != null)
            ongoing.addHeader("Cache-Control", "no-cache")
        val isHTML = chain.request().header("isHTML")
        if (isHTML != null) {
            if (isHTML.contentEquals("true"))
                ongoing.addHeader("Accept", "application/vnd.github.VERSION.html")
            ongoing.removeHeader("isHTML")
        } else
            ongoing.addHeader("Accept", "application/json;versions=1")

        return ongoing.build()
    }

    fun handleGetPutDeleteRequest(chain: Interceptor.Chain) {
        val request = chain.request()
        val response = chain.proceed(request)

        if (request.url().toString().contains("/user/following/")) {
            if (response?.request()?.method() == "GET") {
                if (response.code() == 204 && response.message()!!.contentEquals("No Content"))
                    throw FollowerException("you are followers of this user")
                else if (response.code() == 404 && response.message()!!.contentEquals("Not Found"))
                    throw NotFollowerException("you are Not followers of this user")
            } else if (response.request().method() == "PUT") {
                if (response.code() == 204 && response.message()!!.contentEquals("No Content"))
                    throw FollowerException("Now you are followers of this user")
            } else if (response.request().method() == "DELETE") {
                if (response.code() == 204 && response.message()!!.contentEquals("No Content"))
                    throw NotFollowerException("Now you are not followers of this user")
            }
        }
        if (request.url().toString().contains("/user/starred/")) {
            if (response?.request()?.method() == "GET") {
                if (response.code() == 204 && response.message()!!.contentEquals("No Content"))
                    throw StarringException("you are starring of this repo")
                else if (response.code() == 404 && response.message()!!.contentEquals("Not Found"))
                    throw NotStarringException("you are Not starring of this repo")
            } else if (response?.request()?.method() == "PUT") {
                if (response.code() == 204 && response.message()!!.contentEquals("No Content"))
                    throw StarringException("Now you are starring of this repo")
            } else if (response?.request()?.method() == "DELETE") {
                if (response.code() == 204 && response.message()!!.contentEquals("No Content"))
                    throw NotStarringException("Now you are Not starring of this repo")
            }
        }
        //Check if user is subscriber/watcher of a Repo
        if (request.url().toString().contains("/user/subscriptions/")) {
            if (response?.request()?.method() == "GET") {
                if (response.code() == 204 && response.message()!!.contentEquals("No Content"))
                    throw SubscribedException("you are watcher of this repo")
                else if (response.code() == 404 && response.message()!!.contentEquals("Not Found"))
                    throw NotSubscribedException("you are Not watcher of this repo")
            } else if (response?.request()?.method() == "PUT") {
                if (response.code() == 204 && response.message()!!.contentEquals("No Content"))
                    throw SubscribedException("Now you are watcher of this repo")
            } else if (response?.request()?.method() == "DELETE") {
                if (response.code() == 204 && response.message()!!.contentEquals("No Content"))
                    throw NotSubscribedException("Now you are Not watcher of this repo")
            }
        }

    }

    fun handleResponseErrors(response: Response) {
        when (response.code()) {
            404 -> //Forbidden  API rate limit exceeded for user
                throw FileNotFoundException("This item is not available")
            403 -> //File not found
                throw ApiRateLimitExceedException("Api Rate Limit Exceed for this user, Please retry..")
        /* 404 -> if (!request.url().toString().endsWith("/readme"))
             throw FileNotFoundException("This content is not available now")*/
            401 ->
                throw UserUnAuthorizedException("Unauthorized")
        }
    }

}