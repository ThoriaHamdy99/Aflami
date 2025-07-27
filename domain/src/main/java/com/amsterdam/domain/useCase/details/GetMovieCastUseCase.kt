package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Actor
import javax.inject.Inject

class GetMovieCastUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(movieId: Long): List<Actor>{
        return movieRepository.getActorsByMovieId(movieId)
            .sortedByDescending { it.popularity }

    }
}