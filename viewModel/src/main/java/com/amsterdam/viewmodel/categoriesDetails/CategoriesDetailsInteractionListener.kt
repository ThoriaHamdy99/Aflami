package com.amsterdam.viewmodel.categoriesDetails

import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.viewmodel.shared.TabOption

interface CategoriesDetailsInteractionListener {
    fun onBackClicked()
    fun onMediaClicked(mediaId: Long)
    fun onMovieGenreClicked(movieGenre: MovieGenre)
    fun onTvGenreClicked(tvGenre: TvShowGenre)
    fun onClickRetryRequest(movieGenre: MovieGenre, page: Int)


}