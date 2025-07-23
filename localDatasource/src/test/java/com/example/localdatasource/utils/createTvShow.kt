package com.example.localdatasource.utils

import com.example.repository.dto.local.LocalTvShowDto

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
    productionYear = 2022,
    rating = 8.5f,
    popularity = 123.4
)