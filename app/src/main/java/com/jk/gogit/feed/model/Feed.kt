package com.jk.gogit.feed.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Jitendra Prajapati (jk2praj@gmail.com) on 02/03/2018 (MM/DD/YYYY )
 */

@Serializable
data class Feed(
    val id: String,
    val type: String,
    val actor: Actor,
    val repo: Repo,
    val payload: Payload? = null,
    val public: Boolean,
    @SerialName("created_at")
    val createdAt: String,
    val org: Org? = null
) {
    @Serializable
    data class Payload(
        // val ref: Any,
        val action: String? = null,
        @SerialName(value = "ref_type")
        val refType: String? = null,
        @SerialName(value = "master_branch")
        val masterBranch: String? = null,
        val description: String? = null,
        @SerialName(value = "pusher_type")
        val pusherType: String? = null,
    )

    @Serializable
    data class Org(
        val id: Int,
        val login: String,
        @SerialName(value = "gravatar_id")
        val gravatarId: String,
        @SerialName(value = "avatar_url")
        val avatarUrl: String,
        val url: String
    )

    @Serializable
    data class Actor(
        val id: Int,
        val login: String,
        val display_login: String,
        val gravatar_id: String,
        val url: String,
        val avatar_url: String
    )

    @Serializable
    data class Repo(
        val id: Int,
        val name: String,
        val url: String
    )

    fun getEventName(): String {
        return when (type) {
            "CreateEvent" ->
                "created a Repository"

            "DeleteEvent" ->
                "deleted a Repository"

            "MemberEvent" ->
                "added a member to Repository"

            "ReleaseEvent" ->
                "published a Release"

            "ForkEvent" ->
                "forked a Repository"

            "WatchEvent" ->
                "${payload?.action} watching a repository"

            "PushEvent" ->
                "pushed to"

            "PullRequestEvent" ->
                "Closed pull request"

            "PublicEvent" ->
                "Made public"

            else -> {
                type
            }
        }
    }
}