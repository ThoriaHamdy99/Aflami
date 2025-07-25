package com.amsterdam.viewmodel.search.mapper

import android.icu.text.DecimalFormat
import com.amsterdam.entity.TvShow
import com.amsterdam.viewmodel.shared.uiStates.TvShowItemUiState

fun TvShow.toMediaItemUiState(): TvShowItemUiState =
    TvShowItemUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        yearOfRelease = airDate.year.toString(),
        rate = DecimalFormat("#.#").format(rating).toString()
    )

fun List<TvShow>.toTvShowUiStates() = map(TvShow::toMediaItemUiState)