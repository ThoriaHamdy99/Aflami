package com.amsterdam.viewmodel.categoriesDetails

import com.amsterdam.entity.category.MovieGenre

interface CategoriesMoviesDetailsInteractionListener {
    fun onBackClicked()
    fun onMovieCardClicked(movieId: Long)
    fun onGenreClicked(movieGenre: MovieGenre)
    fun onClickRetryRequest(movieGenre: MovieGenre, page: Int)


}