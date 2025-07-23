package com.example.domain.useCase.authentication

import com.example.domain.repository.AuthenticationRepository
import com.example.domain.utils.UserLoginType

class LoginAsGuestUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(){
        authenticationRepository.setLoginType(UserLoginType.GUEST)
    }
}