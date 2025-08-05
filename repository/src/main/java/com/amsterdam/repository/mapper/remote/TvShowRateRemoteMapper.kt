package com.amsterdam.repository.mapper.remote

import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase.UserRatedTvShow
import com.amsterdam.repository.dto.remote.RemoteTvShowItemDto
import javax.inject.Inject

//class TvShowRateRemoteMapper @Inject constructor(
//    private val movieRemoteMapper: TvShowRemoteMapper
//) : EntityMapper<RemoteTvShowItemDto, UserRatedTvShow> {
//    override fun toEntity(dto: RemoteTvShowItemDto): UserRatedTvShow {
//        return with(dto) {
//            UserRatedTvShow(
//                tvShow = movieRemoteMapper.toEntity(dto = dto),
//                userRate = rating.toInt()
//            )
//        }
//    }
//}
fun RemoteTvShowItemDto.toTvShowUserRateEntity(dto: RemoteTvShowItemDto): UserRatedTvShow {
    return UserRatedTvShow(
        tvShow = dto.toEntity(),
        userRate = rating.toInt()
    )
}

fun List<RemoteTvShowItemDto>.toTvShowUserRateEntityList(): List<UserRatedTvShow> = map { it.toTvShowUserRateEntity(it)}
