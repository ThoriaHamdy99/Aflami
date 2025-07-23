package com.example.repository.datasource.local

import com.example.domain.utils.UserLoginType

interface AuthenticationLocalSource {
    suspend fun setLoginType(loginType: String)
    suspend fun getLoginType(): String
}