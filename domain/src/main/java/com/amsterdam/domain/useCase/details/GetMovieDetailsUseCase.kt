package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.models.MovieDetails
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.common.AddWatchHistoryUseCase

class GetMovieDetailsUseCase(
    private val movieRepository: MovieRepository,
    private val addWatchHistoryUseCase: AddWatchHistoryUseCase,
) {
    suspend operator fun invoke(movieId: Long): MovieDetails {
        return movieRepository.getMovieDetailsById(movieId).also { addWatchHistoryUseCase(movieId) }
    }
}