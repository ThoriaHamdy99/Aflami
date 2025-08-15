package com.amsterdam.entity

import kotlinx.datetime.LocalDate

data class TvShow(
    val id: Long,
    val name: String,
    val description: String,
    val posterUrl: String,
    val airDate: LocalDate?,
    val categories: List<String>,
    val rating: Float,
    val popularity: Double,
    val seasonCount: Int,
    val originCountry: String,
    val videoUrl: String = ""
)