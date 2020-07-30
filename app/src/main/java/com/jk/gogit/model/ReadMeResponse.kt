package com.jk.gogit.model
import com.google.gson.annotations.SerializedName



data class ReadMeResponse(
		@SerializedName("type") val type: String,
		@SerializedName("encoding") val encoding: String,
		@SerializedName("size") val size: Int,
		@SerializedName("name") val name: String,
		@SerializedName("path") val path: String,
		@SerializedName("content") val content: String,
		@SerializedName("sha") val sha: String,
		@SerializedName("url") val url: String,
		@SerializedName("git_url") val gitUrl: String,
		@SerializedName("html_url") val htmlUrl: String,
		@SerializedName("download_url") val downloadUrl: String,
		@SerializedName("_links") val links: Links
)

data class Links(
		@SerializedName("git") val git: String,
		@SerializedName("self") val self: String,
		@SerializedName("html") val html: String
)