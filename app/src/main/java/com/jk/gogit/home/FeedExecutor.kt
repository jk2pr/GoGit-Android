package com.jk.gogit.home

import com.jk.gogit.extensions.formatDateRelativeToToday
import com.jk.gogit.extensions.toDate
import com.jk.gogit.home.model.Feed
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

    suspend fun execute(user:String = "jk2pr",page: Int, perPage: Int) : List<Feed> {
       val response =  client.get {
               // url("/events")
                url("users/$user/received_events")
                    parameter("page", page)
                    parameter("per_page", perPage)

            }.body<List<Feed>>()
        return response.map { feed ->
            feed.copy(createdAt = feed.createdAt.toDate().formatDateRelativeToToday())
        }
    }
        // val userProfile = iApi.getMyProfile()
        //val feeds = iApi.getFeed(userProfile.login, 1, BaseActivity.sMaxRecord)
    }