package com.amsterdam.viewmodel.categoriesDetails.tvShow

import androidx.paging.CombinedLoadStates
import com.amsterdam.domain.model.category.TvShowGenre

interface CategoriesTvShowsDetailsInteractionListener {
    fun onClickBack()
    fun onClickTvShowCard(tvShowId: Long)
    fun onClickGenre(tvShowGenre: TvShowGenre)
    fun onClickRetryRequest()
    fun onPagingLoadStateChanged(loadStates: CombinedLoadStates)
}