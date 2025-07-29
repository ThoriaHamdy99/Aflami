package com.amsterdam.ui.screens.login.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.login.LoginErrorState

fun getLoginErrorMessage(loginErrorState: LoginErrorState?, context: Context): String{
    return when(loginErrorState){
        LoginErrorState.InvalidCredentials -> context.getString(R.string.incorrect_username_or_password)
        LoginErrorState.AccountDisabled -> context.getString(R.string.account_disabled)
        LoginErrorState.VerificationRequired -> context.getString(R.string.account_not_verified)
        null -> ""
    }
}