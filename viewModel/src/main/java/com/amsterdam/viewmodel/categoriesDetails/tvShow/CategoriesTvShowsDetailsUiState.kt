package com.amsterdam.viewmodel.categoriesDetails.tvShow

import androidx.paging.PagingData
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.viewmodel.shared.defaultTvShowGenres
import com.amsterdam.viewmodel.shared.uiStates.TvShowGenreItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CategoriesTvShowsDetailsUiState(
    val selectedGenre: TvShowGenre= TvShowGenre.COMEDY,
    val tvShowGenres: List<TvShowGenreItemUiState> = defaultTvShowGenres,
    val tvShows: Flow<PagingData<TvShowsUiState>> = emptyFlow(),
    val isLoading: Boolean = false,
    val isRetryLoading : Boolean = false
) {
    data class TvShowsUiState(
        val id: Long = 0,
        val name: String = "",
        val posterImageUrl: String = "",
        val rate: String = "",
        val yearOfRelease: String = "",
        val isAdult : Boolean = false
    )
}