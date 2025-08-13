package com.amsterdam.repository.mapper.remote

import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase.UserRatedTvShow
import com.amsterdam.repository.dto.remote.TvShowRemoteItemDto

fun TvShowRemoteItemDto.toTvShowUserRateEntity(dto: TvShowRemoteItemDto): UserRatedTvShow {
    return UserRatedTvShow(
        tvShow = dto.toEntity(),
        userRate = rating.toInt()
    )
}

fun List<TvShowRemoteItemDto>.toTvShowUserRateEntityList(): List<UserRatedTvShow> = map { it.toTvShowUserRateEntity(it)}
