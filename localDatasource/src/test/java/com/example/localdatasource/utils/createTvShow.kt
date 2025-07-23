package com.example.localdatasource.utils

import com.example.repository.dto.local.LocalTvShowDto
import kotlinx.datetime.LocalDate

fun createTvShow(
    id: Long = 1L,
    language: String = "en",
    name: String = "Test Show"
) = LocalTvShowDto(
    tvShowId = id,
    storedLanguage = language,
    name = name,
    description = "Test Description",
    poster = "poster.jpg",
    rating = 8.5f,
    popularity = 123.4,
    airDate = LocalDate.parse("2020-01-01"),
    seasonCount = 5,
    originCountry = "eg"
)