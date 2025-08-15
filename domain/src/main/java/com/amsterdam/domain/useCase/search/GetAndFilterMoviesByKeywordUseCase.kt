package com.amsterdam.domain.useCase.search

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.utils.category.MovieGenre
import com.amsterdam.domain.utils.category.toMovieGenre
import com.amsterdam.entity.Movie
import kotlin.math.floor

class GetAndFilterMoviesByKeywordUseCase(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(
        keyword: String,
        page: Int = 1,
        moviesPerPage: Int = 20,
        rating: Int = 0,
        movieGenre: MovieGenre = MovieGenre.ALL
    ): List<Movie> {
        return movieRepository
            .getMoviesByKeyword(keyword = keyword, page, moviesPerPage)
            .filterMoviesWithRatingAndGenre(rating, genre = movieGenre)
    }


    private fun List<Movie>.filterMoviesWithRatingAndGenre(
        rating: Int,
        genre: MovieGenre
    ): List<Movie> {
        return this.filter { item -> floor(item.rating) >= rating }
            .filter { movie ->
                if (genre == MovieGenre.ALL) return@filter true
                movie.categories.any { it.toMovieGenre() == genre }
            }
    }
}