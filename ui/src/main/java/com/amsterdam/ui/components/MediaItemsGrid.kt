package com.amsterdam.ui.components

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
import com.amsterdam.ui.application.AflamiApp
import com.amsterdam.ui.screens.search.actorSearch.MovieImage
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun MediaItemsGrid(
    mediaItems: LazyPagingItems<MediaItemUiState>,
    modifier: Modifier = Modifier,
    onClickMediaItem: (Long, MediaType) -> Unit,
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
            count = mediaItems.itemCount,
        ) { index ->
            val mediaItem = mediaItems[index] ?: return@items
            val mediaType = when (mediaItem.mediaType) {
                MediaType.MOVIE -> stringResource(R.string.movie)
                MediaType.TV_SHOW -> stringResource(R.string.tv_shows)
            }

            Box {
                MediaCard(
                    movieImage = { MovieImage(mediaItem.posterImageUrl) },
                    movieType = mediaType,
                    movieYear = mediaItem.yearOfRelease,
                    movieTitle = mediaItem.name,
                    movieRating = mediaItem.rate,
                    onClick = { onClickMediaItem(mediaItem.id, mediaItem.mediaType) }
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

        if (mediaItems.loadState.append is LoadState.Loading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                LoadingIndicator(
                    Modifier
                        .size(48.dp)
                        .padding(top = 8.dp)
                )
            }
        }

        if (mediaItems.loadState.append is LoadState.Error) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                PlainTextButton(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(top = 8.dp),
                    title = stringResource(R.string.retry),
                    onClick = { mediaItems.retry() },
                    isEnabled = true,
                    isLoading = mediaItems.loadState.append is LoadState.Loading,
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
        MediaItemsGrid(
            mediaItems = emptyFlow<PagingData<MediaItemUiState>>().collectAsLazyPagingItems(),
            onClickMediaItem = { _, _ -> },
            hasIconButton = false,
            onClickIconButton = {}
        )
    }
}