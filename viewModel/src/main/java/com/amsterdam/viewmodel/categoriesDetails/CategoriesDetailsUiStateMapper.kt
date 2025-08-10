package com.amsterdam.viewmodel.categoriesDetails

import com.amsterdam.entity.Movie
import com.amsterdam.viewmodel.shared.mappers.toFormattedRating
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.utils.toYearString

fun Movie.toMovieUiState( ): CategoriesDetailsUiState.MoviesUiState{
    return CategoriesDetailsUiState.MoviesUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        rate = rating.toFormattedRating(),
        yearOfRelease = releaseDate.toYearString(),
        mediaType = MediaType.MOVIE,
    )


}