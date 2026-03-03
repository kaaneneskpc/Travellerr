package com.kaaneneskpc.travellerr

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform