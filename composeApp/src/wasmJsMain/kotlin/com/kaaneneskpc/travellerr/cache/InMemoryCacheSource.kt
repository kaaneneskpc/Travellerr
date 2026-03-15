package com.kaaneneskpc.travellerr.cache

import com.kaaneneskpc.data.dataSource.CacheDataSource
import kotlinx.browser.localStorage
import org.w3c.dom.get
import org.w3c.dom.set


class InMemoryCacheSource : CacheDataSource {

    companion object {
        private const val AUTH_TOKEN_KEY = "auth_token"
    }

    override suspend fun saveAuthToken(token: String) {
        localStorage[AUTH_TOKEN_KEY] = token
    }

    override suspend fun getAuthToken(): String? {
        return localStorage[AUTH_TOKEN_KEY]
    }

    override suspend fun clearAuthToken() {
        localStorage[AUTH_TOKEN_KEY]
    }
}