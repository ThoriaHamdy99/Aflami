package com.amsterdam.viewmodel.categoriesDetails

interface CategoriesDetailsUiEffect {
    data object NavigateBack : CategoriesDetailsUiEffect
    data class NavigateToMovieDetails(val movieId:Long) : CategoriesDetailsUiEffect

}