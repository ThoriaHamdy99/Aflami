package com.amsterdam.viewmodel.categoriesDetails

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.viewmodel.shared.defaultMovieGenres
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.shared.uiStates.MovieGenreItemUiState
import com.amsterdam.viewmodel.shared.uiStates.TvGenreItemUiState


data class CategoriesDetailsUiState(
    val selectedGenreName: String = "",
    val movieGenres: List<MovieGenreItemUiState> = defaultMovieGenres,
    val movies: List<MoviesUiState> = emptyList(),
    val isLoading: Boolean = false,
    val errorUiState: CategoriesDetailsErrorState? = null,
) {
    data class MoviesUiState(
        val id: Long = 0,
        val name: String = "",
        val posterImageUrl: String = "",
        val rate: String = "",
        val yearOfRelease: String = "",
        val mediaType: MediaType = MediaType.MOVIE
    )
    sealed interface CategoriesDetailsErrorState {
        data object NoNetworkConnection : CategoriesDetailsErrorState
        data object UnknownError : CategoriesDetailsErrorState
        companion object {
            fun toSearchErrorState(exception: Throwable): CategoriesDetailsErrorState =
                when (exception) {
                    is NetworkException -> NoNetworkConnection
                    else -> UnknownError
                }
        }

    }
}