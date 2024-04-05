package com.jk.gogit.overview.model

import com.hoppers.GetUserQuery
import com.hoppers.fragment.GistFields
import com.hoppers.fragment.Repos

data class OverViewTabData(
    val user: GetUserQuery.User,
    val html: String,
    val listType: String,
    val pinnedRepos: List<Repos>,
    val pinnedGists: List<GistFields>,
    val popularRepos: List<Repos>,
) {
    fun toOverViewScreenData(): OverViewScreenData {
        val pinnedRepoItems =
            pinnedRepos.map { OverViewScreenData.OverViewItem.PinnedRepository(it) }
        val pinnedGistItems = pinnedGists.map { OverViewScreenData.OverViewItem.PinnedGist(it) }
        val popularRepoItems =
            popularRepos.map { OverViewScreenData.OverViewItem.PopularRepository(it) }
        val allItems = pinnedRepoItems + pinnedGistItems + popularRepoItems
        return OverViewScreenData(
            user = user,
            html = html,
            listType = listType,
            list = allItems,
        )
    }


    data class OverViewScreenData(
        val user: GetUserQuery.User,
        val html: String,
        val listType: String,
        val list: List<OverViewItem>,
    ) {
        sealed class OverViewItem {
            data class PinnedRepository(val repo: Repos) : OverViewItem()
            data class PinnedGist(val gist: GistFields) : OverViewItem()
            data class PopularRepository(val repo: Repos) : OverViewItem()
        }
    }
}
