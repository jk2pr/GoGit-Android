package com.jk.gogit.model.search

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class User(
        @PrimaryKey(autoGenerate = true)
        val uid: Long,
        val login: String,
        val avatar_url: String,
        val id: Long,
        val url: String,
        val html_url: String,
        val followers_url: String,
        val following_url: String,
        val starred_url: String,
        val gists_url: String,
        val type: String,
        val score: Double
)

data class SearchUserResult (val total_count: Long, val incomplete_results: Boolean, val items: List<User>)