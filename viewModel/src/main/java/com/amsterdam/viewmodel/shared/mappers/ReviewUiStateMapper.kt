package com.amsterdam.viewmodel.shared.mappers

import com.amsterdam.entity.Review
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ReviewUiState
import com.amsterdam.viewmodel.utils.dateToString
import com.amsterdam.viewmodel.shared.mappers.ratingToRatingString

fun Review.toUiState(): ReviewUiState {
    return ReviewUiState(
        author = reviewerName,
        username = reviewerUsername,
        rating = ratingToRatingString(rating),
        content = content,
        date = dateToString(date),
        imageUrl = imageUrl.takeIf { it.isNotBlank() }
    )
}