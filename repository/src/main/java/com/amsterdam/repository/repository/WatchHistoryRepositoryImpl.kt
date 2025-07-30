package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.entity.TvShowWatchHistory
import com.amsterdam.repository.datasource.local.WatchHistoryLocalDataSource
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import com.amsterdam.repository.mapper.local.MovieWatchHistoryLocalMapper
import com.amsterdam.repository.mapper.local.TvWatchHistoryLocalMapper
import com.amsterdam.repository.utils.getDeviceLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WatchHistoryRepositoryImpl @Inject constructor(
    private val watchHistoryLocalDataSource: WatchHistoryLocalDataSource,
    private val movieWatchHistoryLocalMapper: MovieWatchHistoryLocalMapper,
    private val tvWatchHistoryLocalMapper: TvWatchHistoryLocalMapper
) : WatchHistoryRepository {

    override suspend fun addMovieToWatchHistory(movieId : Long) {
        watchHistoryLocalDataSource.addMovieToWatchHistory(MovieWatchHistoryDto(movieId,getDeviceLanguage()))
    }

    override fun getContinueWatchingMovies(page: Int, pageSize: Int): Flow<List<MovieWatchHistory>> {
        return  watchHistoryLocalDataSource.getMovieContinueWatching(page,pageSize,getDeviceLanguage()).map(movieWatchHistoryLocalMapper::toEntityList)
    }

    override suspend fun addTvShowToWatchHistory(tvShowId: Long) {
        watchHistoryLocalDataSource.addTvShowToWatchHistory(TvShowWatchHistoryDto(tvShowId,getDeviceLanguage()))
    }

    override fun getContinueWatchingTvShows(page: Int, pageSize: Int): Flow<List<TvShowWatchHistory>> {
       return watchHistoryLocalDataSource.getTvShowContinueWatching(page,pageSize,getDeviceLanguage()).map(tvWatchHistoryLocalMapper::toEntityList)
    }
}