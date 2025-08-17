package com.amsterdam.domain.model

import com.amsterdam.entity.Movie
import kotlinx.datetime.Instant

data class MovieWatchHistory(
    val movie: Movie,
    val lastWatchedTime: Instant
)