package com.amsterdam.viewmodel.lists

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.useCase.lists.GetUserListsUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class UserListsViewModel @Inject constructor(
    private val getUserListsUseCase: GetUserListsUseCase,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<ListsUiState, ListsEffect>(
    ListsUiState(),
    dispatcherProvider,
),
    ListsInteractionListener {

    init {
        loadCustomLists()
    }

    init {
        manageLocaleLanguageUseCase.getDeviceLanguage()
            .onEach {
                loadCustomLists()
            }.launchIn(viewModelScope)

        loadCustomLists()
    }

    private fun loadCustomLists() {
        tryToExecute(
            action = {
                getUserListsUseCase()
            },
            onSuccess = { customLists ->
                updateState {
                    it.copy(
                        userLists = customLists.map { it.toUserListItemUiState() },
                        isLoading = false,
                        errorUiState = null
                    )
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorUiState = ListsErrorState.toListsErrorState(exception)
                    )
                }
            }
        )
    }

    override fun onClickAddCustomList() {
        sendNewEffect(ListsEffect.NavigateToAddCustomList)
    }

    override fun onClickRetryFetchList() {
        loadCustomLists()
    }


}