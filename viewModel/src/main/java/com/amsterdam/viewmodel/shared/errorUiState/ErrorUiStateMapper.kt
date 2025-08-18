package com.amsterdam.viewmodel.shared.errorUiState

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState.NoInternetError

fun AflamiException.toErrorUiState(): ErrorUiState {
    return when (this) {
        is NetworkException -> NoInternetError
        else -> ErrorUiState.UnknownError
    }
}

fun ErrorUiState?.isNull(): Boolean = this == null