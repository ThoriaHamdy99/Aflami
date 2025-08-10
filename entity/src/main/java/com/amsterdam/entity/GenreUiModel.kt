package com.amsterdam.entity

data class GenreUiModel(
    val genre: Enum<*>,
    val displayName: Int,
    val imageRes: Int
)