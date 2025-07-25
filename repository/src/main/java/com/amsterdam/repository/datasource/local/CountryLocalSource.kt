package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalCountryDto

interface CountryLocalSource {
    suspend fun addCountries(countries: List<LocalCountryDto>)
    suspend fun getCountries(storedLanguage: String): List<LocalCountryDto>
}