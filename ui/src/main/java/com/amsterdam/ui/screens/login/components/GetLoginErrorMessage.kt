package com.amsterdam.ui.screens.login.components

import android.content.Context
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.login.LoginErrorState
import com.amsterdam.viewmodel.login.LoginErrorState.AccountDisabledError
import com.amsterdam.viewmodel.login.LoginErrorState.InvalidCredentialsError
import com.amsterdam.viewmodel.login.LoginErrorState.VerificationRequiredError
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState.NoInternetError

fun getLoginErrorMessage(loginErrorState: LoginErrorState?, context: Context): String{
    return when(loginErrorState){
        InvalidCredentialsError -> context.getString(R.string.incorrect_username_or_password)
        AccountDisabledError -> context.getString(R.string.account_disabled)
        VerificationRequiredError -> context.getString(R.string.account_not_verified)
        LoginErrorState.UnknownError -> context.getString(R.string.search_error_unknown)
        null -> ""
    }
}

fun getLoginErrorMessage(errorState: ErrorUiState?, context: Context): String{
    return when(errorState){
        NoInternetError -> context.getString(R.string.offline_message)
        else -> ""
    }
}
