package com.amsterdam.localdatasource.dataStore

import android.os.LocaleList
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.amsterdam.localdatasource.dataStore.AppPreferencesImpl.PreferenceKeys.CURRENT_LANGUAGE
import com.amsterdam.localdatasource.dataStore.AppPreferencesImpl.PreferenceKeys.IS_DARK_THEME
import com.amsterdam.repository.datasource.local.AppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferencesImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AppPreferences {

    override suspend fun initAppLanguage(language: String) {
        val currentLanguage = dataStore.data.map { preferences ->
            preferences[CURRENT_LANGUAGE]
        }.firstOrNull()
        if (currentLanguage == null) {
            setAppLanguage(language)
        }
    }

    override suspend fun setAppLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[CURRENT_LANGUAGE] = language
        }
    }

    override suspend fun initAppTheme(isDarkTheme: Boolean) {
        val isDarkThemeFromLocal = dataStore.data.map { preferences ->
            preferences[IS_DARK_THEME]
        }.firstOrNull()
        if (isDarkThemeFromLocal == null) {
            setAppTheme(isDarkTheme)
        }
    }

    override fun getAppTheme(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_DARK_THEME] ?: true
        }
    }

    override suspend fun setAppTheme(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = isDarkTheme
        }
    }

    override fun getAppLanguage(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[CURRENT_LANGUAGE] ?: LocaleList.getDefault()[0].language.lowercase()
        }
    }

    private object PreferenceKeys {
        val CURRENT_LANGUAGE = stringPreferencesKey("current_language")
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
    }
}