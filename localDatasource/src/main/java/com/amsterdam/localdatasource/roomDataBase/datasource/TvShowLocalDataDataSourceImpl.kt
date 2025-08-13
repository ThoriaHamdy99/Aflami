package com.amsterdam.localdatasource.roomDataBase.datasource

import androidx.room.Transaction
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowDao
import com.amsterdam.repository.datasource.local.TvShowLocalDataSource
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.PopularTvShowDto
import com.amsterdam.repository.dto.local.TopRatedTvShowDto
import com.amsterdam.repository.dto.local.TvShowCategoryCrossRefDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategories
import kotlinx.datetime.Instant
import javax.inject.Inject

class TvShowLocalDataDataSourceImpl @Inject constructor(
    private val tvShowDao: TvShowDao,
    private val tvShowCategoryInterestDao: TvShowCategoryInterestDao
) : TvShowLocalDataSource {
    @Transaction
    override suspend fun upsertTvShowWithCategories(
        tvShow: LocalTvShowDto,
        categoryIds: List<Long>,
        storedLanguage: String
    ) {
        tvShowDao.upsertTvShow(tvShow)
        val tvShowCrossRefs = categoryIds.map { categoryId ->
            TvShowCategoryCrossRefDto(
                tvShowId = tvShow.tvShowId,
                categoryId = categoryId,
                storedLanguage = storedLanguage
            )
        }
        tvShowDao.upsertTvShowCategoryCrossRefs(tvShowCrossRefs)
    }

    override suspend fun incrementGenreInterest(categoryId: Long) {
        tvShowCategoryInterestDao.incrementInterest(categoryId)
    }

    override suspend fun upsertTvShow(tvShow: LocalTvShowDto) {
        tvShowDao.upsertTvShow(tvShow)
    }

    override suspend fun getTvShowById(
        tvShowId: Long,
        storedLanguage: String
    ): LocalTvShowDto? {
        return tvShowDao.getTvShowById(tvShowId, storedLanguage)
    }

    override suspend fun getPopularTvShows(storedLanguage: String): List<TvShowWithCategories> {
        return tvShowDao.getPopularTvShows(storedLanguage)
    }

    override suspend fun getTopRatedTvShows(storedLanguage: String): List<LocalTvShowDto> {
        return tvShowDao.getTopRatedTvShows(storedLanguage)
    }

    @Transaction
    override suspend fun upsertPopularTvShows(tvShows: List<LocalTvShowDto>) {
        tvShowDao.upsertTvShows(tvShows)
        val entries = tvShows.map { tvShow ->
            PopularTvShowDto(
                tvShowId = tvShow.tvShowId,
                storedLanguage = tvShow.storedLanguage
            )
        }
        tvShowDao.upsertPopularTvShows(entries)
    }

    override suspend fun deleteExpiredPopularTvShows(
        expirationTime: Instant,
        storedLanguage: String
    ) {
        tvShowDao.deleteExpiredPopularTvShows(expirationTime, storedLanguage)
    }

    override suspend fun upsertTopRatedTvShows(tvShows: List<LocalTvShowDto>) {
        tvShowDao.upsertTvShows(tvShows)
        val entries = tvShows.map { tvShow ->
            TopRatedTvShowDto(
                tvShowId = tvShow.tvShowId,
                storedLanguage = tvShow.storedLanguage
            )
        }
        tvShowDao.upsertTopRatedTvShows(entries)
    }

    override suspend fun deleteExpiredTopRatedTvShows(
        expirationTime: Instant,
        storedLanguage: String
    ) {
        tvShowDao.deleteExpiredTopRatedTvShows(expirationTime, storedLanguage)
    }
}