package com.amsterdam.repository.mapper.remote

import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase.UserRatedMovie
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.mapper.shared.EntityMapper
import javax.inject.Inject

class MovieRateRemoteMapper @Inject constructor(
    private val movieRemoteMapper: MovieRemoteMapper
) : EntityMapper<RemoteMovieItemDto, UserRatedMovie> {
    override fun toEntity(dto: RemoteMovieItemDto): UserRatedMovie {
        return with(dto) {
            UserRatedMovie(
                movie = movieRemoteMapper.toEntity(dto = dto),
                userRate = rating.toInt()
            )
        }
    }
}