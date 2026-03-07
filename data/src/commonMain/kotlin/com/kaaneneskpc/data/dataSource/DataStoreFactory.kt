package com.kaaneneskpc.data.dataSource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath


fun createDataStore(producerPath: () -> String): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { producerPath().toPath() }
    )
}

const val dataStoreFileName = "travenor_datastore.preferences_pb"