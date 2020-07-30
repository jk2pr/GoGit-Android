package com.jk.gogit.model
import com.google.gson.annotations.SerializedName



data class Issue(
		@SerializedName("id") var id: Int,
		@SerializedName("url") var url: String,
		@SerializedName("repository_url") val repositoryUrl: String,
		@SerializedName("labels_url") val labelsUrl: String,
		@SerializedName("comments_url") val commentsUrl: String,
		@SerializedName("events_url") val eventsUrl: String,
		@SerializedName("html_url") var htmlUrl: String,
		@SerializedName("number") val number: Int,
		@SerializedName("state") val state: String,
		@SerializedName("title") val title: String,
		@SerializedName("body") val body: String,
		@SerializedName("user") var user: User,
		@SerializedName("labels") val labels: List<Label>,
		@SerializedName("assignee") val assignee: Assignee,
		@SerializedName("assignees") val assignees: List<Assignee>,
		@SerializedName("milestone") val milestone: Milestone,
		@SerializedName("locked") val locked: Boolean,
		@SerializedName("active_lock_reason") val activeLockReason: String,
		@SerializedName("comments") val comments: Int,
		@SerializedName("pull_request") val pullRequest: PullRequest,
		@SerializedName("created_at") var createdAt: String,
		@SerializedName("updated_at") var updatedAt: String,
		@SerializedName("body_html") var body_html: String?
) {
	data class User(
			@SerializedName("login") var login: String,
			@SerializedName("id") var id: Int,
			@SerializedName("avatar_url") var avatarUrl: String,
			@SerializedName("gravatar_id") val gravatarId: String,
			@SerializedName("url") val url: String,
			@SerializedName("html_url") val htmlUrl: String,
			@SerializedName("followers_url") val followersUrl: String,
			@SerializedName("following_url") val followingUrl: String,
			@SerializedName("gists_url") val gistsUrl: String,
			@SerializedName("starred_url") val starredUrl: String,
			@SerializedName("subscriptions_url") val subscriptionsUrl: String,
			@SerializedName("organizations_url") val organizationsUrl: String,
			@SerializedName("repos_url") val reposUrl: String,
			@SerializedName("events_url") val eventsUrl: String,
			@SerializedName("received_events_url") val receivedEventsUrl: String,
			@SerializedName("type") val type: String,
			@SerializedName("site_admin") val siteAdmin: Boolean
	)

	data class Label(
			@SerializedName("id") val id: Int,
			@SerializedName("url") val url: String,
			@SerializedName("name") val name: String,
			@SerializedName("description") val description: String,
			@SerializedName("color") val color: String,
			@SerializedName("default") val default: Boolean
	)

	data class PullRequest(
			@SerializedName("url") val url: String,
			@SerializedName("html_url") val htmlUrl: String,
			@SerializedName("diff_url") val diffUrl: String,
			@SerializedName("patch_url") val patchUrl: String
	)

	data class Assignee(
			@SerializedName("login") val login: String,
			@SerializedName("id") val id: Int,
			@SerializedName("avatar_url") val avatarUrl: String,
			@SerializedName("gravatar_id") val gravatarId: String,
			@SerializedName("url") val url: String,
			@SerializedName("html_url") val htmlUrl: String,
			@SerializedName("followers_url") val followersUrl: String,
			@SerializedName("following_url") val followingUrl: String,
			@SerializedName("gists_url") val gistsUrl: String,
			@SerializedName("starred_url") val starredUrl: String,
			@SerializedName("subscriptions_url") val subscriptionsUrl: String,
			@SerializedName("organizations_url") val organizationsUrl: String,
			@SerializedName("repos_url") val reposUrl: String,
			@SerializedName("events_url") val eventsUrl: String,
			@SerializedName("received_events_url") val receivedEventsUrl: String,
			@SerializedName("type") val type: String,
			@SerializedName("site_admin") val siteAdmin: Boolean
	)

	data class Milestone(
			@SerializedName("url") val url: String,
			@SerializedName("html_url") val htmlUrl: String,
			@SerializedName("labels_url") val labelsUrl: String,
			@SerializedName("id") val id: Int,
			@SerializedName("number") val number: Int,
			@SerializedName("state") val state: String,
			@SerializedName("title") val title: String,
			@SerializedName("description") val description: String,
			@SerializedName("creator") val creator: Creator,
			@SerializedName("open_issues") val openIssues: Int,
			@SerializedName("closed_issues") val closedIssues: Int,
			@SerializedName("created_at") val createdAt: String,
			@SerializedName("updated_at") val updatedAt: String,
			@SerializedName("closed_at") val closedAt: String,
			@SerializedName("due_on") val dueOn: String
	) {
		data class Creator(
				@SerializedName("login") val login: String,
				@SerializedName("id") val id: Int,
				@SerializedName("avatar_url") val avatarUrl: String,
				@SerializedName("gravatar_id") val gravatarId: String,
				@SerializedName("url") val url: String,
				@SerializedName("html_url") val htmlUrl: String,
				@SerializedName("followers_url") val followersUrl: String,
				@SerializedName("following_url") val followingUrl: String,
				@SerializedName("gists_url") val gistsUrl: String,
				@SerializedName("starred_url") val starredUrl: String,
				@SerializedName("subscriptions_url") val subscriptionsUrl: String,
				@SerializedName("organizations_url") val organizationsUrl: String,
				@SerializedName("repos_url") val reposUrl: String,
				@SerializedName("events_url") val eventsUrl: String,
				@SerializedName("received_events_url") val receivedEventsUrl: String,
				@SerializedName("type") val type: String,
				@SerializedName("site_admin") val siteAdmin: Boolean
		)
	}
}