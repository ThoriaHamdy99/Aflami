package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.CountryLocalDto
import com.amsterdam.repository.dto.remote.RemoteCountryDto

fun RemoteCountryDto.toLocalDto(storedLanguage: String): CountryLocalDto =
    CountryLocalDto(
        name = nativeName,
        storedLanguage = storedLanguage,
        isoCode = isoCode
    )

fun List<RemoteCountryDto>.toLocalDtoList(storedLanguage: String): List<CountryLocalDto> = map { it.toLocalDto(storedLanguage) }