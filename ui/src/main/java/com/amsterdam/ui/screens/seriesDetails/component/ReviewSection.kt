package com.amsterdam.ui.screens.seriesDetails.component

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.res.stringResource
import com.amsterdam.ui.R
import com.amsterdam.ui.screens.movieDetails.components.EmptyStateText
import com.amsterdam.ui.screens.movieDetails.components.ReviewCard
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsInteractionListener
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ReviewUiState

fun LazyListScope.ReviewSection(
    reviews: List<ReviewUiState>,
    interactionListener: SeriesDetailsInteractionListener
) {
    if (reviews.isEmpty())
        item {
            EmptyStateText(stringResource(R.string.there_is_no_reviews))
        }
    else
        itemsIndexed(reviews) { index, item ->
            ReviewCard(
                review = item,
                onToggleExpansion = { interactionListener.onReviewExpansionToggled(item.username) })
        }
}