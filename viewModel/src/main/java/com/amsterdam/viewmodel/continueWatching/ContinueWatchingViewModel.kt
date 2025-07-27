package com.amsterdam.viewmodel.continueWatching

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.home.GetContinueWatchingMoviesUseCase
import com.amsterdam.entity.Movie
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class ContinueWatchingViewModel @Inject constructor(
    private val getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase,
    private val continueWatchingUiStateMapper: ContinueWatchingUiStateMapper,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<ContinueWatchingUiState, ContinueWatchingEffect>(
    ContinueWatchingUiState(),
    dispatcherProvider
),
    ContinueWatchingInteractionListener {

    init {
        getContinueWatchingMovies()
    }

    private fun getContinueWatchingMovies() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = ::loadContinueWatchingMovies,
            onSuccess = ::onGetContinueWatchingMoviesSuccess,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    private suspend fun loadContinueWatchingMovies(): List<Movie> {
        return getContinueWatchingMoviesUseCase()
            .firstOrNull() ?: emptyList()
    }

    private fun onGetContinueWatchingMoviesSuccess(continueWatchingMovies: List<Movie>) {
        updateState { continueWatchingUiStateMapper.toUiState(continueWatchingMovies) }
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

    override fun onClickMovie(movieId: Long) {
        sendNewEffect(ContinueWatchingEffect.NavigateToMovieDetailsScreen(movieId))
    }

    override fun onClickRetryLoading() {
        getContinueWatchingMovies()
    }

    override fun onClickBack() {
        sendNewEffect(ContinueWatchingEffect.NavigateBack)
    }

    private fun onCompletion() = updateState { it.copy(isLoading = false) }
}