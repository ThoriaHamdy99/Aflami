package com.example.viewmodel.login

interface LoginInteractionListener {
    fun onUserNameUpdated(username: String)
    fun onPasswordUpdate(password: String)
    fun onSetUserNameError(usernameError: String)
    fun onSetPasswordError(passwordError: String)
    fun onShowPasswordClicked()
    fun onLoginClicked()
    fun onContinueAsGuestClicked()
    fun onForgotPasswordClicked()
    fun onCreateAccountClicked()
}