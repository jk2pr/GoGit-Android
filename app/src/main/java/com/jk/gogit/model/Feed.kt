package com.jk.gogit.model

/**
 * @author Jitendra Prajapati (jk2praj@gmail.com) on 02/03/2018 (MM/DD/YYYY )
 */

data class Feed(
        val id: String,
        val type: String,
        val actor: Actor,
        val repo: Repo,
        val payload: Payload,
        val public: Boolean,
        val created_at: String
) {
    data class Payload(
            val ref: Any,
            val ref_type: String,
            val master_branch: String,
            val description: Any,
            val pusher_type: String
    )

    data class Actor(
            val id: Int,
            val login: String,
            val display_login: String,
            val gravatar_id: String,
            val url: String,
            val avatar_url: String
    )

    data class Repo(
            val id: Int,
            val name: String,
            val url: String
    )
}