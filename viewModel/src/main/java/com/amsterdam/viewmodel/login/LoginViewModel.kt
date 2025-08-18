package com.amsterdam.viewmodel.login

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.authentication.LoginAsGuestUseCase
import com.amsterdam.domain.useCase.authentication.LoginWithPasswordUseCase
import com.amsterdam.domain.useCase.profile.GetAccountDetailsUseCase
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val loginWithPasswordUseCase: LoginWithPasswordUseCase,
    private val loginAsGuestUseCase: LoginAsGuestUseCase,
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase
) : BaseViewModel<LoginUiState, LoginEffect>(LoginUiState(), dispatcherProvider),
    LoginInteractionListener {

    override fun onUserNameUpdated(username: String) {
        resetErrorsToNull()
        updateState {
            it.copy(
                username = username,
                isLoginButtonEnabled = username.isNotBlank() && state.value.password.isNotBlank()
            )
        }
    }

    override fun onPasswordUpdate(password: String) {
        resetErrorsToNull()
        updateState {
            it.copy(
                password = password,
                isLoginButtonEnabled = password.isNotBlank() && state.value.username.isNotBlank()
            )
        }
    }


    override fun onShowPasswordClicked() {
        updateState { it.copy(isPasswordShown = !state.value.isPasswordShown) }
    }

    override fun onLoginClicked() {
        tryToExecute(
            action = { onLoginStart() },
            onSuccess = { onLoginSuccess() },
            onError = ::onError,
            onCompletion = ::onComplete
        )
    }

    private suspend fun onLoginStart() {
        updateState { it.copy(isLoginButtonLoading = true) }
        loginWithPasswordUseCase.invoke(state.value.username, state.value.password)
    }

    private fun onLoginSuccess() {
        tryToExecute(
            action = getAccountDetailsUseCase::invoke,
            onCompletion = ::navigateToHome,
        )
    }

    override fun onContinueAsGuestClicked() {
        tryToExecute(
            action = { onLoginAsGuestStart() },
            onSuccess = { navigateToHome() },
            onError = ::onError,
            onCompletion = ::onComplete
        )
    }

    private fun onError(exception: AflamiException){
        updateLoginErrorUiState(exception.toLoginErrorUiState())
    }

    private fun onComplete() = updateState { it.copy(isLoginButtonLoading = false) }

    override fun onForgotPasswordClicked() {
        sendNewNavigationEffect(LoginEffect.NavigateToResetPassword)
    }

    override fun onCreateAccountClicked() {
        sendNewNavigationEffect(LoginEffect.NavigateToRegister)
    }

    private suspend fun onLoginAsGuestStart() {
        updateState { it.copy(isLoginButtonLoading = true) }
        loginAsGuestUseCase()
    }


    private fun navigateToHome() = sendNewNavigationEffect(LoginEffect.NavigateToHome)

    fun onLoginErrorHandled() = resetErrorsToNull()

    private fun updateLoginErrorUiState(loginError: LoginErrorState?){
        updateState { it.copy(error = loginError) }
    }

    private fun resetErrorsToNull(){
        resetErrorStateToNull()
        updateLoginErrorUiState(null)
    }
}