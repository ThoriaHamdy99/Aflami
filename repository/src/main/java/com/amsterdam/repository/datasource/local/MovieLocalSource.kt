package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import kotlinx.datetime.Instant

interface MovieLocalSource {
    suspend fun getMovieById(movieId: Long, storedLanguage: String): LocalMovieDto?

    suspend fun incrementGenreInterest(categoryId: Long)

    suspend fun upsertMovie(movie: LocalMovieDto)

    suspend fun upsertMovieWithCategories(
        movie: LocalMovieDto,
        categoryIds: List<Long>,
        storedLanguage: String
    )

    suspend fun getTopRatedMovies(storedLanguage: String): List<LocalMovieDto>

    suspend fun getPopularMovies(storedLanguage: String): List<MovieWithCategories>

    suspend fun getUpcomingMovies(storedLanguage: String): List<MovieWithCategories>

    suspend fun upsertPopularMovies(movies: List<LocalMovieDto>)

    suspend fun deleteExpiredPopularMovies(expirationTime: Instant, storedLanguage: String)

    suspend fun upsertTopRatedMovies(movies: List<LocalMovieDto>)

    suspend fun deleteAllExpiredTopRatedMovies(expirationTime: Instant, storedLanguage: String)

    suspend fun upsertUpcomingMovies(movies: List<LocalMovieDto>)

    suspend fun deleteExpiredUpcomingMovies(expirationTime: Instant, storedLanguage: String)

}