package com.amsterdam.domain.useCase.authentication

import com.amsterdam.domain.repository.AuthenticationRepository
import javax.inject.Inject

class LoginWithPasswordUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(
        username: String,
        password: String,
    ) {
        authenticationRepository.loginWithPassword(username, password)
    }
}
