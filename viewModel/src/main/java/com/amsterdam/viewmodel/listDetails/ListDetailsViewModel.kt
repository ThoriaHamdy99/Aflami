package com.amsterdam.viewmodel.listDetails

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amsterdam.domain.useCase.list.GetUserListDetailsUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.paging.PagingSource
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ListDetailsViewModel @Inject constructor(
    private val getUserListDetailsUseCase: GetUserListDetailsUseCase,
    private val listDetailsUiStateMapper: ListDetailsUiStateMapper,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    args: ListDetailsArgs,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<ListDetailsUiState, ListDetailsEffect>(
    ListDetailsUiState(),
    dispatcherProvider
), ListDetailsInteractionListener {

    init {
        // TODO: args
        updateState { it.copy(listId = 8547208, listName = "My favorite") }
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
                            getUserListDetailsUseCase(state.value.listId, page)
                                .map { listDetailsUiStateMapper.listItemToMediaItemUiState(it) }
                        }
                    }
                ).flow.cachedIn(viewModelScope)
            },
            onSuccess = ::onGetListDetailsSuccess,
            onError = {},
            onCompletion = ::onCompletion
        )
    }

    private fun onGetListDetailsSuccess(mediaPagingFlow: Flow<PagingData<MediaItemUiState>>) {
        updateState { it.copy(listItems = mediaPagingFlow) }
    }

    private fun onCompletion() = updateState { it.copy(isLoading = false) }

    override fun onClickMediaItem(mediaId: Long, mediaType: MediaType) {
        when(mediaType){
            MediaType.MOVIE -> sendNewEffect(ListDetailsEffect.NavigateToMovieDetailsScreen(mediaId))
            MediaType.TV_SHOW -> sendNewEffect(ListDetailsEffect.NavigateToTvShowDetailsEffect(mediaId))
        }
    }

    override fun onClickBack() {
        sendNewEffect(ListDetailsEffect.NavigateBack)
    }

    override fun onClickRetryLoading() {
        loadListDetails()
    }
}