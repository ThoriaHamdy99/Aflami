package com.example.repository.mapper.remote

import com.example.entity.TvShow
import com.example.repository.dto.remote.TvShowDetailsRemoteResponse
import com.example.repository.mapper.shared.EntityMapper
import com.example.repository.mapper.shared.mapCategoryIdToTvShowGenre
import com.example.repository.utils.toSafeLocalDate
import kotlinx.datetime.toLocalDate

class TvShowDetailsRemoteMapper() : EntityMapper<TvShowDetailsRemoteResponse, TvShow> {
    override fun toEntity(dto: TvShowDetailsRemoteResponse): TvShow {
        return TvShow(
            id = dto.id,
            name = dto.title,
            description = dto.overview,
            posterUrl = dto.fullPosterPath.orEmpty(),
            airDate = dto.releaseDate.toSafeLocalDate(),
            categories = dto.genres.map { mapCategoryIdToTvShowGenre(it.id.toLong()) },
            rating = dto.voteAverage.toFloat(),
            popularity = dto.popularity,
            seasonCount = dto.seasonCount,
            originCountry = dto.originCountry.firstOrNull() ?: ""
        )
    }
}