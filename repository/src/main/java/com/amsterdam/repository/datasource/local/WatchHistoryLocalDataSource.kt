package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.WatchHistoryDto
import kotlinx.coroutines.flow.Flow

interface WatchHistoryLocalDataSource {
    suspend fun addToWatchHistory(item: WatchHistoryDto)
    suspend fun getContinueWatching(): Flow<List<LocalMovieDto>>
}