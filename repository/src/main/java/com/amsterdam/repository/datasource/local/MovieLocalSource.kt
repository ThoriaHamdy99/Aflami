package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalMovieDto

interface MovieLocalSource {
    suspend fun getMovieById(movieId: Long, storedLanguage: String): LocalMovieDto?

    suspend fun incrementGenreInterest(categoryId: Long)

    suspend fun insertMovie(movie: LocalMovieDto)
}