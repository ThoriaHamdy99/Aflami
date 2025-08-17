package com.amsterdam.viewmodel.shared.uiStates

import com.amsterdam.domain.model.category.MovieGenre
import com.amsterdam.viewmodel.shared.Selectable

data class MovieGenreItemUiState(
    val selectableMovieGenre: Selectable<MovieGenre> = Selectable(
        item = MovieGenre.ALL,
        isSelected = false
    )
)

