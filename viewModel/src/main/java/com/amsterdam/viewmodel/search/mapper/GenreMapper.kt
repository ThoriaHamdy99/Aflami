package com.amsterdam.viewmodel.search.mapper

import com.amsterdam.domain.utils.category.MovieGenre
import com.amsterdam.domain.utils.category.TvShowGenre
import com.amsterdam.viewmodel.shared.Selectable
import com.amsterdam.viewmodel.shared.uiStates.MovieGenreItemUiState
import com.amsterdam.viewmodel.shared.uiStates.TvGenreItemUiState

fun List<MovieGenreItemUiState>.selectByMovieGenre(movieGenre: MovieGenre): List<MovieGenreItemUiState> {
    return this.map { movies ->
        movies.copy(
            selectableMovieGenre = Selectable(
                item = movies.selectableMovieGenre.item,
                isSelected = movies.selectableMovieGenre.item == movieGenre
            )
        )
    }
}

fun List<TvGenreItemUiState>.selectByTvGenre(tvGenre: TvShowGenre): List<TvGenreItemUiState> {
    return this.map { tvShows ->
        tvShows.copy(
            selectableTvShowGenre = Selectable(
                item = tvShows.selectableTvShowGenre.item,
                isSelected = tvShows.selectableTvShowGenre.item == tvGenre
            )
        )
    }
}

fun List<MovieGenreItemUiState>.getSelectedGenreType(): MovieGenre {
    return this.find { it.selectableMovieGenre.isSelected }?.selectableMovieGenre?.item
        ?: MovieGenre.ALL
}

fun List<TvGenreItemUiState>.getSelectedGenreType(): TvShowGenre {
    return this.find { it.selectableTvShowGenre.isSelected }?.selectableTvShowGenre?.item
        ?: TvShowGenre.ALL
}