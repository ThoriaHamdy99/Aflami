package com.example.localdatasource.utils

import com.example.repository.dto.local.LocalMovieDto
import kotlinx.datetime.LocalDate

fun createMovie(
        movieId: Long,
        storedLanguage: String,
        name : String = "Sample Movie"
    ): LocalMovieDto {
        return LocalMovieDto(
            movieId = movieId,
            storedLanguage = storedLanguage,
            name = name,
            description = "Test description",
            poster = "poster.jpg",
            releaseDate = LocalDate.parse("2020-01-01"),
            popularity = 9.5,
            rating = 4.3f,
            originCountry = "US",
            movieLength = 120,
            hasVideo = true
        )
    }