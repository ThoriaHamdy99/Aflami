package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.repository.datasource.local.AppPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppPreferencesRepositoryImpl @Inject constructor(
    private val preferences: AppPreferences
) : AppPreferencesRepository {
    override fun getAppLanguage(): Flow<String> = preferences.getAppLanguage()

    override suspend fun setAppLanguage(language: String) =
        preferences.setAppLanguage(language)

    override suspend fun initAppLanguage(language: String) {
        preferences.initAppLanguage(language)
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
    override suspend fun setOnboardingCompleted(isCompleted: Boolean) {
        preferences.setOnboardingCompleted(isCompleted)
    }

    override suspend fun isOnboardingCompleted(): Boolean {
        return preferences.isOnboardingCompleted()
    }
}