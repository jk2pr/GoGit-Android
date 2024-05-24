package com.jk.gogit

import io.ktor.client.HttpClient

interface Executor {
     val client: HttpClient

     suspend fun execute(params:MutableMap<Any,Any>)
}