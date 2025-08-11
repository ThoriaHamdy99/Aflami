package com.amsterdam.viewmodel.categories


interface CategoriesUiEffect {
    data class NavigateCategoriesDetailsScreen(val genreName: String) : CategoriesUiEffect
}