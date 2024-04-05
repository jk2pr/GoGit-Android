package com.jk.gogit.repos.services

import com.apollographql.apollo3.ApolloClient
import com.hoppers.GetUserReposQuery
import com.hoppers.fragment.Repos
import com.jk.gogit.extensions.formatDateRelativeToToday
import com.jk.gogit.extensions.toDate
import java.util.Date
import javax.inject.Inject

class RepoExecutor
@Inject constructor(private val client: ApolloClient) {

    suspend fun execute(user: String): List<Repos?> {
        return client.query(GetUserReposQuery(user))
            .execute().data?.user?.repositories?.nodes?.map { nodes ->
                nodes?.repos?.copy(updatedAt = nodes.repos.updatedAt.toString().toDate())
            }?.map {
                it?.copy(updatedAt = (it.updatedAt as Date).formatDateRelativeToToday())
            }.orEmpty()
    }
}
