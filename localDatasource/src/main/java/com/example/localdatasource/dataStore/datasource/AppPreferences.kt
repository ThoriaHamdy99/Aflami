package com.example.localdatasource.dataStore.datasource

interface AppPreferences {
    suspend fun getSessionType(): String
    suspend fun setSessionType(sessionType: String)

    suspend fun cacheSessionId(sessionId: String)

    suspend fun getSessionId(): String?

    suspend fun clearSessionId()
}
