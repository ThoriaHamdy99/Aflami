package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.entity.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContinueWatchingMoviesUseCase @Inject constructor(private val watchHistoryRepository: WatchHistoryRepository) {

    suspend operator fun invoke(): Flow<List<Movie>> {
        return watchHistoryRepository.getContinueWatchingMovies()
    }

}