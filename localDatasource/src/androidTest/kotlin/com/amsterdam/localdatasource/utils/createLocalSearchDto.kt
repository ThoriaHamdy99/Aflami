package com.amsterdam.localdatasource.utils

import com.amsterdam.repository.dto.local.LocalSearchDto
import com.amsterdam.repository.dto.local.utils.SearchType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.milliseconds

fun createLocalSearchDto(
    keyword: String,
    type: SearchType,
    storedLanguage: String = "en",
    expireOffsetInMilliseconds: Long = 10_000,
    baseTime: Instant = Clock.System.now()
): LocalSearchDto {
    return LocalSearchDto(
        searchKeyword = keyword,
        searchType = type,
        storedLanguage = storedLanguage,
        expireDate = baseTime.plus(expireOffsetInMilliseconds.milliseconds)
    )
}