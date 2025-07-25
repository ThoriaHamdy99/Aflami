package com.amsterdam.remotedatasource.serviceProvider

import com.amsterdam.repository.dto.remote.authentication.AuthenticationResponseDto
import com.amsterdam.repository.dto.remote.authentication.CreateSessionResponseDto

interface AuthenticationServiceProvider {
    suspend fun createRequestToken(): AuthenticationResponseDto

    suspend fun createSessionWithLogin(
        username: String,
        password: String,
        requestToken: String,
    ): AuthenticationResponseDto

    suspend fun createSession(requestToken: String): CreateSessionResponseDto
}
