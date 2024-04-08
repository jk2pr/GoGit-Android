package com.jk.gogit.feed.services

import com.jk.gogit.feed.model.Feed
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import javax.inject.Inject

class FeedExecutor
@Inject constructor(private val client: HttpClient) {

    /**
     * Show loading
     * Get Data from network
     * Show FinalData
     */

    suspend fun execute(user: String, page: Int, perPage: Int) : List<Feed> {
        return client.get {
                url("users/$user/received_events")
                    parameter("page", page)
                    parameter("per_page", perPage)

            }.body()
        }
        // val userProfile = iApi.getMyProfile()
        //val feeds = iApi.getFeed(userProfile.login, 1, BaseActivity.sMaxRecord)
    }
