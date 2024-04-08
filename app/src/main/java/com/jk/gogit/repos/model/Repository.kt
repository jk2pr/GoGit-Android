package com.jk.gogit.repos.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Owner(
    @SerialName("gists_url")
    val gistsUrl: String,
    @SerialName("repos_url")
    val reposUrl: String,
    @SerialName("following_url")
    val followingUrl: String,
    @SerialName("starred_url")
    val starredUrl: String,
    val login: String,
    @SerialName("followers_url")
    val followersUrl: String,
    val type: String,
    val url: String,
    @SerialName("subscriptions_url")
    val subscriptionsUrl: String,
    @SerialName("received_events_url")
    val receivedEventsUrl: String,
    @SerialName("avatar_url")
    val avatarUrl: String,
    @SerialName("events_url")
    val eventsUrl: String,
    @SerialName("html_url")
    val htmlUrl: String,
    @SerialName("site_admin")
    val siteAdmin: Boolean,
    val id: Int,
    @SerialName("gravatar_id")
    val gravatarId: String,
    @SerialName("organizations_url")
    val organizationsUrl: String
)

@Serializable
data class Repo(
    @SerialName("stargazers_count")
    val stargazersCount: Int,
    @SerialName("pushed_at")
    val pushedAt: String,
    @SerialName("subscription_url")
    val subscriptionUrl: String,
    val language: String? = null,
    @SerialName("branches_url")
    val branchesUrl: String,
    @SerialName("issue_comment_url")
    val issueCommentUrl: String,
    @SerialName("labels_url")
    val labelsUrl: String,
    @SerialName("subscribers_url")
    val subscribersUrl: String,
    val permissions: Permissions? = null,
    @SerialName("releases_url")
    val releasesUrl: String,
    @SerialName("svn_url")
    val svnUrl: String,
    val id: Int,
    val forks: Int,
    @SerialName("archive_url")
    val archiveUrl: String,
    @SerialName("git_refs_url")
    val gitRefsUrl: String,
    @SerialName("forks_url")
    val forksUrl: String,
    @SerialName("statuses_url")
    val statusesUrl: String,
    @SerialName("ssh_url")
    val sshUrl: String,
    val license: License? = null,
    @SerialName("full_name")
    val fullName: String,
    val size: Int,
    @SerialName("languages_url")
    val languagesUrl: String,
    @SerialName("html_url")
    val htmlUrl: String,
    @SerialName("collaborators_url")
    val collaboratorsUrl: String,
    @SerialName("clone_url")
    val cloneUrl: String,
    val name: String,
    @SerialName("pulls_url")
    val pullsUrl: String,
    @SerialName("default_branch")
    val defaultBranch: String,
    @SerialName("hooks_url")
    val hooksUrl: String,
    @SerialName("trees_url")
    val treesUrl: String,
    @SerialName("tags_url")
    val tagsUrl: String,
    val private: Boolean,
    @SerialName("contributors_url")
    val contributorsUrl: String,
    @SerialName("has_downloads")
    val hasDownloads: Boolean,
    @SerialName("notifications_url")
    val notificationsUrl: String,
    @SerialName("open_issues_count")
    val openIssuesCount: Int,
    val description: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    val watchers: Int,
    @SerialName("keys_url")
    val keysUrl: String,
    @SerialName("deployments_url")
    val deploymentsUrl: String,
    @SerialName("has_projects")
    val hasProjects: Boolean,
    val archived: Boolean,
    @SerialName("has_wiki")
    val hasWiki: Boolean,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("comments_url")
    val commentsUrl: String,
    @SerialName("stargazers_url")
    val stargazersUrl: String,
    @SerialName("git_url")
    val gitUrl: String,
    @SerialName("has_pages")
    val hasPages: Boolean,
    val owner: Owner,
    @SerialName("commits_url")
    val commitsUrl: String,
    @SerialName("compare_url")
    val compareUrl: String,
    @SerialName("git_commits_url")
    val gitCommitsUrl: String,
    @SerialName("blobs_url")
    val blobsUrl: String,
    @SerialName("git_tags_url")
    val gitTagsUrl: String,
    @SerialName("merges_url")
    val mergesUrl: String,
    @SerialName("downloads_url")
    val downloadsUrl: String,
    @SerialName("has_issues")
    val hasIssues: Boolean,
    val url: String,
    @SerialName("contents_url")
    val contentsUrl: String,
    @SerialName("mirror_url")
    val mirrorUrl: String? = null,
    @SerialName("milestones_url")
    val milestonesUrl: String,
    @SerialName("teams_url")
    val teamsUrl: String,
    val fork: Boolean,
    @SerialName("issues_url")
    val issuesUrl: String,
    @SerialName("events_url")
    val eventsUrl: String,
    @SerialName("issue_events_url")
    val issueEventsUrl: String,
    @SerialName("assignees_url")
    val assigneesUrl: String,
    @SerialName("open_issues")
    val openIssues: Int,
    @SerialName("watchers_count")
    val watchersCount: Int,
    val homepage: String? = null,
    @SerialName("forks_count")
    val forksCount: Int
)

@Serializable
data class Permissions(val pull: Boolean, val admin: Boolean, val push: Boolean)

@Serializable
data class License(val key: String, val name: String,@SerialName("spdx_id") val spdxId: String, val url: String?)