package com.example.viewmodel.home

data class HomeUiState(
    val popularMovies : List<PopularMovie> = emptyList(),
    val isLoading : Boolean = false,
    val networkError : Boolean = false
){
    data class PopularMovie(
        val name : String = "",
        val rating: String = "" ,
        val posterUrl : String = ""
    )
}
