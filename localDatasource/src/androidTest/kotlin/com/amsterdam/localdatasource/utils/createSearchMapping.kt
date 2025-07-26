package com.amsterdam.localdatasource.utils

import com.amsterdam.repository.dto.local.LocalTvShowWithSearchDto

fun createSearchMapping(
        id: Long = 1L,
        keyword: String = "breaking",
        language: String = "en"
    ) = LocalTvShowWithSearchDto(
        tvShowId = id,
        searchKeyword = keyword,
        storedLanguage = language
    )