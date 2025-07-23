package com.example.domain.useCase.authentication

import com.example.domain.repository.AuthenticationRepository
import com.example.domain.utils.UserLoginType

class GetUserLoginType(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(): UserLoginType{
        return authenticationRepository.getLoginType()
    }
}