package com.amsterdam.domain.repository

import com.amsterdam.entity.Movie
import com.amsterdam.entity.WatchHistory
import kotlinx.coroutines.flow.Flow

interface WatchHistoryRepository {
    suspend fun addToWatchHistory(item: WatchHistory)
    suspend fun getContinueWatchingMovies(): Flow<List<Movie>>
}