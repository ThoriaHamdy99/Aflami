package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.remote.CountryRemoteDto

fun CountryRemoteDto.toEntity(): Country =
    Country(
        countryName = nativeName,
        countryIsoCode = isoCode
    )


fun List<CountryRemoteDto>.toEntityList(): List<Country> = map { it.toEntity() }