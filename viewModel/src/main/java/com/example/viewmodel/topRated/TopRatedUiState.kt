package com.example.viewmodel.topRated

import com.example.viewmodel.home.HomeUiState.TopRatedMovieItemUiState

data class TopRatedUiState(
    val topRatedMovies: List<TopRatedMovieItemUiState> = emptyList(),
    val isLoading: Boolean = false,
    val error: TopRatedError? = null
){
    sealed class TopRatedError{
        data object NetworkError : TopRatedError()
    }
}