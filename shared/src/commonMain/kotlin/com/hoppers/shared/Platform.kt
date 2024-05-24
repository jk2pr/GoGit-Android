package com.hoppers.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform