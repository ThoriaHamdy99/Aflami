package com.amsterdam.domain.useCase

import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.entity.WatchHistory
import kotlinx.datetime.Clock

class AddWatchHistoryUseCase(
    private val watchHistoryRepository: WatchHistoryRepository
) {
    suspend operator fun invoke(movieId: Long) {
        val watchHistory = WatchHistory(
            movieId = movieId,
            lastWatchedTime = Clock.System.now()
        )
        watchHistoryRepository.addToWatchHistory(watchHistory)
    }
}