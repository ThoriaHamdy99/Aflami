package com.amsterdam.ui.screens.login.components

import android.content.Context
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState.LoginErrorState.AccountDisabledError
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState.LoginErrorState.InvalidCredentialsError
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState.LoginErrorState.VerificationRequiredError
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState.NoInternetError

fun getLoginErrorMessage(loginErrorState: ErrorUiState?, context: Context): String{
    return when(loginErrorState){
        InvalidCredentialsError -> context.getString(R.string.incorrect_username_or_password)
        AccountDisabledError -> context.getString(R.string.account_disabled)
        VerificationRequiredError -> context.getString(R.string.account_not_verified)
        NoInternetError -> context.getString(R.string.offline_message)
        ErrorUiState.UnknownError -> context.getString(R.string.search_error_unknown)
        else -> ""
    }
}