package com.amsterdam.viewmodel.shared.errorUiState

sealed interface ErrorUiState {
    data object NoInternetError : ErrorUiState
    data object UnknownError : ErrorUiState
}