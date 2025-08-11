package com.amsterdam.viewmodel.categoriesDetails.tvShow

import androidx.lifecycle.SavedStateHandle

class CategoriesTvShowsDetailsArgs(savedStateHandle: SavedStateHandle) {
    val genre = savedStateHandle.get<String>(GENRE)


    companion object{
        const val GENRE = "genreName"

    }
}