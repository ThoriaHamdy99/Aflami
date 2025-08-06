package com.amsterdam.viewmodel.movieDetails

import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase.MovieDetails
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Movie
import com.amsterdam.entity.ProductionCompany
import com.amsterdam.entity.Review
import com.amsterdam.viewmodel.shared.mappers.ratingToRatingString
import com.amsterdam.viewmodel.shared.mappers.toUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.SimilarMovieUiState
import com.amsterdam.viewmodel.utils.dateToString
import com.amsterdam.viewmodel.utils.movieLengthToHourMinuteString

fun MovieDetails.toUiState(): MovieDetailsUiState {
    return MovieDetailsUiState(
        movieId = movie.id,
        rating = ratingToRatingString(movie.rating),
        movieTitle = movie.name,
        categories = movie.categories,
        moviePostersUrl = moviePosters,
        releaseDate = dateToString(movie.releaseDate),
        movieLength = movieLengthToHourMinuteString(movie.runTimeInMinutes),
        originCountry = movie.originCountry,
        description = movie.description,
        videoUrl = movie.videoUrl,
        actors = actors.map(Actor::toUiState),
        extraItem = MovieDetailsUiState.defaultMovieExtras,
        similarMovies = similarMovies.map(Movie::toSimilarMovieUiState),
        productionCompany = productionCompanies.map(ProductionCompany::toUiState),
        gallery = movieGallery,
        reviews = reviews.map(Review::toUiState)
    )
}

private fun Movie.toSimilarMovieUiState(): SimilarMovieUiState{
    return SimilarMovieUiState(
        movieId = id,
        rate = ratingToRatingString(rating),
        name = name,
        productionYear = releaseDate.year.toString(),
        posterUrl = posterUrl
    )
}
