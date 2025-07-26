package com.amsterdam.localdatasource.utils

import com.amsterdam.repository.dto.local.LocalMovieDto

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
            productionYear = 2023,
            popularity = 9.5,
            rating = 4.3f,
            originCountry = "US",
            movieLength = 120,
            hasVideo = true
        )
    }