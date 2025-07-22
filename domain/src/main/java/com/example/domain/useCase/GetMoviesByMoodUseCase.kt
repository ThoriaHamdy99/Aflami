package com.example.domain.useCase

import com.example.domain.models.Mood
import com.example.domain.repository.MovieRepository
import com.example.entity.Movie

class GetMoviesByMoodUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(mood: Mood): List<Movie> {
        return movieRepository.getMoviesByGenres(mood.movieGenres)
    }
}