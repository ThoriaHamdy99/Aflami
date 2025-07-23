package com.example.repository.repository

import com.example.domain.repository.AuthenticationRepository
import com.example.domain.utils.UserLoginType
import com.example.repository.datasource.local.AuthenticationLocalSource
import com.example.repository.mapper.local.UserLoginTypeMapper

class AuthenticationRepositoryImpl(
    private val authenticationLocalSource: AuthenticationLocalSource,
    private val userLoginTypeMapper: UserLoginTypeMapper
): AuthenticationRepository {
    override suspend fun loginWithPassword(username: String, password: String) {

    }

    override suspend fun setLoginType(loginType: UserLoginType) {
        authenticationLocalSource.setLoginType(userLoginTypeMapper.toLocalLoginType(loginType))
    }

    override suspend fun getLoginType(): UserLoginType {
        return userLoginTypeMapper.fromLocalLoginType(authenticationLocalSource.getLoginType())
    }
}