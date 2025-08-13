package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.CountryRemoteDto

interface CountryRemoteSource {
    suspend fun getCountries(): List<CountryRemoteDto>
}