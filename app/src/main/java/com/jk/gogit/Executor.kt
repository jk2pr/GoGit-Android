package com.jk.gogit

import com.hoppers.networkmodule.network.ktorHttpClient
import io.ktor.client.HttpClient

interface Executor {
     val client: HttpClient

     suspend fun execute(params:MutableMap<Any,Any>)
}