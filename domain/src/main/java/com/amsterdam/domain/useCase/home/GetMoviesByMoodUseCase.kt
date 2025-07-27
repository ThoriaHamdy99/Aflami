package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.models.Mood
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Movie
import javax.inject.Inject

class GetMoviesByMoodUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(mood: Mood): List<Movie> {
        return movieRepository.getMoviesByGenres(mood.movieGenres)
    }
}