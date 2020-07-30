package com.jk.gogit.model

/**
 * @author Jitendra Prajapati (jk2praj@gmail.com) on 28/02/2018 (MM/DD/YYYY )
 */
data class Users(val login: String?,
                 val id: Number?,
                 val avatar_url: String?,
                 val gravatar_id: String?,
                 val url: String?,
                 val html_url: String?,
                 val followers_url: String?
                 , val following_url: String?,
                 val gists_url: String?,
                 val starred_url: String?,
                 val subscriptions_url: String?,
                 val organizations_url: String?,
                 val repos_url: String?,
                 val events_url: String?,
                 val received_events_url: String?,
                 val type: String?,
                 val site_admin: Boolean?,
        /*Will be availbe in contribution user , See Contribution fragment*/val contributions: Int?)