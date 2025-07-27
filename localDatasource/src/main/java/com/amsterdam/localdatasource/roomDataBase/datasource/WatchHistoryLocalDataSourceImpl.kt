package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.WatchHistoryDao
import com.amsterdam.repository.datasource.local.WatchHistoryLocalDataSource
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.WatchHistoryDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WatchHistoryLocalDataSourceImpl @Inject constructor(
    private val dao: WatchHistoryDao
) : WatchHistoryLocalDataSource {

    override suspend fun addToWatchHistory(item: WatchHistoryDto) =
        dao.addToWatchHistory(item)


    override suspend fun getContinueWatching(): Flow<List<LocalMovieDto>> =
         dao.getContinueWatching()

}
