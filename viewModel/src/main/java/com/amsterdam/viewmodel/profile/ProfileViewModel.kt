package com.amsterdam.viewmodel.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.preferences.ManageAppThemeUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.profile.GetAccountDetailsUseCase
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.entity.AccountDetails
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getSessionTypeUseCase: GetsSessionType,
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    private val manageAppThemeUseCase: ManageAppThemeUseCase,
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
        viewModelScope.launch {
            manageLocaleLanguageUseCase.getAppLanguage().collectLatest { language ->
                updateState { state ->
                    state.copy(
                        language = language
                    )
                }
                sendNewEffect(ProfileEffect.LanguageChanged(language.value))
            }
            manageAppThemeUseCase.getAppTheme().collectLatest { isDarkTheme ->
                updateState { state ->
                    state.copy(
                        isDarkTheme = isDarkTheme
                    )
                }
                sendNewEffect(ProfileEffect.ThemeChanged(isDarkTheme))
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

    override fun onClickLogin() {
        sendNewNavigationEffect(ProfileEffect.NavigateToLogin)
    }

    override fun onClickLanguageSetting() {
        updateState { state -> state.copy(showLanguageDialog = true) }
    }

    override fun onChangeLanguage(language: ManageLocaleLanguageUseCase.Language) {
        updateState { state -> state.copy(language = language) }
    }

    override fun onApplyLanguage() {
        viewModelScope.launch(dispatcherProvider.IO) {
            manageLocaleLanguageUseCase.setAppLanguage(
                state.value.language
            )
        }
        onDismissLanguageDialog()
    }

    override fun onDismissLanguageDialog() {
        updateState { state -> state.copy(showLanguageDialog = false) }
    }

    override fun onClickThemeSetting() {
        updateState { state -> state.copy(showThemeDialog = true) }
    }

    override fun onChangeTheme(isDarkTheme: Boolean) {
        updateState { state -> state.copy(isDarkTheme = isDarkTheme) }
    }

    override fun onApplyTheme() {
        viewModelScope.launch(dispatcherProvider.IO) {
            manageAppThemeUseCase.setAppTheme(state.value.isDarkTheme)
        }
        onDismissThemeDialog()
    }

    override fun onDismissThemeDialog() {
        updateState { state -> state.copy(showThemeDialog = false) }
    }

    private fun onError(aflamiException: AflamiException) {

    }
}