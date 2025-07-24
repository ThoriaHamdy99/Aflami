package com.example.viewmodel.register

import com.example.viewmodel.BuildConfig
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.utils.dispatcher.DispatcherProvider


class RegisterViewModel(dispatcherProvider: DispatcherProvider) :
    BaseViewModel<RegisterUiState, RegisterEffect>(RegisterUiState(), dispatcherProvider) {
    init {
        updateState { it.copy(signUpUrl = BuildConfig.MOVIE_SIGN_UP_URL) }

    }

    fun setLoading(isLoading: Boolean) {
        updateState { it.copy(isLoading = isLoading) }
    }

    fun onRegistrationComplete() {
        sendNewEffect(RegisterEffect.NavigateToSignIn)
    }
}