package com.example.viewmodel.application

import androidx.lifecycle.viewModelScope
import com.example.domain.useCase.authentication.GetUserLoginType
import com.example.domain.utils.UserLoginType
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.launch

class ApplicationViewModel(
    dispatcherProvider: DispatcherProvider,
    private val getUserLoginType: GetUserLoginType
): BaseViewModel<ApplicationUiState, Unit>(ApplicationUiState(), dispatcherProvider) {
    init {
        viewModelScope.launch {
            setStartDestination()
        }
    }

    private suspend fun setStartDestination(){
        val loginType = getUserLoginType()
        when(loginType){
            UserLoginType.NONE -> updateState { it.copy(startDestination = ApplicationUiState.StartDestinations.LOGIN) }
            UserLoginType.USER -> updateState { it.copy(startDestination = ApplicationUiState.StartDestinations.HOME) }
            UserLoginType.GUEST -> updateState { it.copy(startDestination = ApplicationUiState.StartDestinations.HOME) }
        }
    }
}