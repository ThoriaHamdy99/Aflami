package com.example.viewmodel.home

import android.util.Log
import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NetworkException
import com.example.domain.useCase.GetPopularMoviesUseCase
import com.example.domain.useCase.GetUpcomingMoviesUseCase
import com.example.entity.Movie
import com.example.entity.category.MovieGenre
import com.example.domain.exceptions.NoInternetException
import com.example.domain.useCase.GetHomeScreenDataUseCase
import com.example.domain.useCase.GetHomeScreenDataUseCase.HomeScreenData
import com.example.viewmodel.home.HomeUiState.HomeError
import com.example.viewmodel.search.mapper.selectByMovieGenre
import com.example.viewmodel.search.mapper.toMoveUiStates
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.utils.dispatcher.DispatcherProvider

class HomeViewModel(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getHomeScreenDataUseCase: GetHomeScreenDataUseCase,
    private val homeUiStateMapper: HomeUiStateMapper, dispatcherProvider: DispatcherProvider
) :
    BaseViewModel<HomeUiState, HomeEffect>(HomeUiState(), dispatcherProvider),
    HomeInteractionListener {

    init {
        getPopularMovies()
        getUpcomingMoviesBySelectedGenre()
        getHomeScreenData()
    }

    private fun getHomeScreenData() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = { getHomeScreenDataUseCase() },
            onSuccess = ::onGetPopularMovieSuccess,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    private fun onGetPopularMovieSuccess(movies: List<Movie>) {
        Log.e("bkh", "onGetPopularMovieSuccess: $movies")
        updateState { homeUiStateMapper.toUiState(movies) }
    private fun onGetPopularMovieSuccess(homeScreenData: HomeScreenData) {
        updateState { homeUiStateMapper.toUiState(homeScreenData) }
    }

    override fun onClickRetryLoading() {
        getHomeScreenData()
        updateState { it.copy(error = null) }
        getPopularMovies()
        getUpcomingMoviesBySelectedGenre()
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

    private fun getUpcomingMoviesBySelectedGenre(selectedUpcomingGenre: MovieGenre = MovieGenre.ALL) {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = { getUpcomingMoviesUseCase(selectedUpcomingGenre) },
            onSuccess = ::onGetUpcomingMovieSuccess,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    private fun onGetUpcomingMovieSuccess(movies: List<Movie>) {
        updateState { it.copy(upcomingMovies = movies.toMoveUiStates()) }
    }

    override fun onClickUpcomingMovieCard(id: Long) {
        sendNewEffect(HomeEffect.NavigateToMovieDetailsEffect(movieId = id))
    }

    override fun onChangeUpcomingMovieGenre(genre: MovieGenre) {
        updateState {
            it.copy(upcomingMovieGenres = it.upcomingMovieGenres.selectByMovieGenre(genre))
        }
        getUpcomingMoviesBySelectedGenre(selectedUpcomingGenre = genre)
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

    private fun onCompletion() = updateState { it.copy(isLoading = false) }
}