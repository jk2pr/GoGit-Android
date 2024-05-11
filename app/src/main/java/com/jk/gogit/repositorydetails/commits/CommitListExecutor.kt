package com.jk.gogit.repositorydetails.commits

import com.apollographql.apollo3.ApolloClient
import com.hoppers.GetCommitsQuery
import com.jk.gogit.extensions.formatDateRelativeToToday
import com.jk.gogit.extensions.toDate
import javax.inject.Inject

class CommitListExecutor @Inject constructor(private val client: ApolloClient) {

    data class CommitData(val date:String, val commits: List<GetCommitsQuery.Commit?>)
    suspend fun execute(
        user: String,
        repo: String,
        branch: String,
        page: Int,
        perPage: Int,
    ): List<CommitData> {

        val r = client
            .query(GetCommitsQuery(owner = user, name = repo, branch = branch))
            .execute().data!!.repository?.ref?.target?.onCommit?.history?.commits.orEmpty().groupBy {
                it?.committedDate.toString().toDate().formatDateRelativeToToday()

            }
      return  r.map {
            CommitData(
                date = it.key,
                commits = it.value
            )
        }

    }
}

