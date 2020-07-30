package com.jk.gogit.model

import com.google.gson.annotations.SerializedName
import com.jk.gogit.ui.Type

data class TimeLine(
        var parentIssue: Issue?,
    @SerializedName("id") var id: Int?,
    @SerializedName("node_id") var nodeId: String?,
    @SerializedName("url") var url: String?,
    @SerializedName("actor") var actor: Actor?,
    @SerializedName("author") var author: Author?,
    @SerializedName("event") var event: Type?,
    @SerializedName("commit_id") var commitId: String?,
    @SerializedName("commit_url") var commitUrl: Any?,
    @SerializedName("created_at") var createdAt: String?,
    @SerializedName("submitted_at") var submittedAt: String?,
    @SerializedName("rename") var rename: Rename?,
    @SerializedName("message") var message: String?,
    @SerializedName("html_url") var htmlUrl: String?,
    @SerializedName("issue_url") var issueUrl: String?,
    @SerializedName("user") var user: User?,
    @SerializedName("updated_at") var updatedAt: String?,
    @SerializedName("author_association") var authorAssociation: String?,
    @SerializedName("body") var body: String?,
    @SerializedName("reactions") var reactions: Reactions?,
    @SerializedName("label") var label: Issue.Label?
) {
    data class User(
        @SerializedName("login") var login: String?,
        @SerializedName("id") var id: Int?,
        @SerializedName("node_id") var nodeId: String?,
        @SerializedName("avatar_url") var avatarUrl: String?,
        @SerializedName("gravatar_id") var gravatarId: String?,
        @SerializedName("url") var url: String?,
        @SerializedName("html_url") var htmlUrl: String?,
        @SerializedName("followers_url") var followersUrl: String?,
        @SerializedName("following_url") var followingUrl: String?,
        @SerializedName("gists_url") var gistsUrl: String?,
        @SerializedName("starred_url") var starredUrl: String?,
        @SerializedName("subscriptions_url") var subscriptionsUrl: String?,
        @SerializedName("organizations_url") var organizationsUrl: String?,
        @SerializedName("repos_url") var reposUrl: String?,
        @SerializedName("events_url") var eventsUrl: String?,
        @SerializedName("received_events_url") var receivedEventsUrl: String?,
        @SerializedName("type") var type: String?,
        @SerializedName("site_admin") var siteAdmin: Boolean?
    )

    data class Rename(
        @SerializedName("from") var from: String?,
        @SerializedName("to") var to: String?
    )
    data class Author(
        @SerializedName("name") var name: String?,
        @SerializedName("email") var email: String?,
        @SerializedName("date") var date: String?
    )

    data class Actor(
        @SerializedName("login") var login: String?,
        @SerializedName("id") var id: Int?,
        @SerializedName("node_id") var nodeId: String?,
        @SerializedName("avatar_url") var avatarUrl: String?,
        @SerializedName("gravatar_id") var gravatarId: String?,
        @SerializedName("url") var url: String?,
        @SerializedName("html_url") var htmlUrl: String?,
        @SerializedName("followers_url") var followersUrl: String?,
        @SerializedName("following_url") var followingUrl: String?,
        @SerializedName("gists_url") var gistsUrl: String?,
        @SerializedName("starred_url") var starredUrl: String?,
        @SerializedName("subscriptions_url") var subscriptionsUrl: String?,
        @SerializedName("organizations_url") var organizationsUrl: String?,
        @SerializedName("repos_url") var reposUrl: String?,
        @SerializedName("events_url") var eventsUrl: String?,
        @SerializedName("received_events_url") var receivedEventsUrl: String?,
        @SerializedName("type") var type: String?,
        @SerializedName("site_admin") var siteAdmin: Boolean?
    )

    data class Reactions(
        @SerializedName("url") var url: String?,
        @SerializedName("total_count") var totalCount: Int?,
        @SerializedName("+1") var up: Int?,
        @SerializedName("-1") var down: Int?,
        @SerializedName("laugh") var laugh: Int?,
        @SerializedName("hooray") var hooray: Int?,
        @SerializedName("confused") var confused: Int?,
        @SerializedName("heart") var heart: Int?
    )
}