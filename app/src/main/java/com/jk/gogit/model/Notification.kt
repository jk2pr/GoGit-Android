package com.jk.gogit.model

import com.google.gson.annotations.SerializedName

data class NotificationOwner(@SerializedName("gists_url")
                 val gistsUrl: String = "",
                 @SerializedName("repos_url")
                 val reposUrl: String = "",
                 @SerializedName("following_url")
                 val followingUrl: String = "",
                 @SerializedName("starred_url")
                 val starredUrl: String = "",
                 @SerializedName("login")
                 val login: String = "",
                 @SerializedName("followers_url")
                 val followersUrl: String = "",
                 @SerializedName("type")
                 val type: String = "",
                 @SerializedName("url")
                 val url: String = "",
                 @SerializedName("subscriptions_url")
                 val subscriptionsUrl: String = "",
                 @SerializedName("received_events_url")
                 val receivedEventsUrl: String = "",
                 @SerializedName("avatar_url")
                 val avatarUrl: String = "",
                 @SerializedName("events_url")
                 val eventsUrl: String = "",
                 @SerializedName("html_url")
                 val htmlUrl: String = "",
                 @SerializedName("site_admin")
                 val siteAdmin: Boolean = false,
                 @SerializedName("id")
                 val id: Int = 0,
                 @SerializedName("gravatar_id")
                 val gravatarId: String = "",
                 @SerializedName("organizations_url")
                 val organizationsUrl: String = "")


data class Repository(@SerializedName("owner")
                      val owner: NotificationOwner,
                      @SerializedName("private")
                      val private: Boolean = false,
                      @SerializedName("fork")
                      val fork: Boolean = false,
                      @SerializedName("full_name")
                      val fullName: String = "",
                      @SerializedName("html_url")
                      val htmlUrl: String = "",
                      @SerializedName("name")
                      val name: String = "",
                      @SerializedName("description")
                      val description: String = "",
                      @SerializedName("id")
                      val id: Int = 0,
                      @SerializedName("url")
                      val url: String = "")


data class Subject(@SerializedName("latest_comment_url")
                   val latestCommentUrl: String = "",
                   @SerializedName("title")
                   val title: String = "",
                   @SerializedName("type")
                   val type: String = "",
                   @SerializedName("url")
                   val url: String = "")


data class Notification(@SerializedName("reason")
                        val reason: String = "",
                        @SerializedName("updated_at")
                        val updatedAt: String = "",
                        @SerializedName("unread")
                        val unread: Boolean = false,
                        @SerializedName("subject")
                        val subject: Subject,
                        @SerializedName("id")
                        val id: String = "",
                        @SerializedName("repository")
                        val repository: Repository,
                        @SerializedName("last_read_at")
                        val lastReadAt: String = "",
                        @SerializedName("url")
                        val url: String = "")


