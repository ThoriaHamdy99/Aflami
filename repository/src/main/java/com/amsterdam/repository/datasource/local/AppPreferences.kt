package com.amsterdam.repository.datasource.local

import kotlinx.coroutines.flow.Flow

interface AppPreferences {
    suspend fun initDeviceLanguage(language: String)
    fun getDeviceLanguage(): Flow<String>
    suspend fun setDeviceLanguage(language: String)

    suspend fun initAppTheme(isDarkTheme: Boolean)
    fun getAppTheme(): Flow<Boolean>
    suspend fun setAppTheme(isDarkTheme: Boolean)
}