package com.amsterdam.ui.screens.login.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.login.PasswordErrorState
import com.amsterdam.viewmodel.login.UsernameErrorState

@Composable
fun getUserNameErrorMessage(usernameErrorState: UsernameErrorState?): String{
    return when(usernameErrorState){
        UsernameErrorState.InvalidCredentials -> stringResource(R.string.incorrect_username_or_password)
        null -> ""
    }
}

@Composable
fun getPasswordErrorMessage(passwordErrorState: PasswordErrorState?): String{
    return when(passwordErrorState){
        PasswordErrorState.InvalidCredentials -> stringResource(R.string.incorrect_username_or_password)
        null -> ""
    }
}