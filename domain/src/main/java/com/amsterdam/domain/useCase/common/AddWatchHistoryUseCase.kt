package com.amsterdam.domain.useCase.common

import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.entity.WatchHistory
import kotlinx.datetime.Clock
import javax.inject.Inject

class AddWatchHistoryUseCase @Inject constructor(
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