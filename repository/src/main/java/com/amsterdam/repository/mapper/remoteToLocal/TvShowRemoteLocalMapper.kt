package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.remote.RemoteTvShowItemDto
import com.amsterdam.repository.utils.toSafeLocalDate

fun RemoteTvShowItemDto.toLocalDto(storedLanguage: String): TvShowLocalDto {
    return TvShowLocalDto(
        tvShowId = id,
        storedLanguage = storedLanguage,
        name = title,
        description = overview,
        poster = fullPosterPath.orEmpty(),
        airDate = releaseDate.toSafeLocalDate(),
        rating = voteAverage.toFloat(),
        popularity = popularity,
        seasonCount = seasonCount,
        originCountry = originCountry.firstOrNull() ?: "",
    )
}

fun List<RemoteTvShowItemDto>.toLocalDtoList(storedLanguage: String) = map { it.toLocalDto(storedLanguage) }