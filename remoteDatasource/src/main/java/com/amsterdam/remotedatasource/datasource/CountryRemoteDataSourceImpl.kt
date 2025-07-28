package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.serviceProvider.CountryServiceProvider
import com.amsterdam.repository.datasource.remote.CountryRemoteSource
import com.amsterdam.repository.dto.remote.RemoteCountryDto
import javax.inject.Inject

class CountryRemoteDataSourceImpl @Inject constructor(
    private val countryServiceProvider: CountryServiceProvider
) : CountryRemoteSource {
    override suspend fun getCountries(): List<RemoteCountryDto> {
        return countryServiceProvider.getCountries()
    }
}