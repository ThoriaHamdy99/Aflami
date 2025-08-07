package com.amsterdam.viewmodel.shared.mappers

fun ratingToRatingString(rating: Float): String {
    return if  (rating % 1 == 0.0f) "${rating.toInt()}" else "%.1f".format(rating)
}