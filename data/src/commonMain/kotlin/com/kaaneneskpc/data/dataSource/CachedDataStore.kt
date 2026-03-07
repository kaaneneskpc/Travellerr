package com.kaaneneskpc.data.dataSource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    fun getAuthTokenFlow(): Flow<String?> {
        val key = stringPreferencesKey(KEY_AUTH_TOKEN)
        return dataStore.data.map { preferences -> preferences[key] }
    }
}