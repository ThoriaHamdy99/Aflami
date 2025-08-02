package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowDao
import com.amsterdam.repository.datasource.local.TvShowLocalSource
import com.amsterdam.repository.dto.local.LocalTvShowDto
import javax.inject.Inject

class TvShowLocalDataSourceImpl @Inject constructor(
    private val tvShowDao: TvShowDao,
    private val tvShowCategoryInterestDao: TvShowCategoryInterestDao
) : TvShowLocalSource {

    override suspend fun incrementGenreInterest(categoryId: Long) {
        tvShowCategoryInterestDao.incrementInterest(categoryId)
    }

    override suspend fun insertTvShow(tvShow: LocalTvShowDto) {
        tvShowDao.insertTvShow(tvShow)
    }

    override suspend fun getTvShowById(
        tvShowId: Long,
        storedLanguage: String
    ): LocalTvShowDto? {
        return tvShowDao.getTvShowById(tvShowId, storedLanguage)
    }
}