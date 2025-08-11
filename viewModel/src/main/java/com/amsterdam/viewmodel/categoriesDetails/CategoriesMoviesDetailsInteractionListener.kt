package com.amsterdam.viewmodel.categoriesDetails

import androidx.paging.CombinedLoadStates
import com.amsterdam.entity.category.MovieGenre

interface CategoriesMoviesDetailsInteractionListener {
    fun onBackClicked()
    fun onMovieCardClicked(movieId: Long)
    fun onGenreClicked(movieGenre: MovieGenre)
    fun onClickRetryRequest()
    fun onPagingLoadStateChanged(loadStates: CombinedLoadStates)
}