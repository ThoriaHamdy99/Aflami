package com.amsterdam.viewmodel.categoriesDetails.tvShow

import androidx.paging.CombinedLoadStates
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.entity.category.TvShowGenre

interface CategoriesTvShowsDetailsInteractionListener {
    fun onBackClicked()
    fun onTvShowCardClicked(tvShowId: Long)
    fun onGenreClicked(tvShowGenre: TvShowGenre)
    fun onClickRetryRequest()
    fun onPagingLoadStateChanged(loadStates: CombinedLoadStates)
}