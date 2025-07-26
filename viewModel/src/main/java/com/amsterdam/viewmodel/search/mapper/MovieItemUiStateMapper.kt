package com.amsterdam.viewmodel.search.mapper

import android.icu.text.DecimalFormat
import com.amsterdam.entity.Movie
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState

fun Movie.toMediaItemUiState(): MovieItemUiState =
    MovieItemUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        yearOfRelease = releaseDate.year.toString(),
        rate = DecimalFormat("#.#").format(rating).toString()
    )

fun List<Movie>.toMoveUiStates() = map(Movie::toMediaItemUiState)
