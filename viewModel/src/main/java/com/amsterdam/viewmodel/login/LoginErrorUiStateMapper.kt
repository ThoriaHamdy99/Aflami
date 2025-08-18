package com.amsterdam.viewmodel.login

import com.amsterdam.domain.exceptions.AccountDisabledException
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.InvalidCredentialsException
import com.amsterdam.domain.exceptions.VerificationRequiredException
import com.amsterdam.viewmodel.login.LoginErrorState.AccountDisabledError
import com.amsterdam.viewmodel.login.LoginErrorState.InvalidCredentialsError
import com.amsterdam.viewmodel.login.LoginErrorState.VerificationRequiredError

fun AflamiException.toLoginErrorUiState(): LoginErrorState? {
    return when (this) {
        is InvalidCredentialsException -> InvalidCredentialsError
        is VerificationRequiredException -> VerificationRequiredError
        is AccountDisabledException -> AccountDisabledError
        else -> null
    }
}