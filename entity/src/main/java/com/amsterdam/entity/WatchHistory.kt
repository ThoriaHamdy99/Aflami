package com.amsterdam.entity

import kotlinx.datetime.Instant

data class WatchHistory(
    val movieId: Long,
    val lastWatchedTime: Instant
)