package com.jk.gogit.users.services

import com.apollographql.apollo3.ApolloClient
import com.hoppers.FollowUserMutation
import com.hoppers.GetFollowersAndFollowingQuery
import com.hoppers.GetStargazersQuery
import com.hoppers.fragment.UserFields
import javax.inject.Inject

class UserListExecutor
@Inject constructor(private val client: ApolloClient) {

    suspend fun execute(user: String, page: Int, perPage: Int, type: String): List<UserFields?> {
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

    suspend fun executeStargazers(
        user: String,
        repoName: String,
        page: Int,
        perPage: Int,
    ): List<UserFields?> {
        val query = GetStargazersQuery(owner_name = user, repoName = repoName)
        val response = client.query(query).execute().data?.repository

        val r =  response?.let { usr ->
            usr.stargazers.nodes?.map {
                it?.userFields
            }
        } ?: emptyList()
        return r
    }


}
