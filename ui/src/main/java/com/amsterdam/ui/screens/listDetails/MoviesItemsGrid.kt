package com.amsterdam.ui.screens.listDetails


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.designsystem.components.IconButton
import com.amsterdam.designsystem.components.LoadingIndicator
import com.amsterdam.designsystem.components.buttons.ButtonDefaults
import com.amsterdam.designsystem.components.buttons.PlainTextButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R
import com.amsterdam.ui.components.MediaCard
import com.amsterdam.ui.screens.search.actorSearch.MovieImage
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun MoviesItemsGrid(
    movies: LazyPagingItems<MovieItemUiState>,
    modifier: Modifier = Modifier,
    onClickMovie: (Long) -> Unit,
    hasIconButton: Boolean = false,
    onClickIconButton: () -> Unit = {}
) {
    val gridState = rememberLazyGridState()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 156.dp),
        state = gridState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = movies.itemCount,
        ) { index ->
            val movie = movies[index] ?: return@items

            Box {
                MediaCard(
                    movieImage = { MovieImage(movie.posterImageUrl) },
                    movieType = stringResource(R.string.movie),
                    movieYear = movie.yearOfRelease,
                    movieTitle = movie.name,
                    movieRating = movie.rate,
                    onClick = { onClickMovie(movie.id) }
                )
                if (hasIconButton) {
                    IconButton(
                        modifier = Modifier
                            .padding(start = 4.dp, top = 4.dp)
                            .size(32.dp),
                        paddingValues = PaddingValues(6.dp),
                        painter = painterResource(R.drawable.ic_remove_item),
                        contentDescription = stringResource(R.string.remove_from_list),
                        containerColor = AppTheme.color.iconBackGround,
                        tint = AppTheme.color.redAccent,
                        onClick = onClickIconButton
                    )
                }
            }
        }

        if (movies.loadState.append is LoadState.Loading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                LoadingIndicator(
                    Modifier
                        .size(48.dp)
                        .padding(top = 8.dp)
                )
            }
        }

        if (movies.loadState.append is LoadState.Error) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                PlainTextButton(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(top = 8.dp),
                    title = stringResource(R.string.retry),
                    onClick = { movies.retry() },
                    isEnabled = true,
                    isLoading = movies.loadState.append is LoadState.Loading,
                    isNegative = false,
                    colors = ButtonDefaults.textButtonColors()
                )
            }
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun MediaItemsGridPreview() {
    AflamiTheme {
        MoviesItemsGrid(
            movies = emptyFlow<PagingData<MovieItemUiState>>().collectAsLazyPagingItems(),
            onClickMovie = { },
            hasIconButton = false,
            onClickIconButton = {}
        )
    }
}