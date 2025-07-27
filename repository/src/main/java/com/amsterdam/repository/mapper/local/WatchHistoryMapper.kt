package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.WatchHistory
import com.amsterdam.repository.dto.local.WatchHistoryDto
import com.amsterdam.repository.mapper.shared.DtoMapper
import com.amsterdam.repository.mapper.shared.EntityMapper
import com.amsterdam.repository.utils.getDeviceLanguage
import javax.inject.Inject

class WatchHistoryMapper @Inject constructor(): EntityMapper<WatchHistoryDto, WatchHistory>,
    DtoMapper<WatchHistory, WatchHistoryDto> {

    override fun toEntity(dto: WatchHistoryDto): WatchHistory {
        return WatchHistory(
            movieId = dto.movieId,
            lastWatchedTime = dto.lastWatchedTime
        )
    }

    override fun toDto(entity: WatchHistory, args: List<Any>): WatchHistoryDto {
        return WatchHistoryDto(
            movieId = entity.movieId,
            storedLanguage = getDeviceLanguage(),
            lastWatchedTime = entity.lastWatchedTime
        )
    }
}
