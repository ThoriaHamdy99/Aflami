package com.amsterdam.repository.utils

import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.local.LocalMovieDto
import kotlinx.datetime.LocalDate

val localMovieDto1 = LocalMovieDto(
    movieId = 1,
    storedLanguage = "en",
    name = "Movie A",
    description = "Overview A",
    poster = "poster1.jpg",
    releaseDate = LocalDate(2022, 1, 1),
    popularity = 75.5,
    rating = 8.0f,
    originCountry = "USA",
    movieLength = 120,
)
val localMovieDto2 = LocalMovieDto(
    movieId = 2,
    storedLanguage = "en",
    name = "Movie B",
    description = "Overview B",
    poster = "poster2.jpg",
    releaseDate = LocalDate(2022, 2, 1),
    popularity = 80.0,
    rating = 7.0f,
    originCountry = "UK",
    movieLength = 100,

    )
val movie1 = Movie(
    id = localMovieDto1.movieId,
    name = localMovieDto1.name,
    description = localMovieDto1.description,
    posterUrl = localMovieDto1.poster,
    releaseDate = localMovieDto1.releaseDate,
    categories = emptyList(),
    rating = localMovieDto1.rating,
    popularity = localMovieDto1.popularity,
    originCountry = localMovieDto1.originCountry,
    runTimeInMinutes = localMovieDto1.movieLength,
)
val movie2 = Movie(
    id = localMovieDto2.movieId,
    name = localMovieDto2.name,
    description = localMovieDto2.description,
    posterUrl = localMovieDto2.poster,
    releaseDate = localMovieDto2.releaseDate,
    categories = emptyList(),
    rating = localMovieDto2.rating,
    popularity = localMovieDto2.popularity,
    originCountry = localMovieDto2.originCountry,
    runTimeInMinutes = localMovieDto2.movieLength,
)