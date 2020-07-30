package com.jk.gogit.model
import com.google.gson.annotations.SerializedName



data class File(
		@SerializedName("name") val name: String,
		@SerializedName("path") val path: String,
		@SerializedName("sha") val sha: String, //
		@SerializedName("size") val size: Int, //0
		@SerializedName("url") val url: String, //
		@SerializedName("html_url") val htmlUrl: String,
		@SerializedName("git_url") val gitUrl: String,
		@SerializedName("type") val type: String, //dir
		@SerializedName("_links") val links: Links
) {
	data class Links(
			@SerializedName("self") val self: String,
			@SerializedName("git") val git: String,
			@SerializedName("html") val html: String
	)
}

data class IndividualFile(@SerializedName("name") val name: String,
						  @SerializedName("path") val path: String,
						  @SerializedName("sha") val sha: String, //
						  @SerializedName("size") val size: Int, //0
						  @SerializedName("url") val url: String, //
						  @SerializedName("html_url") val htmlUrl: String,
						  @SerializedName("git_url") val gitUrl: String,
						  @SerializedName("type") val type: String, //dir
						  @SerializedName("download_url") val download_url: String, //dir
						  @SerializedName("content") val content: String, //dir
						  @SerializedName("encoding") val encoding: String, //dir
						  @SerializedName("_links") val links: File.Links)