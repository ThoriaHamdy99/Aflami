package com.amsterdam.viewmodel.categoriesDetails

import androidx.lifecycle.SavedStateHandle
import com.amsterdam.entity.category.MovieGenre

class CategoriesMovieDetailsArgs (savedStateHandle: SavedStateHandle){
    val genre = savedStateHandle.get<String>(GENRE_NAME)
    val mediaType = savedStateHandle.get<String>(MEDIA_TYPE)



    companion object{
        const val GENRE_NAME = "genreName"
        const val MEDIA_TYPE = "mediaType"


    }
}