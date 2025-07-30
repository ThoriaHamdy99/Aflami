package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import kotlinx.coroutines.flow.Flow

interface WatchHistoryLocalDataSource {
    suspend fun addMovieToWatchHistory(movieId : MovieWatchHistoryDto)
    fun getMovieContinueWatching(page: Int, pageSize: Int,storedLanguage: String): Flow<List<LocalMovieDto>>
    suspend fun addTvShowToWatchHistory(tvShowId : TvShowWatchHistoryDto)
    fun getTvShowContinueWatching(page: Int, pageSize: Int,storedLanguage: String): Flow<List<LocalTvShowDto>>
}