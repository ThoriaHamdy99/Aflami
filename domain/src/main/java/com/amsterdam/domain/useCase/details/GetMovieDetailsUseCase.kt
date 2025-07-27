package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.models.MovieDetails
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.common.AddWatchHistoryUseCase

class GetMovieDetailsUseCase(
    private val movieRepository: MovieRepository,
    private val addWatchHistoryUseCase: AddWatchHistoryUseCase,
) {
    suspend operator fun invoke(movieId: Long): MovieDetails {
//        val movieDetails = movieRepository.getMovieDetailsById(movieId)
//        val reviews = movieRepository.getMovieReviews(movieId)
//        val actors = movieRepository.getActorsByMovieId(movieId)
//        val similarMovies = movieRepository.getSimilarMovies(movieId)
//        val movieGallery = movieRepository.getMovieGallery(movieId)
//        val moviePosters = movieRepository.getMoviePosters(movieId).take(10)
//        val productionsCompanies = movieRepository.getProductionCompany(movieId)

        return movieRepository.getMovieDetailsById(movieId).also { addWatchHistoryUseCase(movieId) }
    }

//    data class MovieDetails(
//        val movie: Movie,
//        val reviews: List<Review>,
//        val categories: List<MovieGenre>,
//        val actors: List<Actor>,
//        val similarMovies: List<Movie>,
//        val movieGallery: List<String>,
//        val moviePosters: List<String>,
//        val productionsCompanies: List<ProductionCompany>
//    )
}