package com.jk.gogit.repositorydetails

import com.apollographql.apollo3.ApolloClient
import com.hoppers.GetRepoDetailsQuery
import javax.inject.Inject

class RepoDetailExecutor @Inject constructor(private val client: ApolloClient) {

    suspend fun execute(
        user: String,
        repo: String,
        path: String,
        page: Int,
        perPage: Int,
    ): GetRepoDetailsQuery.Repository = client
        .query(GetRepoDetailsQuery(owner_name = user, repoName = repo, path = path))
        .execute().data?.repository!!
}



