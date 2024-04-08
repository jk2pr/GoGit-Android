package com.jk.gogit.profile.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    @SerialName("gists_url")
    val gistsUrl: String? = null,
    @SerialName("repos_url")
    val reposUrl: String,
    @SerialName("following_url")
    val followingUrl: String? = null,
    val bio: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    val login: String,
    val type: String,
    val blog: String? = null,
    @SerialName("subscriptions_url")
    val subscriptionsUrl: String? = null,
    @SerialName("updated_at")
    val updatedAt: String,
    val siteAdmin: Boolean = false,
    val company: String? = null,
    val id: Int = 0,
    val publicRepos: Int = 0,
    @SerialName("gravatar_id")
    val gravatarId: String? = null,
    val email: String? = null,
    @SerialName("organizations_url")
    val organizationsUrl: String? = null,
    val hireable: Boolean? = null,
    @SerialName("starred_url")
    val starredUrl: String? = null,
    @SerialName("followers_url")
    val followersUrl: String? = null,
    val publicGists: Int = 0,
    val url: String,
    @SerialName("received_events_url")
    val receivedEventsUrl: String? = null,
    val followers: Int = 0,
    @SerialName("avatar_url")
    val avatarUrl: String,
    @SerialName("events_url")
    val eventsUrl: String,
    @SerialName("html_url")
    val htmlUrl: String,
    val following: Int = 0,
    val name: String? = null,
    val location: String? = null
)