package com.amsterdam.viewmodel.onboarding

import com.amsterdam.domain.useCase.preferences.SetOnboardingCompletedUseCase
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val setOnboardingCompletedUseCase: SetOnboardingCompletedUseCase,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel<OnboardingUiState, OnboardingEffect>(
    OnboardingUiState(totalPages = 4),
    dispatcherProvider
), OnboardingInteractionListener {

    override fun onNextPageClicked() {
        if (state.value.currentPageIndex < state.value.totalPages - 1) {
            val newIndex = state.value.currentPageIndex + 1
            updateState {
                it.copy(
                    currentPageIndex = newIndex,
                    isLastPage = newIndex == state.value.totalPages - 1
                )
            }
        }
    }

    override fun onPreviousPageClicked() {
        if (state.value.currentPageIndex > 0) {
            val newIndex = state.value.currentPageIndex - 1
            updateState {
                it.copy(
                    currentPageIndex = newIndex,
                    isLastPage = newIndex == state.value.totalPages - 1
                )
            }
        }
    }

    override fun onSkipClicked() {
        completeOnboardingAndNavigateToLogin()
    }

    override fun onGetStartedClicked() {
        completeOnboardingAndNavigateToLogin()
    }

    private fun completeOnboardingAndNavigateToLogin() {
        tryToExecute(
            action = { setOnboardingCompletedUseCase(true) },
            onSuccess = { sendNewNavigationEffect(OnboardingEffect.NavigateToLoginScreen) },
            onError = { sendNewNavigationEffect(OnboardingEffect.NavigateToLoginScreen) }
        )
    }

    fun setCurrentPage(newIndex: Int) {
        updateState {
            it.copy(
                currentPageIndex = newIndex,
                isLastPage = newIndex == state.value.totalPages - 1
            )
        }
    }
}

