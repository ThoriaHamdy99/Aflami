package com.example.localdatasource.dataStore.appPreferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.localdatasource.dataStore.appPreferences.AppPreferencesImpl.PreferenceKeys.LOGIN_TYPE
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppPreferencesImpl(
    private val datastore: DataStore<Preferences>
): AppPreferences {
    private object PreferenceKeys {
        val LOGIN_TYPE = stringPreferencesKey("loginType")
    }

    override suspend fun getLoginType(): String {
        return datastore.data.map { settings ->
            settings[LOGIN_TYPE] ?: ""
        }.first()
    }

    override suspend fun setLoginType(loginType: String) {
        datastore.edit { settings ->
            settings[LOGIN_TYPE] = loginType
        }
    }
}