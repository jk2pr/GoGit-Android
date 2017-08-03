package com.jk.daggerrxkotlin.api

class NewsDataResponse(
        val author: String,
        val title: String,
        val num_comments: Int,
        val created: Long,
        val thumbnail: String,
        val url: String?
)