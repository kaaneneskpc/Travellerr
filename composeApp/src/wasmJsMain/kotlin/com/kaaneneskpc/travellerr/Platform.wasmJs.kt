package com.kaaneneskpc.travellerr

actual fun getPlatform(): Platform {
    return object: Platform {
        override val name: String
            get() = "WASM JS"
    }
}