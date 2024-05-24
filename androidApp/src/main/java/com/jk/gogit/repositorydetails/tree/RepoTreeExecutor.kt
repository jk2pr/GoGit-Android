package com.jk.gogit.repositorydetails.tree

import com.apollographql.apollo3.ApolloClient
import com.hoppers.GetRepositoryTreeQuery
import javax.inject.Inject

class RepoTreeExecutor @Inject constructor(private val client: ApolloClient) {

    suspend fun execute(
        user: String,
        repo: String,
        path: String,
    ): List<GetRepositoryTreeQuery.Entry> {

        val r = client
            .query(GetRepositoryTreeQuery(owner = user, repo = repo, ref = path))
            .execute().data!!.repository?.`object`?.onTree?.entries?.sortedByDescending { it.type }.orEmpty()

        return r
    }
}

