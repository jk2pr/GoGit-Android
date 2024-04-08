package com.jk.gogit.users.services

import com.apollographql.apollo3.ApolloClient
import com.hoppers.GetFollowersAndFollowingQuery
import com.hoppers.fragment.UserFields
import javax.inject.Inject

class UserListExecutor
@Inject constructor(private val client: ApolloClient) {

    suspend fun execute(user: String, page: Int, perPage: Int, type: String): List<UserFields?> {
        val name = user.split("/").first()
        val query = GetFollowersAndFollowingQuery(username = name)

        val response = client.query(query).execute().data?.user

        return response?.let { usr ->
            when (type) {
                "following" -> usr.following.nodes?.map { it?.userFields }
                "followers" -> usr.followers.nodes?.map { it?.userFields }
                else -> emptyList()
            }
        } ?: emptyList()
    }


}
