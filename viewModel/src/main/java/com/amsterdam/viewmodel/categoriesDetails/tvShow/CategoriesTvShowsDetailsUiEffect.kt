package com.amsterdam.viewmodel.categoriesDetails.tvShow

interface CategoriesTvShowsDetailsUiEffect {
    data object NavigateBack : CategoriesTvShowsDetailsUiEffect
    data class NavigateToTvShowDetails(val tvShowId:Long) : CategoriesTvShowsDetailsUiEffect
}