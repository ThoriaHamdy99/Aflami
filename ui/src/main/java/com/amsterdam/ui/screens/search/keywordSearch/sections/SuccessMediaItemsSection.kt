package com.amsterdam.ui.screens.search.keywordSearch.sections

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.components.MovieCard
import com.amsterdam.viewmodel.search.keywordSearch.SearchUiState
import com.amsterdam.viewmodel.search.keywordSearch.TabOption
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.TvShowItemUiState

fun LazyGridScope.successMediaItemsSection(
    selectedTabOption: TabOption,
    moviesFlow: LazyPagingItems<MovieItemUiState>,
    tvShowsFlow: LazyPagingItems<TvShowItemUiState>,
    onPageLoading: (Boolean) -> Unit,
    onMovieClicked: (movieId: Long) -> Unit,
    onTvShowClicked: (tvShowId: Long) -> Unit,
    state: SearchUiState,
) {
    val selectedItems = if (selectedTabOption == TabOption.MOVIES) {
        moviesFlow
    } else {
        tvShowsFlow
    }

    if(state.keyword.isNotBlank() && state.errorUiState == null && selectedItems.itemCount > 0){
    items(
        count = selectedItems.itemCount,
        key = { index -> getItemKey(selectedTabOption, index, selectedItems) },
    ) { index ->
            val mediaItem = selectedItems[index]
            when (mediaItem) {
                is MovieItemUiState -> {
                    MovieCard(
                        movieImage = {
                            SafeImageView(
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = mediaItem.name,
                                model = mediaItem.posterImageUrl,
                                contentScale = ContentScale.Crop,
                                onLoading = { ImageLoadingIndicator() },
                                onError = { ImageErrorIndicator() },
                            )
                        },
                        movieType = stringResource(R.string.movies),
                        movieYear = mediaItem.yearOfRelease,
                        movieTitle = mediaItem.name,
                        movieRating = mediaItem.rate,
                        onClick = { onMovieClicked(mediaItem.id) }
                    )
                }

                is TvShowItemUiState -> {
                    MovieCard(
                        movieImage = {
                            SafeImageView(
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = mediaItem.name,
                                model = mediaItem.posterImageUrl,
                                contentScale = ContentScale.Crop,
                                onLoading = { ImageLoadingIndicator() },
                                onError = { ImageErrorIndicator() },
                            )
                        },
                        movieType = stringResource(R.string.tv_shows),
                        movieYear = mediaItem.yearOfRelease,
                        movieTitle = mediaItem.name,
                        movieRating = mediaItem.rate,
                        onClick = { onTvShowClicked(mediaItem.id) }
                    )
                }
            }
        }
    }

    selectedItems.apply {
        when (loadState.refresh) {
            is LoadState.Loading -> {
                onPageLoading(true)
            }

            is LoadState.NotLoading -> {
                onPageLoading(false)
            }

            else -> onPageLoading(false)
        }
    }
}

private fun getItemKey(
    selectedTabOption: TabOption,
    index: Int,
    selectedItems: LazyPagingItems<out Any>
): String {
    val item = selectedItems[index]
    val id = if (selectedTabOption == TabOption.MOVIES) (item as MovieItemUiState).id
    else (item as TvShowItemUiState).id
    return "${id}-${index}"
}