package com.amsterdam.viewmodel.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.profile.GetAccountDetailsUseCase
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.entity.AccountDetails
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getSessionTypeUseCase: GetsSessionType,
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel<ProfileUiState, ProfileEffect>(
    initialState = ProfileUiState(),
    dispatcherProvider = dispatcherProvider
), ProfileInteractionListener {
    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        updateState { state -> state.copy(isLoading = true) }
        tryToExecute(
            action = { getSessionTypeUseCase() },
            onSuccess = ::onGetSessionTypeSuccess,
            onError = ::onError
        )
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
        viewModelScope.launch {
            sendNewNavigationEffect(ProfileEffect.NavigateToLogin)
        }
    }
}