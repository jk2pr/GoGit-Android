package com.jk.gogit.repos.services

import com.apollographql.apollo3.ApolloClient
import com.hoppers.GetForksQuery
import com.hoppers.GetOrgReposQuery
import com.hoppers.GetUserReposQuery
import com.hoppers.GetUserStarredReposQuery
import com.hoppers.fragment.Repos
import com.jk.gogit.extensions.formatDateRelativeToToday
import com.jk.gogit.extensions.toDate
import javax.inject.Inject

class RepoExecutor
@Inject constructor(private val client: ApolloClient) {

    suspend fun execute(
        user: String,
        isStarred: Boolean = false,
        isOrg: Boolean = false,
        filter:String? = null,
        repoName: String = ""
    ): List<Repos?> {

        val response = if (isOrg)
            client.query(GetOrgReposQuery(user))
                .execute().data?.organization?.repositories?.nodes?.map {
                    it?.repos as Repos
                }
        else
            if (filter == "Forks")
                client.query(GetForksQuery(ownerName = user, repoName = repoName))
                    .execute().data?.repository?.forks?.nodes?.map {
                        it?.repos as Repos
                    }
        else
            if (isStarred)
                client.query(GetUserStarredReposQuery(user))
                    .execute().data?.user?.starredRepositories?.nodes?.map {
                        it?.repos as Repos
                    }
            else
                client.query(GetUserReposQuery(user))
                    .execute().data?.user?.repositories?.nodes?.map {
                        it?.repos as Repos
                    }




        return response?.map { repos ->
            repos.copy(updatedAt = repos.updatedAt.toString().toDate().formatDateRelativeToToday())
        }.orEmpty()
    }
}
