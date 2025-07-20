package com.example.viewmodel.home

data class HomeUiState(
    val popularMovies : List<PopularMovie> = emptyList(),
    val isLoading : Boolean = false,
    val error : HomeError? = null
){
    data class PopularMovie(
        val name : String = "",
        val rating: String = "" ,
        val posterUrl : String = ""
    )
    sealed class HomeError{
        data object NetworkError : HomeError()
    }
}
