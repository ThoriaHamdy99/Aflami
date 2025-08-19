package com.amsterdam.viewmodel.continueWatching

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.amsterdam.domain.useCase.continueWatching.GetContinueWatchingDataUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.utils.MovieWatchHistory
import com.amsterdam.domain.utils.TvShowWatchHistory
import com.amsterdam.paging.createPagingSource
import com.amsterdam.viewmodel.continueWatching.ContinueWatchingUiState.ContinueWatchingItemUiState
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import com.amsterdam.viewmodel.utils.getLinearItemsList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ContinueWatchingViewModel @Inject constructor(
    private val getContinueWatchingScreenDataUseCase: GetContinueWatchingDataUseCase,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<ContinueWatchingUiState, ContinueWatchingEffect>(
    ContinueWatchingUiState(),
    dispatcherProvider
),
    ContinueWatchingInteractionListener {

    init {
        updateState { it.copy(isLoading = true) }
        manageLocaleLanguageUseCase.getAppLanguage()
                .onEach { getContinueWatchingData() }
                .launchIn(viewModelScope)
    }

    private fun getContinueWatchingData() {
        updateState { it.copy(isRetryLoading = true) }
        tryToExecute(
            action = {
                createPagingSource(scope = viewModelScope) { page ->
                    val continueWatchingScreenData = getContinueWatchingScreenDataUseCase(
                        page = page,
                        pageSize = 20
                    ).first()
                    getLinearItemsList(
                        continueWatchingScreenData.continueWatchingMovies,
                        continueWatchingScreenData.continueWatchingTvShows,
                        MovieWatchHistory::toContinueWatchingItemUiState,
                        TvShowWatchHistory::toContinueWatchingItemUiState
                    )
                            .sortedByDescending { it.dateAdded }
                            .take(10)
                }
            },
            onSuccess = ::onGetContinueWatchingScreenDataSuccess,
            onCompletion = ::onCompletion
        )
    }

    fun onGetContinueWatchingScreenDataSuccess(mediaItems: Flow<PagingData<ContinueWatchingItemUiState>>) {
        updateState { currentState ->
            currentState.copy(continueMediaItemUiStates = mediaItems)
        }
    }

    override fun onClickMediaItem(mediaId: Long, mediaType: MediaType) {
        if (mediaType == MediaType.MOVIE)
            sendNewNavigationEffect(ContinueWatchingEffect.NavigateToMovieDetailsScreen(mediaId))
        else
            sendNewNavigationEffect(ContinueWatchingEffect.NavigateToTvShowDetailsEffect(mediaId))
    }

    override fun onClickRetryLoading() = getContinueWatchingData()

    override fun onClickBack() = sendNewNavigationEffect(ContinueWatchingEffect.NavigateBack)

    private fun onCompletion() = updateState { it.copy(isLoading = false, isRetryLoading = false) }
}