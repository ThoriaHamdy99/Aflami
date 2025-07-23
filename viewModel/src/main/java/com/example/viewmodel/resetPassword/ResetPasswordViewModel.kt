package com.example.viewmodel.resetPassword

import com.example.viewmodel.BuildConfig
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.utils.dispatcher.DispatcherProvider


class ResetPasswordViewModel : BaseViewModel<ResetPasswordUiState, ResetPasswordEffect> {
    constructor(dispatcherProvider: DispatcherProvider) : super(
        dispatcherProvider = dispatcherProvider,
        initialState = ResetPasswordUiState()
    )

    init {

        updateState { it.copy(resetPasswordUrl = BuildConfig.MOVIE_RESET_PASSWORD_URL) }
    }

    fun setLoading(isLoading: Boolean) {
        updateState { it.copy(isLoading = isLoading) }
    }

    fun onResetPasswordComplete() {
        sendNewEffect(ResetPasswordEffect.NavigateToSignIn)
    }
}