package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.repository.datasource.local.AppPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppPreferencesRepositoryImpl @Inject constructor(
    private val preferences: AppPreferences
) : AppPreferencesRepository {
    override fun getDeviceLanguage(): Flow<String> = preferences.getDeviceLanguage()

    override suspend fun setDeviceLanguage(language: String) =
        preferences.setDeviceLanguage(language)

    override suspend fun initDeviceLanguage(language: String) {
        preferences.initDeviceLanguage(language)
    }

    override suspend fun initAppTheme(isDarkTheme: Boolean) {
        preferences.initAppTheme(isDarkTheme)
    }

    override fun getAppTheme(): Flow<Boolean> {
        return preferences.getAppTheme()
    }

    override suspend fun setAppTheme(isDarkTheme: Boolean) {
        preferences.setAppTheme(isDarkTheme)
    }
}