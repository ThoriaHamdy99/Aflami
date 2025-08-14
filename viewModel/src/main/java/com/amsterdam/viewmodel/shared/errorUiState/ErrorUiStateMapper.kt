package com.amsterdam.viewmodel.shared.errorUiState

import com.amsterdam.domain.exceptions.AccountDisabledException
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.InvalidCredentialsException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.exceptions.VerificationRequiredException
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState.NoInternetError

fun AflamiException.toErrorUiState(): ErrorUiState {
    return when (this) {
        is InvalidCredentialsException -> ErrorUiState.LoginErrorState.InvalidCredentialsError
        is VerificationRequiredException -> ErrorUiState.LoginErrorState.VerificationRequiredError
        is AccountDisabledException -> ErrorUiState.LoginErrorState.AccountDisabledError
        is NetworkException -> NoInternetError
        else -> ErrorUiState.UnknownError
    }
}

fun ErrorUiState?.isNull(): Boolean = this == null