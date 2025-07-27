package com.amsterdam.viewmodel.home

import android.annotation.SuppressLint
import com.amsterdam.domain.useCase.home.GetHomeScreenDataUseCase
import com.amsterdam.entity.Movie
import com.amsterdam.viewmodel.home.HomeUiState.PopularMovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState

class HomeUiStateMapper {

    @SuppressLint("DefaultLocale")
    fun toUiState(homeScreenData: GetHomeScreenDataUseCase.HomeScreenData): HomeUiState {
        return HomeUiState(
            popularMovies = moviesToPopularMoviesUiState(homeScreenData.popularMovies),
            topRatedMovies = moviesToMoviesItemsUiState(homeScreenData.topRatedMovies),
            upcomingMovies = moviesToMoviesItemsUiState(homeScreenData.upComingMovies),
            continueWatchingMovies = moviesToMoviesItemsUiState(homeScreenData.continueWatchingMovies)
        )
    }

    fun moviesToPopularMoviesUiState(movies: List<Movie>) = movies.map(::movieToPopularMovieUiState)
    fun moviesToMoviesItemsUiState(movies: List<Movie>) = movies.map(::movieToMovieItemUiState)

    @SuppressLint("DefaultLocale")
    private fun movieToPopularMovieUiState(movie: Movie): PopularMovieItemUiState {
        return PopularMovieItemUiState(
            id = movie.id,
            name = movie.name,
            rating = String.format("%.1f", movie.rating),
            posterUrl = movie.posterUrl,
            category = movie.categories.map { it.name }
        )
    }

    @SuppressLint("DefaultLocale")
    private fun movieToMovieItemUiState(movie: Movie): MovieItemUiState {
        return MovieItemUiState(
            id = movie.id,
            name = movie.name,
            rate = String.format("%.1f", movie.rating),
            posterImageUrl = movie.posterUrl,
            yearOfRelease = movie.releaseDate.year.toString()
        )
    }
}