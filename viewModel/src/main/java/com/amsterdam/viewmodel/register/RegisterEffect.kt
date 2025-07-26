package com.amsterdam.viewmodel.register

sealed interface RegisterEffect {
    data object NavigateToSignIn : RegisterEffect
}