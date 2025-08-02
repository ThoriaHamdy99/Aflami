package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.entity.TvShowWatchHistory
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.MovieLocalSource
import com.amsterdam.repository.datasource.local.TvShowLocalSource
import com.amsterdam.repository.datasource.local.WatchHistoryLocalDataSource
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.datasource.remote.TvShowsRemoteSource
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.mapper.local.MovieWatchHistoryLocalMapper
import com.amsterdam.repository.mapper.local.TvWatchHistoryLocalMapper
import com.amsterdam.repository.mapper.remoteToLocal.MovieRemoteDetailsLocalMapper
import com.amsterdam.repository.mapper.remoteToLocal.TvShowRemoteDetailsLocalMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WatchHistoryRepositoryImpl @Inject constructor(
    private val watchHistoryLocalDataSource: WatchHistoryLocalDataSource,
    private val movieLocalSource: MovieLocalSource,
    private val movieRemoteDataSource: MovieRemoteSource,
    private val tvShowLocalDataSource: TvShowLocalSource,
    private val tvShowRemoteSource: TvShowsRemoteSource,
    private val preferences: AppPreferences,
    private val tvShowRemoteDetailsLocalMapper: TvShowRemoteDetailsLocalMapper,
    private val movieRemoteDetailsLocalMapper: MovieRemoteDetailsLocalMapper,
    private val localTvDataSource: TvShowLocalSource,
    private val movieWatchHistoryLocalMapper: MovieWatchHistoryLocalMapper,
    private val tvWatchHistoryLocalMapper: TvWatchHistoryLocalMapper,
) : WatchHistoryRepository {

    override suspend fun addMovieToWatchHistory(movieId: Long) {
        watchHistoryLocalDataSource.addMovieToWatchHistory(MovieWatchHistoryDto(movieId))
    }

    override fun getContinueWatchingMovies(
        page: Int,
        pageSize: Int
    ): Flow<List<MovieWatchHistory>> = flow {
        runCatching {
            watchHistoryLocalDataSource.getMoviesWatchHistory(page, pageSize).collect {
                it.map { getMovieWatchHistory(it) }
                    .also { emit(it) }
            }
        }
    }

    private suspend fun getMovieWatchHistory(movieWatchHistoryDto: MovieWatchHistoryDto): MovieWatchHistory {
        val language = preferences.getDeviceLanguage().first()
        return movieLocalSource.getMovieById(
            movieWatchHistoryDto.movieId,
            language
        )?.let {
            movieWatchHistoryLocalMapper.toEntity(
                dto = it.copy(insertedDate = movieWatchHistoryDto.watchedDate)
            )
        } ?: movieWatchHistoryLocalMapper.toEntity(
            dto = movieRemoteDetailsLocalMapper.toLocal(
                remote = movieRemoteDataSource.getMovieDetailsById(movieWatchHistoryDto.movieId)
                    .also { cacheWatchedMovie(it) },
                args = listOf(language)
            )
        )

    }

    private suspend fun cacheWatchedMovie(remoteMovieDetailsResponse: RemoteMovieDetailsResponse) {
        movieLocalSource.insertMovie(
            movie = movieRemoteDetailsLocalMapper.toLocal(
                remote = remoteMovieDetailsResponse,
                args = listOf(preferences.getDeviceLanguage().first())
            )
        )
    }

    override suspend fun addTvShowToWatchHistory(tvShowId: Long) {
        watchHistoryLocalDataSource.addTvShowToWatchHistory(TvShowWatchHistoryDto(tvShowId))
    }

    override fun getContinueWatchingTvShows(
        page: Int,
        pageSize: Int
    ): Flow<List<TvShowWatchHistory>> = flow {
        runCatching {
            watchHistoryLocalDataSource.getTvShowsWatchHistory(page, pageSize).collect {
                it.map { getTvShowWatchHistory(it) }
                    .also { emit(it) }
            }
        }
    }


    private suspend fun getTvShowWatchHistory(tvShowWatchHistoryDto: TvShowWatchHistoryDto): TvShowWatchHistory {
        val language = preferences.getDeviceLanguage().first()

        return tvShowLocalDataSource.getTvShowById(
            tvShowWatchHistoryDto.tvShowId,
            language
        )?.let {
            tvWatchHistoryLocalMapper.toEntity(
                dto = it.copy(insertedDate = tvShowWatchHistoryDto.watchedDate),
            )
        } ?: tvWatchHistoryLocalMapper.toEntity(
            dto = tvShowRemoteDetailsLocalMapper.toLocal(
                remote = tvShowRemoteSource.getTvShowDetailsById(tvShowWatchHistoryDto.tvShowId)
                    .also { cacheWatchedTvShow(it) },
                args = listOf(language)
            )
        )
    }

    private suspend fun cacheWatchedTvShow(remoteTvShowItemDto: TvShowDetailsRemoteResponse) {
        localTvDataSource.insertTvShow(
            tvShow = tvShowRemoteDetailsLocalMapper.toLocal(
                remote = remoteTvShowItemDto,
                args = listOf(preferences.getDeviceLanguage().first())
            )
        )
    }
}