package com.jk.gogit.profile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class LoginUser(
    val userId: String,
    val providerId: String,
    val displayName: String,
    val photoUrl: String,
    val email: String,
    val isEmailVerified: Boolean,
    val rawUserInfo: RawUserInfo
)

@Serializable
data class RawUserInfo(
    @SerialName("gists_url")
    val gistsUrl: String,
    @SerialName("repos_url")
    val reposUrl: String,
    @SerialName("two_factor_authentication")
    val twoFactorAuthentication: Boolean,
    @SerialName("following_url")
    val followingUrl: String,
    @SerialName("twitter_username")
    val twitterUsername: String? = null,
    val bio: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    val login: String,
    val type: String,
    val blog: String? = null,
    @SerialName("private_gists")
    val privateGists: Int,
    @SerialName("total_private_repos")
    val totalPrivateRepos: Int,
    @SerialName("subscriptions_url")
    val subscriptionsUrl: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("site_admin")
    val siteAdmin: Boolean,
    @SerialName("disk_usage")
    val diskUsage: Int,
    val collaborators: Int,
    val company: String? = null,
    @SerialName("owned_private_repos")
    val ownedPrivateRepos: Int,
    val id: Int,
    @SerialName("public_repos")
    val publicRepos: Int,
    @SerialName("gravatar_id")
    val gravatarId: String,
    val plan: Plan,
    val email: String? = null,
    @SerialName("organizations_url")
    val organizationsUrl: String,
    val hireable: Boolean,
    @SerialName("starred_url")
    val starredUrl: String,
    @SerialName("followers_url")
    val followersUrl: String,
    @SerialName("public_gists")
    val publicGists: Int,
    val url: String,
    @SerialName("received_events_url")
    val receivedEventsUrl: String,
    val followers: Int,
    @SerialName("avatar_url")
    val avatarUrl: String,
    @SerialName("events_url")
    val eventsUrl: String,
    @SerialName("html_url")
    val htmlUrl: String,
    val following: Int,
    val name: String? = null,
    val location: String? = null,
    @SerialName("node_id")
    val nodeId: String
)

@Serializable
data class Plan(
    @SerialName("private_repos")
    val privateRepos: Int,
    val name: String,
    val collaborators: Int,
    val space: Long
)