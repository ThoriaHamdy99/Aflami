package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalTvShowDto

interface TvShowLocalSource {
    suspend fun incrementGenreInterest(categoryId: Long)

    suspend fun insertTvShow(tvShow : LocalTvShowDto)

    suspend fun getTvShowById(tvShowId: Long, storedLanguage: String): LocalTvShowDto?
}