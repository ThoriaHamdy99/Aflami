package com.example.localdatasource.dataStore.appPreferences

interface AppPreferences {
    suspend fun getLoginType(): String
    suspend fun setLoginType(loginType: String)
}