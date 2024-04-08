package com.jk.gogit.profile.services

import com.apollographql.apollo3.ApolloClient
import com.hoppers.GetPinnedItemsQuery
import com.hoppers.GetStarredReposQuery
import com.hoppers.GetUserQuery
import com.hoppers.ReadMeQuery
import com.hoppers.fragment.GistFields
import com.hoppers.fragment.Repos
import com.jk.gogit.overview.model.OverViewTabData
import javax.inject.Inject

class UserProfileExecutor
@Inject constructor(private val client: ApolloClient) {

    /**
     * Fetch user data, README content, and pinned/starred items.
     */
    suspend fun execute(login: String): OverViewTabData {


        val userResponse = client.query(GetUserQuery(login)).execute()
        val user = userResponse.data?.user ?: throw userResponse.exception ?: IllegalStateException(
            "User not found"
        )

        val readmeResponse = client.query(ReadMeQuery(login)).execute()
        val html = readmeResponse.data?.repository?.`object`?.onBlob?.text ?: ""

        var listType = "Pinned"
        var pinnedRepos: List<Repos> = emptyList()
        var popularRepos: List<Repos> = emptyList()
        var pinnedGists: List<GistFields> = emptyList()

        client.query(GetPinnedItemsQuery(login))
            .execute().data?.user?.pinnedItems?.let { pinnedItems ->
            if (pinnedItems.nodes.isNullOrEmpty()) {
                listType = "Starred"
                popularRepos = client.query(GetStarredReposQuery(login))
                    .execute().data?.user?.repositories?.nodes?.mapNotNull { it?.repos }
                    .orEmpty()
            } else {
                pinnedRepos = pinnedItems.nodes.mapNotNull { it?.repos }
                pinnedGists = pinnedItems.nodes.mapNotNull { it?.gistFields }
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
}
