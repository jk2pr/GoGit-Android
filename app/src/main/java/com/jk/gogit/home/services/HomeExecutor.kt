package com.jk.gogit.home.services

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.hoppers.FollowUserMutation
import com.hoppers.GetUserPinnedItemsQuery
import com.hoppers.GetUserPopularReposQuery
import com.hoppers.GetUserQuery
import com.hoppers.ReadMeQuery
import com.hoppers.UnfollowUserMutation
import com.hoppers.fragment.GistFields
import com.hoppers.fragment.Repos
import com.jk.gogit.extensions.formatDateRelativeToToday
import com.jk.gogit.extensions.toDate
import com.jk.gogit.overview.model.OverViewTabData
import javax.inject.Inject

class HomeExecutor
@Inject constructor(private val client: ApolloClient) {

    /**
     * Fetch user data, README content, and pinned/starred items.
     */
    suspend fun execute(login: String): OverViewTabData {


        val userResponse = client.query(GetUserQuery(login)).execute()
        Log.d( "execute:" ,"${userResponse.extensions.keys}")
        val user = userResponse.data?.user ?: throw userResponse.exception ?: IllegalStateException(
            "Sorry, the user you're looking for was not found"
        )

        val readmeResponse = client.query(ReadMeQuery(login)).execute()
        val html = readmeResponse.data?.repository?.`object`?.onBlob?.text ?: ""

        var listType = "Pinned"
        var pinnedRepos: List<Repos> = emptyList()
        var popularRepos: List<Repos> = emptyList()
        var pinnedGists: List<GistFields> = emptyList()

        client.query(GetUserPinnedItemsQuery(login))
            .execute().data?.user?.pinnedItems?.let { pinnedItems ->
                if (pinnedItems.nodes.isNullOrEmpty()) {
                    listType = "Starred"
                    popularRepos = client.query(GetUserPopularReposQuery(login, first = 5))
                        .execute().data?.user?.repositories?.nodes?.mapNotNull {
                            it?.repos?.copy(
                                updatedAt = it.repos.updatedAt.toString().toDate()
                                    .formatDateRelativeToToday()
                            )
                        }
                        .orEmpty()
                } else {
                    pinnedRepos = pinnedItems.nodes.mapNotNull {
                        it?.repos?.copy(
                            updatedAt = it.repos.updatedAt.toString().toDate()
                                .formatDateRelativeToToday()
                        )
                    }
                    pinnedGists = pinnedItems.nodes.mapNotNull {
                        it?.gistFields?.copy(
                            updatedAt = it.gistFields.updatedAt.toString().toDate()
                                .formatDateRelativeToToday()
                        )
                    }
                }
            }


        return OverViewTabData(
            user = user,
            pinnedRepos = pinnedRepos,
            pinnedGists = pinnedGists,
            popularRepos = popularRepos,
            html = html,
            listType = listType
        )
    }
    suspend fun followUser(userId: String): FollowUserMutation.Data
    {
        return  client.mutation(FollowUserMutation(user = userId)).execute().dataOrThrow()


    }
    suspend fun unFollowUser(userId: String): UnfollowUserMutation.Data
    {
        return  client.mutation(UnfollowUserMutation(user = userId)).execute().dataOrThrow()


    }
}
