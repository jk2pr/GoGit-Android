package com.jk.gogit.users.services

import com.apollographql.apollo3.ApolloClient
import com.hoppers.GetFollowersAndFollowingQuery
import com.hoppers.GetStargazersQuery
import com.hoppers.GetWatchersQuery
import com.hoppers.fragment.UserFields
import javax.inject.Inject

class UserListExecutor
@Inject constructor(private val client: ApolloClient) {

    suspend fun executeFollowersAndFollowing(
        user: String,
        page: Int,
        perPage: Int,
        type: String
    ): List<UserFields?> {
        val query = GetFollowersAndFollowingQuery(username = user)

        val response = client.query(query).execute().data?.user

        return response?.let { usr ->
            when (type) {
                "following" -> usr.following.nodes?.map { it?.userFields }
                "followers" -> usr.followers.nodes?.map { it?.userFields }
                else -> emptyList()
            }
        } ?: emptyList()
    }

    suspend fun executeFilters(
        user: String,
        repoName: String,
        page: Int,
        perPage: Int,
        filter: String
    ): List<UserFields?> {

        val query = when (filter) {
            "stargazers" -> GetStargazersQuery(owner_name = user, repoName = repoName)
            "watchers" -> GetWatchersQuery(ownerName = user, repoName = repoName)
            else -> GetWatchersQuery(ownerName = user, repoName = repoName)
        }
        val response = client.query(query).execute().dataOrThrow()
        val r = response.let { usr ->
            when (usr) {
                is GetStargazersQuery.Data -> usr.repository?.stargazers?.nodes?.map { it?.userFields }
                is GetWatchersQuery.Data -> usr.repository?.watchers?.nodes?.map { it?.userFields }
                else -> emptyList()
            }
        } ?: emptyList()
        return r
    }


}
