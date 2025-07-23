package com.example.viewmodel.register

import com.example.viewmodel.BuildConfig
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.utils.dispatcher.DispatcherProvider


class RegisterViewModel : BaseViewModel<RegisterUiState, RegisterEffect> {
    constructor(dispatcherProvider: DispatcherProvider): super(dispatcherProvider = dispatcherProvider, initialState = RegisterUiState())

    fun setLoading(isLoading: Boolean) {
        updateState { it.copy(isLoading = isLoading) }
    }

    fun onRegistrationComplete() {
        sendNewEffect(RegisterEffect.NavigateToSignIn)
    }
}