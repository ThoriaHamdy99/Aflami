package com.amsterdam.repository.mapper.remote

import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase.MovieDetails
import com.amsterdam.repository.dto.remote.Rated
import com.amsterdam.repository.dto.remote.MovieDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.MovieItemRemoteDto

fun MovieDetailsRemoteResponse.toMovieDetailsEntity(): MovieDetails {
    val movieItemDto = MovieItemRemoteDto(
        adult = adult,
        backdropPath = backdropPath,
        genreIds = genreIds,
        id = id,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        productionCompanies = productionCompanies,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        originCountry = originCountry,
        runtime = runtime,
        genres = genres
    )

    return MovieDetails(
        movie = movieItemDto.toEntity(videoUrl = videos.results.firstOrNull()?.fullVideoUrl?:""),
        reviews = reviews.results.toEntityList(),
        actors = credits.cast.toEntityList(),
        similarMovies = similar.results.toMovieEntityList(isPoster = false),
        movieGallery = images.toEntityList(),
        moviePosters = images.toEntityList(),
        productionCompanies = productionCompanies.toEntityList(),
        userRate = if(accountStates?.rated is Rated.RatedValue) accountStates.rated.value.toInt() else null
    )
}

fun MovieDetailsRemoteResponse.toMovieItemDto(): MovieItemRemoteDto {
    return MovieItemRemoteDto(
        adult = adult,
        backdropPath = backdropPath,
        genreIds = genreIds,
        id = id,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        productionCompanies = productionCompanies,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        originCountry = originCountry,
        runtime = runtime,
        genres = genres
    )
}