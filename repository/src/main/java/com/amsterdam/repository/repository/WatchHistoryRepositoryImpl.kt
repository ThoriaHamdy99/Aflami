package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.entity.Movie
import com.amsterdam.entity.WatchHistory
import com.amsterdam.repository.datasource.local.WatchHistoryLocalDataSource
import com.amsterdam.repository.mapper.local.MovieLocalMapper
import com.amsterdam.repository.mapper.local.WatchHistoryMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WatchHistoryRepositoryImpl @Inject constructor(
    private val watchHistoryLocalDataSource: WatchHistoryLocalDataSource,
    private val movieLocalMapper: MovieLocalMapper,
    private val watchHistoryMapper: WatchHistoryMapper
) : WatchHistoryRepository {

    override suspend fun addToWatchHistory(item: WatchHistory) {
        watchHistoryLocalDataSource.addToWatchHistory(watchHistoryMapper.toDto(item, emptyList()))
    }

    override suspend fun getContinueWatchingMovies(): Flow<List<Movie>> {
        return watchHistoryLocalDataSource.getContinueWatching().map {
            movieLocalMapper.toEntityList(it)
        }
    }

}