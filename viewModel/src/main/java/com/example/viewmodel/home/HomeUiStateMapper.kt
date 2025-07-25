package com.example.viewmodel.home

//import com.example.domain.useCase.GetHomeScreenDataUseCase
import android.annotation.SuppressLint
import com.example.entity.Movie
import com.example.viewmodel.home.HomeUiState.PopularMovieItemUiState
import com.example.viewmodel.shared.uiStates.MovieItemUiState

class HomeUiStateMapper {

    fun moviesToPopularMoviesUiState(movies: List<Movie>) = movies.map(::movieToPopularMovieUiState)
    fun moviesToMoviesItemsUiState(movies: List<Movie>) = movies.map(::movieToMovieItemUiState)

    @SuppressLint("DefaultLocale")
    private fun movieToPopularMovieUiState(movie: Movie): PopularMovieItemUiState {
        return PopularMovieItemUiState(
            name = movie.name,
            rating = String.format("%.1f", movie.rating),
            posterUrl = movie.posterUrl
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