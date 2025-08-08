package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.RemoteCountryDto
import retrofit2.http.GET

interface CountryApiService {

    @GET("configuration/countries")
    suspend fun getCountries(): List<RemoteCountryDto>
}