package com.amsterdam.remotedatasource.serviceProvider

import com.amsterdam.repository.dto.remote.RemoteCountryDto

interface CountryServiceProvider {
    suspend fun getCountries(): List<RemoteCountryDto>
}