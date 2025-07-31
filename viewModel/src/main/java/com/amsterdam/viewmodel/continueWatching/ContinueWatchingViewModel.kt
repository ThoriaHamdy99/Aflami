package com.amsterdam.viewmodel.continueWatching

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.home.GetContinueWatchingScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetContinueWatchingScreenDataUseCase.ContinueWatchingScreenData
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import com.amsterdam.viewmodel.utils.getLinearItemsList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class ContinueWatchingViewModel @Inject constructor(
    private val getContinueWatchingScreenDataUseCase: GetContinueWatchingScreenDataUseCase,
    private val continueWatchingUiStateMapper: ContinueWatchingUiStateMapper,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<ContinueWatchingUiState, ContinueWatchingEffect>(
    ContinueWatchingUiState(),
    dispatcherProvider
),
    ContinueWatchingInteractionListener {

    init {
        manageLocaleLanguageUseCase.getDeviceLanguage()
            .onEach {
                getContinueWatchingData()
            }.launchIn(viewModelScope)
    }

    private fun getContinueWatchingData() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            val continueWatchingScreenData = getContinueWatchingScreenDataUseCase(page = page, pageSize = 20)
                            getLinearItemsList(continueWatchingScreenData.continueWatchingMovies.first(),
                                continueWatchingScreenData.continueWatchingTvShows.first(),
                                continueWatchingUiStateMapper::continueWatchingMediaItemUiState,
                                continueWatchingUiStateMapper::continueWatchingMediaItemUiState
                            ).sortedByDescending { it.dateAdded }
                        }
                    }
                ).flow.map { pagingData -> pagingData.map { it } }.cachedIn(viewModelScope)
            },
            onSuccess = ::onGetContinueWatchingScreenDataSuccess,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    fun onGetContinueWatchingScreenDataSuccess(mediaItems: Flow<PagingData<ContinueWatchingMediaItemUiState>>) {
        updateState { currentState ->
            currentState.copy(
                continueMediaItemUiStates = mediaItems
            )
        }
    }

    private fun onError(exception: AflamiException) {
        when (exception) {
            is NoInternetException -> updateState {
                it.copy(
                    error = ContinueWatchingUiState.ContinueWatchingError.NetworkError
                )
            }
            else ->{}
        }
    }

    override fun onClickMediaItem(mediaId: Long, mediaType: MediaType) {
        if (mediaType == MediaType.MOVIE)
            sendNewEffect(ContinueWatchingEffect.NavigateToMovieDetailsScreen(mediaId))
        else
            sendNewEffect(ContinueWatchingEffect.NavigateToTvShowDetailsEffect(mediaId))
    }

    override fun onClickRetryLoading() {
        getContinueWatchingData()
    }

    override fun onClickBack() {
        sendNewEffect(ContinueWatchingEffect.NavigateBack)
    }

    private fun onCompletion() = updateState { it.copy(isLoading = false) }
}