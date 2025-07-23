package com.example.localdatasource.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences

class DatastoreProvider{
    companion object{
        @Volatile
        private var instance: DataStore<Preferences>? = null

        fun getInstance(context: Context): DataStore<Preferences> {
            return instance ?: synchronized(this) {
                buildDataStore(context).also {
                    instance = it
                }
            }
        }

        private fun buildDataStore(context: Context): DataStore<Preferences>{
            return PreferenceDataStoreFactory.create(
                produceFile = { context.dataStoreFile("app.preferences_pb") }
            )
        }
    }
}