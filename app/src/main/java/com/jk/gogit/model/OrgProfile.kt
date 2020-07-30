package com.jk.gogit.model


import com.google.gson.annotations.SerializedName

data class OrgProfile(@SerializedName("repos_url")
                      val reposUrl: String?,
                      @SerializedName("has_repository_projects")
                      val hasRepositoryProjects: Boolean,
                      @SerializedName("members_url")
                      val membersUrl: String?,
                      @SerializedName("description")
                      val description: String?,
                      @SerializedName("created_at")
                      val createdAt: String?,
                      @SerializedName("login")
                      val login: String?,
                      @SerializedName("blog")
                      val blog: String?,
                      @SerializedName("type")
                      val type: String?,
                      @SerializedName("public_members_url")
                      val publicMembersUrl: String?,
                      @SerializedName("total_private_repos")
                      val totalPrivateRepos: Int,
                      @SerializedName("private_gists")
                      val privateGists: Int,
                      @SerializedName("billing_email")
                      val billingEmail: String?,
                      @SerializedName("disk_usage")
                      val diskUsage: Int,
                      @SerializedName("collaborators")
                      val collaborators: Int,
                      @SerializedName("company")
                      val company: String?,
                      @SerializedName("owned_private_repos")
                      val ownedPrivateRepos: Int,
                      @SerializedName("id")
                      val id: Int,
                      @SerializedName("public_repos")
                      val publicRepos: Int,
                      @SerializedName("plan")
                      val plan: Plan,
                      @SerializedName("email")
                      val email: String?,
                      @SerializedName("members_can_create_repositories")
                      val membersCanCreateRepositories: Boolean,
                      @SerializedName("default_repository_settings")
                      val defaultRepositorySettings: String? ,
                      @SerializedName("public_gists")
                      val publicGists: Int,
                      @SerializedName("url")
                      val url: String?,
                      @SerializedName("issues_url")
                      val issuesUrl: String?,
                      @SerializedName("followers")
                      val followers: Int,
                      @SerializedName("avatar_url")
                      val avatarUrl: String?,
                      @SerializedName("events_url")
                      val eventsUrl: String?,
                      @SerializedName("has_organization_projects")
                      val hasOrganizationProjects: Boolean,
                      @SerializedName("following")
                      val following: Int,
                      @SerializedName("html_url")
                      val htmlUrl: String?,
                      @SerializedName("name")
                      val name: String?,
                      @SerializedName("location")
                      val location: String?,
                      @SerializedName("hooks_url")
                      val hooksUrl: String?)


data class Plan(@SerializedName("private_repos")
                val privateRepos: Int,
                @SerializedName("name")
                val name: String?,
                @SerializedName("space")
                val space: Int)


