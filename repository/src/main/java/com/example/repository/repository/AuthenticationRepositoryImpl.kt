package com.example.repository.repository

import com.example.domain.repository.AuthenticationRepository
import com.example.domain.utils.SessionType
import com.example.repository.datasource.local.AuthenticationLocalSource
import com.example.repository.mapper.local.UserLoginTypeMapper

class AuthenticationRepositoryImpl(
    private val authenticationLocalSource: AuthenticationLocalSource,
    private val userLoginTypeMapper: UserLoginTypeMapper
): AuthenticationRepository {
    override suspend fun loginWithPassword(username: String, password: String) {

    }

    override suspend fun setSessionType(sessionType: SessionType) {
        authenticationLocalSource.setSessionType(userLoginTypeMapper.toLocalLoginType(sessionType))
    }

    override suspend fun getSessionType(): SessionType {
        return userLoginTypeMapper.fromLocalLoginType(authenticationLocalSource.getSessionType())
    }
}