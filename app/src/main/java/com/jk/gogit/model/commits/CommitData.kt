package com.jk.gogit.model.commits

import com.google.gson.annotations.SerializedName

data class CommitData(@SerializedName("committer")
                      val committer: OuterCommitter,
                      @SerializedName("author")
                      val author: OuterAuthor?,
                      @SerializedName("html_url")
                      val htmlUrl: String = "",
                      @SerializedName("comments_url")
                      val commentsUrl: String = "",
                      @SerializedName("commit")
                      val commit: Commit,
                      @SerializedName("sha")
                      val sha: String = "",
                      @SerializedName("url")
                      val url: String = "",
                      @SerializedName("parents")
                      val parents: List<ParentsItem>?)

data class OuterAuthor(
        @SerializedName("gists_url")
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

data class OuterCommitter( @SerializedName("gists_url")
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


data class Commit(@SerializedName("comment_count")
                  val commentCount: Int = 0,
                  @SerializedName("committer")
                  val committer: InnerCommitter,
                  @SerializedName("author")
                  val author: Author,
                  @SerializedName("tree")
                  val tree: Tree,
                  @SerializedName("message")
                  val message: String = "",
                  @SerializedName("url")
                  val url: String = "",
                  @SerializedName("verification")
                  val verification: Verification)


data class InnerCommitter(@SerializedName("date")
                          val date: String = "",
                          @SerializedName("name")
                          val name: String = "",
                          @SerializedName("email")
                          val email: String = "")

data class Author(@SerializedName("date")
                  val date: String = "",
                  @SerializedName("name")
                  val name: String = "",
                  @SerializedName("email")
                  val email: String = "")


data class Tree(@SerializedName("sha")
                val sha: String = "",
                @SerializedName("url")
                val url: String = "")


data class Verification(@SerializedName("reason")
                        val reason: String = "",
                        @SerializedName("signature")
                        val signature: String,
                        @SerializedName("payload")
                        val payload: String,
                        @SerializedName("verified")
                        val verified: Boolean = false)


data class ParentsItem(@SerializedName("sha")
                       val sha: String = "",
                       @SerializedName("url")
                       val url: String = "")


