package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Season
import com.amsterdam.repository.dto.remote.SeasonDto
import com.amsterdam.repository.mapper.shared.EntityMapper

class SeasonRemoteMapper : EntityMapper<SeasonDto, Season> {
    override fun toEntity(dto: SeasonDto): Season {
        return Season(
            id = dto.id,
            seasonNumber = dto.seasonNumber,
            title = dto.title,
            episodeCount = dto.episodeCount
        )
    }
}