package com.amsterdam.viewmodel.application

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.preferences.GetRestrictionLevelUseCase
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
    dispatcherProvider: DispatcherProvider,
    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    private val getsSessionType: GetsSessionType,
    private val getRestrictionLevelUseCase: GetRestrictionLevelUseCase
) : BaseViewModel<ApplicationUiState, Unit>(ApplicationUiState(), dispatcherProvider) {

    init {
        viewModelScope.launch {
            getRestrictionLevel()
        }
    }

    suspend fun setStartDestination(): ApplicationUiState.StartDestinations {
        val sessionType = getsSessionType()
        return when (sessionType) {
            SessionType.NOT_LOGGED_IN -> ApplicationUiState.StartDestinations.LOGIN
            SessionType.LOGGED_IN -> ApplicationUiState.StartDestinations.HOME
            SessionType.GUEST -> ApplicationUiState.StartDestinations.HOME
        }
    }

    suspend fun onConfigurationChanged(locale: Locale) {
        manageLocaleLanguageUseCase.setDeviceLanguage(locale.language.lowercase())
    }

    private suspend fun getRestrictionLevel(){
        val restrictionLevel = getRestrictionLevelUseCase()
        restrictionLevel.collectLatest { restriction ->
            updateState {
                it.copy(restrictionLevel = restriction)
            }
        }
    }
}