package com.amsterdam.repository.datasource.local

import kotlinx.coroutines.flow.Flow

interface AppPreferences {
    suspend fun initAppLanguage(language: String)
    fun getAppLanguage(): Flow<String>
    suspend fun setAppLanguage(language: String)

    suspend fun initAppTheme(isDarkTheme: Boolean)
    fun getAppTheme(): Flow<Boolean>
    suspend fun setAppTheme(isDarkTheme: Boolean)
}