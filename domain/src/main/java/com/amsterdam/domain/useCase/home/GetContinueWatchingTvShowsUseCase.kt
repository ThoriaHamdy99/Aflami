package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.model.TvShowWatchHistory
import com.amsterdam.domain.repository.WatchHistoryRepository
import kotlinx.coroutines.flow.Flow

class GetContinueWatchingTvShowsUseCase(
    private val watchHistoryRepository: WatchHistoryRepository,
) {
    operator fun invoke(page: Int, pageSize: Int): Flow<List<TvShowWatchHistory>> {
        return watchHistoryRepository.getContinueWatchingTvShows(page,pageSize)
    }
}