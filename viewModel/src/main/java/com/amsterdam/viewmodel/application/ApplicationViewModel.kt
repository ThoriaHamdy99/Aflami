package com.amsterdam.viewmodel.application

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.preferences.GetOnboardingStatusUseCase
import com.amsterdam.domain.useCase.preferences.ManageAppThemeUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    private val getOnboardingStatusUseCase: GetOnboardingStatusUseCase,
    private val manageAppThemeUseCase: ManageAppThemeUseCase,
    private val getsSessionType: GetsSessionType
) : BaseViewModel<ApplicationUiState, Unit>(ApplicationUiState(), dispatcherProvider) {

    init {
        listenToAppSettings()
    }

    suspend fun setStartDestination(): ApplicationUiState.StartDestinations {
        val isOnboardingCompleted = getOnboardingStatusUseCase()

        if (!isOnboardingCompleted) {
            return ApplicationUiState.StartDestinations.ON_BOARDING
        }

        val sessionType = getsSessionType()
        return when (sessionType) {
            SessionType.NOT_LOGGED_IN -> ApplicationUiState.StartDestinations.LOGIN
            SessionType.LOGGED_IN -> ApplicationUiState.StartDestinations.HOME
            SessionType.GUEST -> ApplicationUiState.StartDestinations.HOME
        }
    }

    fun initAppSettings(locale: Locale) {
        viewModelScope.launch(dispatcherProvider.IO) {
            manageLocaleLanguageUseCase.initAppLanguage(locale.language)
            manageAppThemeUseCase.initAppTheme(isDarkTheme = true)
        }
    }

    private fun listenToAppSettings() {
        viewModelScope.launch(dispatcherProvider.IO) {
            manageAppThemeUseCase.getAppTheme().collectLatest { isDarkTheme ->
                updateState { state ->
                    state.copy(
                        isDarkTheme = isDarkTheme
                    )
                }
            }
            manageLocaleLanguageUseCase.getAppLanguage().collectLatest { language ->
                updateState { state ->
                    state.copy(
                        language = language,
                    )
                }
            }
        }
    }

    fun setLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}