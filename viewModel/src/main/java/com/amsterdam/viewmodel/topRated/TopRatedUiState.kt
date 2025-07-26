package com.amsterdam.viewmodel.topRated

import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState

data class TopRatedUiState(
    val topRatedMovies: List<MovieItemUiState> = emptyList(),
    val isLoading: Boolean = false,
    val error: TopRatedError? = null
){
    sealed class TopRatedError{
        data object NetworkError : TopRatedError()
    }
}