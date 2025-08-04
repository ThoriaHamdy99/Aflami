package com.amsterdam.domain.repository

import kotlinx.coroutines.flow.Flow

interface AppPreferencesRepository {
    suspend fun setOnboardingCompleted(isCompleted: Boolean)
    suspend fun isOnboardingCompleted(): Boolean
    fun getAppLanguage(): Flow<String>
    suspend fun setAppLanguage(language: String)
    suspend fun initAppLanguage(language: String)

    suspend fun initAppTheme(isDarkTheme: Boolean)
    fun getAppTheme(): Flow<Boolean>
    suspend fun setAppTheme(isDarkTheme: Boolean)
}