package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.utils.toSafeLocalDate

fun TvShowDetailsRemoteResponse.toLocalDto(storedLanguage: String): TvShowLocalDto {
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
