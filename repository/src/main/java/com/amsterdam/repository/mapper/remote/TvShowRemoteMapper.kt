package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.repository.dto.remote.RemoteTvShowItemDto
import com.amsterdam.repository.mapper.shared.EntityMapper
import com.amsterdam.repository.mapper.shared.mapCategoryIdToTvShowGenre
import com.amsterdam.repository.utils.toSafeLocalDate

class TvShowRemoteMapper() : EntityMapper<RemoteTvShowItemDto, TvShow> {
    override fun toEntity(dto: RemoteTvShowItemDto): TvShow {
        return TvShow(
            id = dto.id,
            name = dto.title,
            description = dto.overview,
            posterUrl = dto.fullPosterPath.orEmpty(),
            airDate = dto.releaseDate.toSafeLocalDate(),
            categories = mapGenreIdsToCategories(dto.genreIds),
            rating = dto.voteAverage.toFloat(),
            popularity = dto.popularity,
            seasonCount = dto.seasonCount,
            originCountry = dto.originCountry.firstOrNull() ?: "",
        )
    }

    private fun mapGenreIdsToCategories(genreIds: List<Int>): List<TvShowGenre> {
        return genreIds.map { mapCategoryIdToTvShowGenre(it.toLong()) }
    }
}