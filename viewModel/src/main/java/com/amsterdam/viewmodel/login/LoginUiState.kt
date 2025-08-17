package com.amsterdam.viewmodel.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoginButtonLoading: Boolean = false,
    val isLoginButtonEnabled: Boolean = false,
    val isPasswordShown: Boolean = false,
    val error: LoginErrorState? = null
)

sealed interface LoginErrorState {
    data object InvalidCredentialsError: LoginErrorState
    data object VerificationRequiredError: LoginErrorState
    data object AccountDisabledError: LoginErrorState
    data object UnknownError: LoginErrorState
}