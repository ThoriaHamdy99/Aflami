package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.WatchHistoryDao
import com.amsterdam.repository.datasource.local.WatchHistoryLocalDataSource
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WatchHistoryLocalDataSourceImpl @Inject constructor(
    private val dao: WatchHistoryDao
) : WatchHistoryLocalDataSource {

    override suspend fun addMovieToWatchHistory(item: MovieWatchHistoryDto) =
        dao.addMovieToWatchHistory(item)

    override fun getMovieContinueWatching(page: Int, pageSize: Int,storedLanguage: String): Flow<List<LocalMovieDto>> {
        val offset = (page - 1) * pageSize
        return dao.getMovieContinueWatching(offset, pageSize,storedLanguage)
    }

    override suspend fun addTvShowToWatchHistory(item: TvShowWatchHistoryDto) =
        dao.addTvShowToWatchHistory(item)

    override fun getTvShowContinueWatching(page: Int, pageSize: Int,storedLanguage: String): Flow<List<LocalTvShowDto>> {
        val offset = (page - 1) * pageSize
        return dao.getTvShowContinueWatching(offset, pageSize,storedLanguage)
    }
}

