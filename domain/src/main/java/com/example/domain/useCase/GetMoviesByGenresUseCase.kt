package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import com.example.entity.Movie
import com.example.entity.category.MovieGenre

class GetMoviesByGenresUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(
        genres: List<MovieGenre>
    ): List<Movie> = movieRepository.getMoviesByGenres(genres)
}