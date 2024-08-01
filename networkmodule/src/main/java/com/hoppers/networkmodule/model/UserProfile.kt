package com.hoppers.networkmodule.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    @SerialName("gravatar_id") val gravatarId: String? = null,
    @SerialName("twitter_username") val twitterUsername: String? = null,
    @SerialName("disk_usage") val diskUsage: Int = 0,
    val hireable: String? = null,
    @SerialName("organizations_url") val organizationsUrl: String? = null,
    @SerialName("gists_url") val gistsUrl: String? = null,
    @SerialName("received_events_url") val receivedEventsUrl: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("repos_url") val reposUrl: String? = null,
    @SerialName("html_url") val htmlUrl: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    val id: String? = null,
    val bio: String? = null,
    val url: String? = null,
    val blog: String? = null,
    val name: String? = null,
    @SerialName("plan")
    val plan: String? = null,
    val type: String? = null,
    val email: String? = null,
    val login: String? = null,
    val collaborators: String? = null,
    @SerialName("site_admin") val siteAdmin: String? = null,
    val followers: Int=0,
    val following: Int=0,
    @SerialName("public_gists") val publicGists: Int=0,
    @SerialName("public_repos") val publicRepos: Int=0,
    val company: String? = null,
    @SerialName("private_gists") val privateGists: Int=0,
    @SerialName("events_url") val eventsUrl: String? = null,
    @SerialName("starred_url") val starredUrl: String? = null,
    @SerialName("following_url") val followingUrl: String? = null,
    @SerialName("subscriptions_url") val subscriptionsUrl: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("owned_private_repos") val ownedPrivateRepos: Int=0,
    @SerialName("two_factor_authentication") val twoFactorAuthentication: String? = null,
    @SerialName("total_private_repos") val totalPrivateRepos: Int=0,
    val location: String? = null,
    @SerialName("followers_url") val followersUrl: String? = null,
    @SerialName("node_id") val nodeId: String? = null
)
