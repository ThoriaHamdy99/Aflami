package com.amsterdam.viewmodel.listDetails

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amsterdam.domain.useCase.list.GetMoviesFromListUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.paging.PagingSource
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ListDetailsViewModel @Inject constructor(
    private val getMoviesFromListUseCase: GetMoviesFromListUseCase,
    private val listDetailsUiStateMapper: ListDetailsUiStateMapper,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    args: ListDetailsArgs,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<ListDetailsUiState, ListDetailsEffect>(
    ListDetailsUiState(),
    dispatcherProvider
), ListDetailsInteractionListener {

    init {
        updateState { it.copy(
            listId = args.listId,
            listName = args.listName
        ) }
        manageLocaleLanguageUseCase.getDeviceLanguage()
            .onEach { loadListDetails() }
            .launchIn(viewModelScope)

        loadListDetails()
    }

    private fun loadListDetails() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 10),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getMoviesFromListUseCase(state.value.listId, page)
                                .map { listDetailsUiStateMapper.movieToMovieItemUiState(it) }
                        }
                    }
                ).flow.cachedIn(viewModelScope)
            },
            onSuccess = ::onGetListDetailsSuccess,
            onError = {},
            onCompletion = ::onCompletion
        )
    }

    private fun onGetListDetailsSuccess(moviesPagingFlow: Flow<PagingData<MovieItemUiState>>) {
        updateState { it.copy(listItems = moviesPagingFlow) }
    }

    private fun onCompletion() = updateState { it.copy(isLoading = false) }

    override fun onClickMovie(movieId: Long) {
        sendNewEffect(ListDetailsEffect.NavigateToMovieDetailsScreen(movieId))
    }

    override fun onClickBack() {
        sendNewEffect(ListDetailsEffect.NavigateBack)
    }

    override fun onClickRetryLoading() {
        loadListDetails()
    }
}