package com.amsterdam.localdatasource.dataStore.datasource

import com.amsterdam.domain.exceptions.UnauthorizedException
import com.amsterdam.repository.datasource.local.AuthenticationLocalSource
import javax.inject.Inject

class AuthenticationLocalDataSourceImpl @Inject constructor(
    private val appPreferences: AppPreferences
): AuthenticationLocalSource {
    override suspend fun setSessionType(sessionType: String) {
        appPreferences.setSessionType(sessionType)
    }

    override suspend fun getSessionType(): String {
        return appPreferences.getSessionType()
    }

    override suspend fun cacheSessionId(sessionId: String) {
        appPreferences.cacheSessionId(sessionId = sessionId)
    }

    override suspend fun getCachedSessionId(): String = appPreferences.getSessionId() ?: throw UnauthorizedException()

    override suspend fun clearCachedSessionId() {
        appPreferences.clearSessionId()
    }
}
