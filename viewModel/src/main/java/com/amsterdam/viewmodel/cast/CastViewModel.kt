package com.amsterdam.viewmodel.cast

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.GetMovieCastUseCase
import com.amsterdam.entity.Actor
import com.amsterdam.viewmodel.cast.CastUiState.CastErrorUiState
import com.amsterdam.viewmodel.cast.mapper.toUiState
import com.amsterdam.viewmodel.movieDetails.MovieDetailsArgs
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider

class CastViewModel(
    private val getMovieCastUseCase: GetMovieCastUseCase,
    private val args: MovieDetailsArgs,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<CastUiState, CastUiEffect>(CastUiState(), dispatcherProvider),
    CastInteractionListener {

    init { fetchMovieCast() }

    private fun fetchMovieCast() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = ::executeFetchMovieCast,
            onSuccess = ::onGetMovieCastSuccess,
            onError = ::onGetMovieCastError,
            onCompletion = ::onGetMovieCastCompletion,
        )
    }

    private suspend fun executeFetchMovieCast() =
        getMovieCastUseCase(args.movieId!!)

    private fun onGetMovieCastSuccess(cast: List<Actor>) {
        updateState { it.copy(cast = cast.map { it.toUiState() }, errorUiState = null) }
    }

    private fun onGetMovieCastError(exception: AflamiException) {
        val errorUiState = when (exception) {
            is NoInternetException -> CastErrorUiState.NoNetworkConnection
            else -> null
        }

        updateState { it.copy(errorUiState = errorUiState) }
    }

    private fun onGetMovieCastCompletion() = updateState { it.copy(isLoading = false) }

    override fun onClickNavigateBack() = sendNewEffect(CastUiEffect.NavigateBack)

    override fun onClickRetrySearch() = fetchMovieCast()

}