package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalCountryDto

interface CountryLocalDataSource {
    suspend fun upsertCountries(countries: List<LocalCountryDto>)

    suspend fun getCountries(storedLanguage: String): List<LocalCountryDto>
}