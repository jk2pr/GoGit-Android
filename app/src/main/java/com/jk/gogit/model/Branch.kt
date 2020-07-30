package com.jk.gogit.model


import com.google.gson.annotations.SerializedName

data class Commit(@SerializedName("sha")
                  val sha: String = "",
                  @SerializedName("url")
                  val url: String = "")


data class Branch(@SerializedName("protected")
                  val protected: Boolean = false,
                  @SerializedName("name")
                  val name: String = "",
                  @SerializedName("commit")
                  val commit: Commit,
                  @SerializedName("protection_url")
                  val protectionUrl: String = "")


