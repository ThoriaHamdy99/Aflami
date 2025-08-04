package com.amsterdam.viewmodel.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.profile.GetAccountDetailsUseCase
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.entity.AccountDetails
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getSessionTypeUseCase: GetsSessionType,
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel<ProfileUiState, ProfileEffect>(
    initialState = ProfileUiState(),
    dispatcherProvider = dispatcherProvider
), ProfileInteractionListener {
    init {
        loadProfileData()
        loadSettings()
    }

    private fun loadProfileData() {
        updateState { state -> state.copy(isLoading = true) }
        tryToExecute(
            action = { getSessionTypeUseCase() },
            onSuccess = ::onGetSessionTypeSuccess,
            onError = ::onError
        )
    }

    private fun loadSettings() {
        manageLocaleLanguageUseCase.getDeviceLanguage().onEach { language ->
            updateState { state ->
                state.copy(
                    language = language
                )
            }
        }
    }

    private fun onGetSessionTypeSuccess(sessionType: SessionType) {
        if (sessionType != SessionType.LOGGED_IN) {
            updateState { state -> state.copy(isUserLoggedIn = false, isLoading = false) }
            return
        }
        getUserProfileInfo()
    }

    private fun getUserProfileInfo() {
        updateState { state -> state.copy(isUserLoggedIn = true, isLoading = false) }
        tryToExecute(
            action = { getAccountDetailsUseCase() },
            onSuccess = ::onGetUserProfileInfoSuccess,
            onError = ::onError
        )
    }

    private fun onGetUserProfileInfoSuccess(accountDetails: AccountDetails) {
        Log.d("ProfileViewModel", "onGetUserProfileInfoSuccess: $accountDetails")
        updateState { state ->
            state.copy(
                userInfo = state.userInfo.copy(
                    username = accountDetails.username,
                    userAvatarUrl = accountDetails.avatarUrl
                )
            )
        }
    }

    private fun onError(aflamiException: AflamiException) {

    }

    override fun onClickLogin() {
        sendNewNavigationEffect(ProfileEffect.NavigateToLogin)
    }

    override fun onClickLanguage() {
        updateState { state -> state.copy(showLanguageDialog = true) }
    }

    override fun onChangeLanguage(language: ManageLocaleLanguageUseCase.Language) {
        viewModelScope.launch(dispatcherProvider.IO) {
            manageLocaleLanguageUseCase.setDeviceLanguage(
                language.value
            )
        }
        updateState { state -> state.copy(language = language) }
        onDismissLanguageDialog()
    }

    override fun onDismissLanguageDialog() {
        updateState { state -> state.copy(showLanguageDialog = false) }
    }

    override fun onClickTheme() {
        updateState { state -> state.copy(showThemeDialog = true) }
    }

    override fun onChangeTheme(isDarkTheme: Boolean) {
        //Change theme here
        updateState { state -> state.copy(isDarkTheme = isDarkTheme) }
        onDismissLanguageDialog()
    }

    override fun onDismissThemeDialog() {
        updateState { state -> state.copy(showThemeDialog = false) }
    }
}