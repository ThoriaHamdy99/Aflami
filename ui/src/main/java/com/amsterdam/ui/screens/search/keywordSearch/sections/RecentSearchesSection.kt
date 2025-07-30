package com.amsterdam.ui.screens.search.keywordSearch.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.divider.HorizontalDivider
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.components.RecentSearchItem

internal fun LazyGridScope.recentSearchesSection(
    keyword: String,
    recentSearches: List<String>,
    onAllRecentSearchesCleared: () -> Unit,
    onRecentSearchClicked: (String) -> Unit,
    onRecentSearchCleared: (String) -> Unit,
    headerHeight: Dp,
    modifier: Modifier = Modifier,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        AnimatedVisibility(recentSearches.isNotEmpty() && keyword.isBlank()) {
            Row(
                modifier =
                    modifier
                        .fillMaxWidth()
                        .background(color = AppTheme.color.surface)
                        .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.recent_searches),
                    style = AppTheme.textStyle.title.medium,
                    color = AppTheme.color.title,
                    textAlign = TextAlign.Start,
                )

                Text(
                    modifier = modifier.clickable(onClick = onAllRecentSearchesCleared),
                    text = stringResource(R.string.clear_all),
                    style = AppTheme.textStyle.label.medium,
                    color = AppTheme.color.primary,
                )
            }
        }
    }

    items(
        items = recentSearches,
        key = { it },
        span = { GridItemSpan(maxLineSpan) },
    ) { recentSearchItem ->
        AnimatedVisibility(recentSearches.isNotEmpty() && keyword.isBlank()) {
            Column {
                RecentSearchItem(
                    modifier = Modifier.animateItem(),
                    title = recentSearchItem,
                    onItemClick = onRecentSearchClicked,
                    onCancelClick = onRecentSearchCleared,
                )
                if (recentSearchItem != recentSearches.last()) HorizontalDivider()
            }
        }
    }


    item(span = { GridItemSpan(maxLineSpan) }) {
        AnimatedVisibility(keyword.isEmpty() && recentSearches.isEmpty()) {
            CenterOfScreenContainer(unneededSpace = headerHeight) {
                ExploreMoviesAndShows(modifier = Modifier)
            }
        }
    }
}
