package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.serviceProvider.AuthenticationServiceProvider
import com.amsterdam.repository.datasource.remote.AuthenticationRemoteSource
import javax.inject.Inject

class AuthenticationRemoteDataSourceImpl @Inject constructor (
    private val authenticationServiceProvider: AuthenticationServiceProvider,
) : AuthenticationRemoteSource {
    override suspend fun loginWithPassword(
        username: String,
        password: String,
    ): String {
        val unactiveToken = authenticationServiceProvider.createRequestToken()
        val activeToken =
            authenticationServiceProvider.createSessionWithLogin(username, password, unactiveToken.requestToken ?: "")
        return authenticationServiceProvider.createSession(activeToken.requestToken ?: "").sessionId
    }
}
