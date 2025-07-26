package com.amsterdam.domain.useCase

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Movie

class GetMoviesByActorUseCase(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(
        actorName: String,
        page: Int = 1,
        moviesPerPage: Int = 20
    ): List<Movie> {
        return movieRepository.getMoviesByActor(actorName, page, moviesPerPage)
    }
}