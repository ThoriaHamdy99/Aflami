package com.amsterdam.remotedatasource.serviceProvider.implementation

import com.amsterdam.remotedatasource.api.CountryApiService
import com.amsterdam.remotedatasource.serviceProvider.CountryServiceProvider
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.dto.remote.RemoteCountryDto
import javax.inject.Inject

class CountryServiceProviderImpl @Inject constructor(
    private val countryApiService: CountryApiService
) : CountryServiceProvider {
    override suspend fun getCountries(): List<RemoteCountryDto> {
        return responseCall { countryApiService.getCountries() }
    }
}