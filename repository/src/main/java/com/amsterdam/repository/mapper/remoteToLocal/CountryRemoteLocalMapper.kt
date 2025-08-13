package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalCountryDto
import com.amsterdam.repository.dto.remote.CountryRemoteDto

fun CountryRemoteDto.toLocalDto(storedLanguage: String): LocalCountryDto =
    LocalCountryDto(
        name = nativeName,
        storedLanguage = storedLanguage,
        isoCode = isoCode
    )

fun List<CountryRemoteDto>.toLocalDtoList(storedLanguage: String): List<LocalCountryDto> = map { it.toLocalDto(storedLanguage) }