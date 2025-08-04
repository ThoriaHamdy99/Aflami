package com.amsterdam.domain.repository

import kotlinx.coroutines.flow.Flow

interface AppPreferencesRepository {
    fun getDeviceLanguage(): Flow<String>
    suspend fun setDeviceLanguage(language: String)
    suspend fun initDeviceLanguage(language: String)

    suspend fun initAppTheme(isDarkTheme: Boolean)
    fun getAppTheme(): Flow<Boolean>
    suspend fun setAppTheme(isDarkTheme: Boolean)
}