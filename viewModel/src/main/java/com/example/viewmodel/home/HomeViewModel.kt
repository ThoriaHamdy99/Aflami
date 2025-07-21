package com.example.viewmodel.home

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.useCase.GetHomeScreenDataUseCase
import com.example.domain.useCase.GetHomeScreenDataUseCase.HomeScreenData
import com.example.viewmodel.home.HomeUiState.HomeError
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.utils.dispatcher.DispatcherProvider

class HomeViewModel(
    private val getHomeScreenDataUseCase: GetHomeScreenDataUseCase,
    private val homeUiStateMapper: HomeUiStateMapper, dispatcherProvider: DispatcherProvider
) :
    BaseViewModel<HomeUiState, HomeEffect>(HomeUiState(), dispatcherProvider),
    HomeInteractionListener {

    init {
        getHomeScreenData()
    }

    private fun getHomeScreenData() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = { getHomeScreenDataUseCase() },
            onSuccess = ::onGetPopularMovieSuccess,
            onError = ::onError
        )
    }

    private fun onGetPopularMovieSuccess(homeScreenData: HomeScreenData) {
        updateState { homeUiStateMapper.toUiState(homeScreenData) }
    }

    override fun onClickRetryLoading() {
        getHomeScreenData()
    }

    override fun onClickSearch() {
        sendNewEffect(HomeEffect.NavigateToSearchScreenEffect)
    }

    override fun onClickMovie(movieId: Long) {
        sendNewEffect(HomeEffect.NavigateToMovieDetailsScreen(movieId))
    }

    override fun onClickShowAllToRatedMovies() {
        sendNewEffect(HomeEffect.NavigateToTopRatedMoviesScreen)
    }

    private fun onError(exception: AflamiException) {
        when (exception) {
            is NoInternetException -> updateState {
                it.copy(
                    isLoading = false,
                    error = HomeError.NetworkError
                )
            }

            else -> {}
        }
    }
}