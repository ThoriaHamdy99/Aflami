package com.amsterdam.repository.mapper.remote

import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase.UserRatedMovie
import com.amsterdam.repository.dto.remote.MovieItemRemoteDto
import kotlin.collections.map

fun MovieItemRemoteDto.toMovieUserRateEntity(dto: MovieItemRemoteDto): UserRatedMovie {
    return UserRatedMovie(
        movie = dto.toEntity(),
        userRate = rating.toInt()
    )
}

fun List<MovieItemRemoteDto>.toMovieUserRateEntityList(): List<UserRatedMovie> = map { it.toMovieUserRateEntity(it) }