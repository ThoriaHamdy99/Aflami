package com.amsterdam.viewmodel.shared

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.viewmodel.shared.BaseErrorUiState.NoInternetError


sealed interface BaseErrorUiState {
    data object NoInternetError : BaseErrorUiState
    data object UnknownError : BaseErrorUiState
}

fun AflamiException.toErrorUiState(): BaseErrorUiState {
    return when (this) {
        is NetworkException -> NoInternetError
        else -> BaseErrorUiState.UnknownError
    }
}