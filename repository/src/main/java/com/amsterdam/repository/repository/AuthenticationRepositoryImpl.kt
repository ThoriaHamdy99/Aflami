package com.amsterdam.repository.repository

import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.repository.datasource.local.AuthenticationLocalDataSource
import com.amsterdam.repository.datasource.local.ProfileLocalDataSource
import com.amsterdam.repository.datasource.remote.AuthenticationRemoteDataSource
import com.amsterdam.repository.mapper.local.stringToSessionTypeEntity
import com.amsterdam.repository.mapper.local.toLocalDto
import com.amsterdam.repository.security.CryptoData
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    private val profileLocalDataSource: ProfileLocalDataSource,
    val cryptoData: CryptoData,
) : AuthenticationRepository {
    override suspend fun loginWithPassword(username: String, password: String, ) {
        authenticationRemoteDataSource.loginWithPassword(username, password).let { sessionId ->
        authenticationLocalDataSource.cacheSessionId(cryptoData.encryptString(sessionId)) }
        authenticationLocalDataSource.setSessionType(SessionType.LOGGED_IN.toLocalDto())
    }

    override suspend fun getSessionId(): String =
        cryptoData.decryptString(authenticationLocalDataSource.getCachedSessionId())
            ?: throw UnknownException()

    override suspend fun setSessionType(sessionType: SessionType) {
        authenticationLocalDataSource.setSessionType(sessionType.toLocalDto())
    }

    override suspend fun getSessionType(): SessionType =
        stringToSessionTypeEntity(authenticationLocalDataSource.getSessionType())

    override suspend fun logout() {
        authenticationLocalDataSource.clearCachedSessionId()
        authenticationLocalDataSource.setSessionType(SessionType.NOT_LOGGED_IN.toLocalDto())
        profileLocalDataSource.deleteAccountDetails()
    }
}
