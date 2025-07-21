package com.example.viewmodel.home

data class HomeUiState(
    val popularMovies : List<PopularMovieItemUiState> = emptyList(),
    val topRatedMovies : List<TopRatedMovieItemUiState> = emptyList(),
    val isLoading : Boolean = false,
    val error : HomeError? = null
){
    data class PopularMovieItemUiState(
        val name : String = "",
        val rating: String = "" ,
        val posterUrl : String = ""
    )

    data class TopRatedMovieItemUiState(
        val id : Long,
        val name : String = "",
        val rating: String = "" ,
        val posterImageUrl : String = "",
        val yearOfRelease : String = "2006"
    )

    sealed class HomeError{
        data object NetworkError : HomeError()
    }
}
