package com.amsterdam.aflami.di

import com.amsterdam.remotedatasource.api.AuthenticationApiService
import com.amsterdam.remotedatasource.api.CategoryApiService
import com.amsterdam.remotedatasource.api.CountryApiService
import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.remotedatasource.api.TvShowsApiService
import com.amsterdam.remotedatasource.client.RetrofitClient
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val serviceModule = module {
    single { Json { prettyPrint = true; isLenient = true; ignoreUnknownKeys = true } }
    singleOf(::RetrofitClient)

    single { get<RetrofitClient>().authenticationApiService() } bind AuthenticationApiService::class
    single { get<RetrofitClient>().movieApiService() } bind MovieApiService::class
    single { get<RetrofitClient>().categoryApiService() } bind CategoryApiService::class
    single { get<RetrofitClient>().countryApiService() } bind CountryApiService::class
    single { get<RetrofitClient>().tvApiService() } bind TvShowsApiService::class
}