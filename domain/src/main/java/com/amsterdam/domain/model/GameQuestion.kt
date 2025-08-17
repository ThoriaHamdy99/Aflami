package com.amsterdam.domain.model

data class GameQuestion<T>(
    val question: String,
    val choices: List<T>,
    val correctChoice: T,
    val questionTime: Int,
)
