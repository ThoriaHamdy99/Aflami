package com.amsterdam.domain.useCase.authentication

import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.utils.SessionType
import javax.inject.Inject

class LoginAsGuestUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(){
        authenticationRepository.setSessionType(SessionType.GUEST)
    }
}