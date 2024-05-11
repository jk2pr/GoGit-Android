package com.jk.gogit.organisation

import com.apollographql.apollo3.ApolloClient
import com.hoppers.GetUserOrganizationQuery
import com.hoppers.fragment.Org
import javax.inject.Inject

class OrgExecutor
@Inject constructor(private val client: ApolloClient) {

    suspend fun execute(user: String): List<Org> =
        client.query(GetUserOrganizationQuery(user))
            .execute().data?.user?.organizations?.nodes?.map {
            it?.org as Org
        }.orEmpty()


}
