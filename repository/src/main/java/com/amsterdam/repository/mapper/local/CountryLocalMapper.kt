package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.local.CountryLocalDto

fun CountryLocalDto.toEntity(): Country =
    Country(
        countryName = name,
        countryIsoCode = isoCode
    )

fun List<CountryLocalDto>.toEntityList(): List<Country> = map { it.toEntity() }
fun Country.LocalCountryDto(storedLanguage: String): CountryLocalDto =
    CountryLocalDto(
        name = countryName,
        storedLanguage = storedLanguage,
        isoCode = countryIsoCode
    )
