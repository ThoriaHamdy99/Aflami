package com.amsterdam.viewmodel.categoriesDetails

import androidx.lifecycle.SavedStateHandle
import com.amsterdam.entity.category.MovieGenre

class CategoriesMovieDetailsArgs (savedStateHandle: SavedStateHandle){
    val genre = savedStateHandle.get<String>(GENRE_NAME)


    companion object{
        const val GENRE_NAME = "genreName"
    }
}