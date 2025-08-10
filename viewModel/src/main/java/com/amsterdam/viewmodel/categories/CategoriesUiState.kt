package com.amsterdam.viewmodel.categories

import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.shared.TabOption

data class CategoriesUiState(
    val selectedTabOption: TabOption = TabOption.MOVIES,
    val categories: List<MovieGenre> = emptyList(),
    val movieGenres: MovieGenre = MovieGenre.ACTION,
    )
