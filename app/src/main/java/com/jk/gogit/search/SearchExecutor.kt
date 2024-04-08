package com.jk.gogit.search

import com.apollographql.apollo3.ApolloClient
import com.hoppers.SearchThingsQuery
import com.hoppers.fragment.Repos
import com.hoppers.fragment.UserFields
import com.hoppers.type.SearchType
import javax.inject.Inject

class SearchExecutor
@Inject constructor(private val client: ApolloClient) {

    suspend fun execute(user: String, type: SearchType): List<Any?> {
        val response = client.query(SearchThingsQuery(queryString = user, type = type)).execute()
        val nodes = response.data?.search?.nodes ?: emptyList()

        return nodes.mapNotNull { node ->
            when (type) {
                SearchType.REPOSITORY -> node?.asRepos()
                SearchType.USER -> node?.asUser()
                else -> null
            }
        }
    }

    private fun SearchThingsQuery.Node.asRepos(): Repos? {
        return this.repos
    }

    private fun SearchThingsQuery.Node.asUser(): UserFields? {
        return this.userFields

    }
}
