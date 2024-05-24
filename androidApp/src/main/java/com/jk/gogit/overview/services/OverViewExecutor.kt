/*
package com.jk.gogit.overview.services

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.hoppers.ReadMeQuery
import com.hoppers.GetUserInfoQuery
import com.hoppers.type.PinnableItem
import com.jk.gogit.overview.model.OverViewTabData
import javax.inject.Inject

class OverViewExecutor
@Inject constructor(private val client: ApolloClient) {

    */
/**
     * Show loading
     * Get Data from network
     * Show FinalData
     *//*


    suspend fun execute(user: String): OverViewTabData {

        val html: String = client.query(ReadMeQuery(user))
            .execute().data?.repository?.`object`?.onBlob?.text.toString()

        Log.d("OverViewExecutor", "execute html: $html")

            //  val feeds = client.query(PinnedRepo(user)).execute().data?.user?.pinnedItems?.nodes
            //       ?: emptyList()

            //Log.d("OverViewExecutor", "execute feed: $feeds")
       //     return null
        OverViewTabData(html, emptyList())

        }
    }

// val userProfile = iApi.getMyProfile()
//val feeds = iApi.getFeed(userProfile.login, 1, BaseActivity.sMaxRecord)


*/
