package com.amsterdam.domain.utils

import kotlin.time.Duration

data class GameQuestion<T>(
    val question: String,
    val choices: List<T>,
    val correctChoice: T,
    val questionTime: Duration,
)
