package com.amsterdam.viewmodel.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoginButtonLoading: Boolean = false,
    val isLoginButtonEnabled: Boolean = false,
    val isPasswordShown: Boolean = false
)