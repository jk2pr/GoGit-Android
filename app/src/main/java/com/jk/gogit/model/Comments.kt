package com.jk.gogit.model
import com.google.gson.annotations.SerializedName


data class Comments(
		@SerializedName("url") val url: String?,
		@SerializedName("html_url") val htmlUrl: String?,
		@SerializedName("issue_url") val issueUrl: String?,
		@SerializedName("id") val id: Int?,
		@SerializedName("user") val user: Issue.User?,
		@SerializedName("created_at") val createdAt: String?,
		@SerializedName("updated_at") val updatedAt: String?,
		@SerializedName("author_association") val authorAssociation: String?,
		@SerializedName("body") val body: String?
)