package com.amsterdam.viewmodel.profile

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
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
            onError = ::onUserDataNotLoaded
        )
    }

    private fun loadSettings() {
        viewModelScope.launch {
            manageLocaleLanguageUseCase.getAppLanguage().collect { language ->
                onGetAppLanguage(language)
            }
        }
        viewModelScope.launch {
            manageAppThemeUseCase.getAppTheme().collect { isDarkTheme ->
                onGetAppTheme(isDarkTheme)
            }
        }
    }

    private fun onGetAppTheme(isDarkTheme: Boolean) {
        updateState { state ->
            state.copy(
                isDarkTheme = isDarkTheme,
                updatedIsDarkTheme = isDarkTheme
            )
        }
    }

    private fun onGetAppLanguage(language: ManageLocaleLanguageUseCase.Language) {
        updateState { state ->
            state.copy(
                language = language,
                updatedLanguage = language
            )
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
            onError = ::onUserDataNotLoaded
        )
    }

    private fun onGetUserProfileInfoSuccess(accountDetails: AccountDetails) {
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
        viewModelScope.launch {
            manageLocaleLanguageUseCase.setAppLanguage(state.value.language)
        }
        onApplyLanguageSuccess(Unit)
    }

    private fun onApplyLanguageFailure(aflamiException: AflamiException) {
        updateState { state -> state.copy(language = state.updatedLanguage) }
        //sendNewEffect(ProfileEffect.LanguageNotChanged)
    }

    private fun onApplyLanguageSuccess(unit: Unit) {
        updateState { state -> state.copy(updatedLanguage = state.language) }
        onDismissLanguageDialog()
        //sendNewEffect(ProfileEffect.LanguageChanged)
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
        viewModelScope.launch {
            manageAppThemeUseCase.setAppTheme(state.value.isDarkTheme)
        }
        onApplyThemeSuccess(Unit)
    }

    private fun onApplyThemeFailure(aflamiException: AflamiException) {
        updateState { state -> state.copy(isDarkTheme = state.updatedIsDarkTheme) }
        //sendNewEffect(ProfileEffect.ThemeNotChanged)
    }

    private fun onApplyThemeSuccess(unit: Unit) {
        updateState { state -> state.copy(updatedIsDarkTheme = state.isDarkTheme) }
        onDismissThemeDialog()
        //sendNewEffect(ProfileEffect.ThemeChanged)
    }

    override fun onDismissThemeDialog() {
        updateState { state -> state.copy(showThemeDialog = false) }
    }

    private fun onUserDataNotLoaded(aflamiException: AflamiException) {
        when (aflamiException) {
            is NetworkException -> sendNewEffect(ProfileEffect.UserDataNotLoaded)
            else -> {}
        }
    }
}