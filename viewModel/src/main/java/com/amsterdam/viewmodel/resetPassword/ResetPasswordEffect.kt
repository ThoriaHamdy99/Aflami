package com.amsterdam.viewmodel.resetPassword

sealed interface ResetPasswordEffect {
    data object NavigateToSignIn : ResetPasswordEffect
}