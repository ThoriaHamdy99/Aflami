package com.example.remotedatasource.datasource

import android.util.Log
import com.example.remotedatasource.serviceProvider.AuthenticationServiceProvider
import com.example.repository.datasource.remote.AuthenticationRemoteSource

class AuthenticationRemoteDataSourceImpl(
    private val authenticationServiceProvider: AuthenticationServiceProvider,
) : AuthenticationRemoteSource {
    override suspend fun loginWithPassword(
        username: String,
        password: String,
    ): String {
        Log.d("TAG", "loginWithPassword: CreateRequestToken")
        val unactiveToken = authenticationServiceProvider.createRequestToken()
        Log.d("TAG", "loginWithPassword: createSessionWithLogin with $unactiveToken")
        val activeToken =
            authenticationServiceProvider.createSessionWithLogin(username, password, unactiveToken.requestToken ?: "")
        Log.d("TAG", "loginWithPassword: createSession $activeToken")
        return authenticationServiceProvider.createSession(activeToken.requestToken ?: "").sessionId
    }
}
