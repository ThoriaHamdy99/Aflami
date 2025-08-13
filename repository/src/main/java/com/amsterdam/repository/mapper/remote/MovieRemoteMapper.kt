package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.remote.MovieItemRemoteDto
import com.amsterdam.repository.mapper.shared.toMovieGenre
import com.amsterdam.repository.utils.toSafeLocalDate

fun MovieItemRemoteDto.toEntity(
    isPoster: Boolean = true,
    videoUrl : String = ""
): Movie {
    val genreIds = genreIds.ifEmpty { genres.map { it.id } }
    val imageUrl = if (isPoster) fullPosterUrl else fullBackdropUrl

    return Movie(
        id = id,
        name = title,
        description = overview,
        posterUrl = imageUrl.orEmpty(),
        releaseDate = releaseDate.toSafeLocalDate(),
        categories = genreIds.map { toMovieGenre(it.toLong()) },
        rating = voteAverage.toFloat(),
        popularity = popularity,
        originCountry = originCountry.firstOrNull() ?: "",
        runTimeInMinutes = runtime,
        videoUrl = videoUrl
    )
}


fun List<MovieItemRemoteDto>.toMovieEntityList(isPoster: Boolean = true): List<Movie> =
    map { it.toEntity(isPoster) }