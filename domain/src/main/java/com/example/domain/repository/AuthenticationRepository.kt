package com.example.domain.repository

import com.example.domain.utils.UserLoginType

interface AuthenticationRepository {
    suspend fun loginWithPassword(
        username: String,
        password: String,
    )

    suspend fun setLoginType(loginType: UserLoginType)
    suspend fun getLoginType(): UserLoginType
}
