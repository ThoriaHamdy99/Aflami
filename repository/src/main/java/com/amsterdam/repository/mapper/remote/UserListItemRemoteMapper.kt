package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.UserListItem
import com.amsterdam.repository.dto.remote.UserListItemDto
import com.amsterdam.repository.mapper.shared.EntityMapper
import com.amsterdam.repository.utils.toSafeLocalDate
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class UserListItemRemoteMapper @Inject constructor() : EntityMapper<UserListItemDto, UserListItem> {

    override fun toEntity(dto: UserListItemDto): UserListItem {
        return toEntity(dto, isPoster = true)
    }

    fun toEntity(dto: UserListItemDto, isPoster: Boolean): UserListItem {
        val imageUrl = if (isPoster) dto.fullPosterUrl else dto.fullBackdropUrl
        return UserListItem(
            id = dto.id,
            title = dto.title ?: dto.name.orEmpty(),
            description = dto.overview,
            posterUrl = imageUrl.orEmpty(),
            releaseDate = dto.releaseDate?.toSafeLocalDate() ?: dto.firstAirDate?.toSafeLocalDate()
            ?: LocalDate.fromEpochDays(0),
            rating = dto.voteAverage.toFloat(),
            mediaType = UserListItem.MediaType.valueOf(dto.mediaType.uppercase())
        )
    }

    fun toEntityList(dtoList: List<UserListItemDto>, isPoster: Boolean): List<UserListItem> {
        return dtoList.map { toEntity(it, isPoster) }
    }
}