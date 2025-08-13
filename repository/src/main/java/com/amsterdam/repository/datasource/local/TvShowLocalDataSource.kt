package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategories
import kotlinx.datetime.Instant

interface TvShowLocalDataSource {
    suspend fun upsertTvShowWithCategories(
        tvShow: LocalTvShowDto,
        categoryIds: List<Long>,
        storedLanguage: String
    )

    suspend fun incrementGenreInterest(categoryId: Long)

    suspend fun upsertTvShow(tvShow: LocalTvShowDto)

    suspend fun getTvShowById(tvShowId: Long, storedLanguage: String): LocalTvShowDto?

    suspend fun getPopularTvShows(storedLanguage: String): List<TvShowWithCategories>

    suspend fun getTopRatedTvShows(storedLanguage: String): List<LocalTvShowDto>

    suspend fun upsertPopularTvShows(tvShows: List<LocalTvShowDto>)

    suspend fun deleteExpiredPopularTvShows(expirationTime: Instant, storedLanguage: String)

    suspend fun upsertTopRatedTvShows(tvShows: List<LocalTvShowDto>)

    suspend fun deleteExpiredTopRatedTvShows(expirationTime: Instant, storedLanguage: String)
}