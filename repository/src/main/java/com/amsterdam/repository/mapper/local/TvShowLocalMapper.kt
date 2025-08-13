package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.TvShow
import com.amsterdam.repository.dto.local.TvShowLocalDto

fun TvShowLocalDto.toEntity(): TvShow =
    TvShow(
        id = tvShowId,
        name = name,
        description = description,
        posterUrl = poster,
        airDate = airDate,
        rating = rating,
        categories = emptyList(),
        popularity = popularity,
        seasonCount = seasonCount,
        originCountry = originCountry
    )
