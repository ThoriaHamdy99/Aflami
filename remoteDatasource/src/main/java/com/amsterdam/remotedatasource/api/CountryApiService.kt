package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.RemoteCountryDto
import retrofit2.http.GET

interface CountryApiService {

    @GET(GET_COUNTRIES)
    suspend fun getCountries(): List<RemoteCountryDto>

    companion object {
        private const val GET_COUNTRIES = "configuration/countries"
    }
}