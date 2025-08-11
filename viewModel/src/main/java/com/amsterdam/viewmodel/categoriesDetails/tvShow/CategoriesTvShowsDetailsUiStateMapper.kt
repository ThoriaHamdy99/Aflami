package com.amsterdam.viewmodel.categoriesDetails.tvShow

import com.amsterdam.entity.TvShow
import com.amsterdam.viewmodel.shared.mappers.toFormattedRating

fun TvShow.toTvShowUiState(): CategoriesTvShowsDetailsUiState.TvShowsUiState{
    return CategoriesTvShowsDetailsUiState.TvShowsUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        rate = rating.toFormattedRating(),
        yearOfRelease =airDate.year.toString(),
    )
}