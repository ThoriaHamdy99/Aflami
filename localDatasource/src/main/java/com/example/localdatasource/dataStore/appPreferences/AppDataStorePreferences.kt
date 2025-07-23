package com.example.localdatasource.dataStore.appPreferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.localdatasource.dataStore.appPreferences.AppDataStorePreferences.PreferenceKeys.SESSION_ID
import com.example.localdatasource.dataStore.appPreferences.AppDataStorePreferences.PreferenceKeys.SESSION_TYPE
import com.example.localdatasource.dataStore.datasource.AppPreferences
import com.example.localdatasource.dataStore.utils.CryptoData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppDataStorePreferences(
    private val datastore: DataStore<Preferences>,
    val cryptoData: CryptoData,
) : AppPreferences {
    private object PreferenceKeys {
        val SESSION_TYPE = stringPreferencesKey("sessionType")
        val SESSION_ID = stringPreferencesKey("session_id")
    }

    override suspend fun getSessionType(): String {
        return datastore.data.map { settings ->
            settings[SESSION_TYPE] ?: ""
        }.first()
    }

    override suspend fun setSessionType(sessionType: String) {
        datastore.edit { settings ->
            settings[SESSION_TYPE] = sessionType
        }
    }

    override suspend fun cacheSessionId(sessionId: String) {
        val encryptedSessionId = cryptoData.encryptString(sessionId)
        datastore.edit { setting ->
            setting[SESSION_ID] = encryptedSessionId
        }
    }

    override suspend fun getSessionId(): String? {
        val encryptedSessionId =
            datastore.data
                .map { setting ->
                    setting[SESSION_ID] ?: throw Exception()
                }.first()
        return cryptoData.decryptString(encryptedSessionId)
    }

    override suspend fun clearSessionId() {
        datastore.edit { setting ->
            setting[SESSION_ID] = ""
        }
    }
}
