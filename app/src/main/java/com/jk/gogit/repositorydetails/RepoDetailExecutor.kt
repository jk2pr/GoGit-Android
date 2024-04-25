package com.jk.gogit.repositorydetails

import com.apollographql.apollo3.ApolloClient
import com.hoppers.GetRepoDetailsQuery
import com.jk.gogit.extensions.formatDateRelativeToToday
import com.jk.gogit.extensions.toDate
import javax.inject.Inject

class RepoDetailExecutor @Inject constructor(private val client: ApolloClient) {

    suspend fun execute(
        user: String,
        repo: String,
        path: String,
        page: Int,
        perPage: Int,
    ): GetRepoDetailsQuery.Repository {

        val r = client
            .query(GetRepoDetailsQuery(owner_name = user, repoName = repo, path = path))
            .execute().data?.repository!!

        return r.run {
            val modifiedPullRequests = this.pullRequests.copy(
                pr = this.pullRequests.pr?.mapNotNull { pr ->
                    pr?.copy(
                        createdAt = pr.createdAt.toString().toDate().formatDateRelativeToToday()
                    )
                }
            )
            this.copy(pullRequests = modifiedPullRequests)
        }
    }
}




