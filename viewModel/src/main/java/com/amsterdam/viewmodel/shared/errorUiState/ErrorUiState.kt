package com.amsterdam.viewmodel.shared.errorUiState

sealed interface ErrorUiState {
    data object NoInternetError : ErrorUiState
    data object UnknownError : ErrorUiState

    sealed interface LoginErrorState: ErrorUiState {
        data object InvalidCredentialsError: LoginErrorState
        data object VerificationRequiredError: LoginErrorState
        data object AccountDisabledError: LoginErrorState
    }
}