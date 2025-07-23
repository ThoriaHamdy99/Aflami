package com.example.viewmodel.login

import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.AuthenticationException
import com.example.domain.exceptions.InvalidCredentialsException
import com.example.domain.useCase.authentication.LoginWithPasswordUseCase
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import kotlin.random.Random

class LoginViewModel(
    dispatcherProvider: DispatcherProvider,
    private val loginWithPasswordUseCase: LoginWithPasswordUseCase
) : BaseViewModel<LoginUiState, LoginEffect>(LoginUiState(), dispatcherProvider),
    LoginInteractionListener {

    override fun onUserNameUpdated(username: String) {
        updateState {
            it.copy(username = username, usernameError = "")
        }
        shouldEnableLoginButton()
    }

    override fun onPasswordUpdate(password: String) {
        updateState {
            it.copy(password = password, passwordError = "")
        }
        shouldEnableLoginButton()
    }

    override fun onSetUserNameError(usernameError: String) {
        updateState {
            it.copy(usernameError = usernameError)
        }
    }

    override fun onSetPasswordError(passwordError: String) {
        updateState {
            it.copy(passwordError = passwordError)
        }
    }

    override fun onShowPasswordClicked() {
        updateState {
            it.copy(isPasswordShown = !state.value.isPasswordShown)
        }
    }

    override fun onLoginClicked() {
        tryToExecute(
            action = ::onLoginStart,
            onSuccess = { onLoginSuccess() },
            onError = ::onLoginWithPasswordError,
            onCompletion = ::onLoginComplete

        )
    }

    override fun onContinueAsGuestClicked() {
        viewModelScope.launch {
            delay(1000)
            sendNewEffect(LoginEffect.NavigateToHome)
        }
    }

    override fun onForgotPasswordClicked() {

    }

    override fun onCreateAccountClicked() {

    }

    private fun shouldEnableLoginButton() {
        if (state.value.username.isNotBlank() && state.value.password.isNotBlank()) {
            updateState {
                it.copy(
                    isLoginButtonEnabled = true
                )
            }
        } else {
            updateState {
                it.copy(isLoginButtonEnabled = false)
            }
        }
    }

    private suspend fun onLoginStart() {
        updateState { it.copy(isLoginButtonLoading = true) }
        loginWithPasswordUseCase.invoke(state.value.username, state.value.password)
    }

    private fun onLoginSuccess() {
        sendNewEffect(LoginEffect.NavigateToHome)
    }

    private fun onLoginWithPasswordError(exception: AflamiException) {
        when(exception){
            is InvalidCredentialsException -> {
                sendNewEffect(LoginEffect.InvalidCredentialsError)
            }
        }
    }

    private fun onLoginComplete() {
        updateState { it.copy(isLoginButtonLoading = false) }
    }
}