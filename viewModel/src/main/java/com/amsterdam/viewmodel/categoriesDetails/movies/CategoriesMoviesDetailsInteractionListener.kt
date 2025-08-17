package com.amsterdam.viewmodel.categoriesDetails.movies

import androidx.paging.CombinedLoadStates
import com.amsterdam.domain.utils.category.MovieGenre

interface CategoriesMoviesDetailsInteractionListener {
    fun onClickBack()
    fun onClickMovieCard(movieId: Long)
    fun onClickGenre(movieGenre: MovieGenre)
    fun onClickRetryRequest()
    fun onPagingLoadStateChanged(loadStates: CombinedLoadStates)
}