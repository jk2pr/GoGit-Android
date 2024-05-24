package com.jk.gogit.orgdetails

import com.apollographql.apollo3.ApolloClient
import com.hoppers.GetOrgPinnedItemsQuery
import com.hoppers.GetOrgPopularReposQuery
import com.hoppers.GetOrganizationDetailQuery
import com.hoppers.ReadMeQuery
import com.hoppers.fragment.GistFields
import com.hoppers.fragment.Repos
import com.jk.gogit.extensions.formatDateRelativeToToday
import com.jk.gogit.extensions.toDate
import com.jk.gogit.overview.model.OverViewTabData
import javax.inject.Inject

class OrgDetailsExecutor
@Inject constructor(private val client: ApolloClient) {

    /**
     * Fetch user data, README content, and pinned/starred items.
     */
    suspend fun execute(login: String): OverViewTabData {


        val userResponse = client.query(GetOrganizationDetailQuery(login)).execute()
        val org = userResponse.data?.organization ?: throw userResponse.exception ?: IllegalStateException(
           "It seems that the organization $login has enabled OAuth App access restrictions, meaning that data access to third-parties is limited."
        )

        val readmeResponse = client.query(ReadMeQuery(login)).execute()
        val html = readmeResponse.data?.repository?.`object`?.onBlob?.text ?: ""

        var listType = "Pinned"
        var pinnedRepos: List<Repos> = emptyList()
        var popularRepos: List<Repos> = emptyList()
        var pinnedGists: List<GistFields> = emptyList()

        client.query(GetOrgPinnedItemsQuery(login))
            .execute().data?.organization?.pinnedItems?.let { pinnedItems ->
                if (pinnedItems.nodes.isNullOrEmpty()) {
                    listType = "Starred"
                    popularRepos = client.query(GetOrgPopularReposQuery(login, first = 5))
                        .execute().data?.organization?.repositories?.nodes?.mapNotNull {
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
            org = org,
            pinnedRepos = pinnedRepos,
            pinnedGists = pinnedGists,
            popularRepos = popularRepos,
            html = html,
            listType = listType
        )
    }
}
