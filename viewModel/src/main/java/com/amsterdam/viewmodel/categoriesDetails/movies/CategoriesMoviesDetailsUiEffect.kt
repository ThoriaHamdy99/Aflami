package com.amsterdam.viewmodel.categoriesDetails.movies

interface CategoriesMoviesDetailsUiEffect {
    data object NavigateBack : CategoriesMoviesDetailsUiEffect
    data class NavigateToMovieDetails(val movieId:Long) : CategoriesMoviesDetailsUiEffect

}