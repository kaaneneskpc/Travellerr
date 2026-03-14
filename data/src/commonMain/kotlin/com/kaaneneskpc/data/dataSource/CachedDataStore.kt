package com.kaaneneskpc.data.dataSource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class CacheDataSource(private val dataStore: DataStore<Preferences>) {

    companion object {
        const val KEY_AUTH_TOKEN = "auth_token"
    }

    suspend fun saveAuthToken(token: String) {
        val key = stringPreferencesKey(KEY_AUTH_TOKEN)
        dataStore.edit { preferences ->
            preferences[key] = token
        }
    }

    suspend fun getAuthToken(): String? {
        val key = stringPreferencesKey(KEY_AUTH_TOKEN)
        val preferences = dataStore.data.first()
        val token = preferences[key]
        return token
    }

    suspend fun clearAuthToken() {
        val key = stringPreferencesKey(KEY_AUTH_TOKEN)
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }
}