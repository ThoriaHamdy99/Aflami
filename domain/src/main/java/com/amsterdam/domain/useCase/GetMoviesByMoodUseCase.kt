package com.amsterdam.domain.useCase

import com.amsterdam.domain.models.Mood
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Movie

class GetMoviesByMoodUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(mood: Mood): List<Movie> {
        return movieRepository.getMoviesByGenres(mood.movieGenres)
    }
}