package com.amsterdam.aflami.di

import com.amsterdam.remotedatasource.api.AuthenticationApiService
import com.amsterdam.remotedatasource.api.CategoryApiService
import com.amsterdam.remotedatasource.api.CountryApiService
import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.remotedatasource.api.TvShowsApiService
import com.amsterdam.remotedatasource.client.RetrofitClient
import com.amsterdam.remotedatasource.serviceProvider.AuthenticationServiceProvider
import com.amsterdam.remotedatasource.serviceProvider.CategoryServiceProvider
import com.amsterdam.remotedatasource.serviceProvider.CountryServiceProvider
import com.amsterdam.remotedatasource.serviceProvider.MovieServiceProvider
import com.amsterdam.remotedatasource.serviceProvider.TvShowsServiceProvider
import com.amsterdam.remotedatasource.serviceProvider.implementation.AuthenticationServiceProviderImpl
import com.amsterdam.remotedatasource.serviceProvider.implementation.CategoryServiceProviderImpl
import com.amsterdam.remotedatasource.serviceProvider.implementation.CountryServiceProviderImpl
import com.amsterdam.remotedatasource.serviceProvider.implementation.MovieServiceProviderImpl
import com.amsterdam.remotedatasource.serviceProvider.implementation.TvShowsServiceProviderImpl
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

    singleOf(::AuthenticationServiceProviderImpl) bind AuthenticationServiceProvider::class
    singleOf(::CategoryServiceProviderImpl) bind CategoryServiceProvider::class
    singleOf(::CountryServiceProviderImpl) bind CountryServiceProvider::class
    singleOf(::MovieServiceProviderImpl) bind MovieServiceProvider::class
    singleOf(::TvShowsServiceProviderImpl) bind TvShowsServiceProvider::class
}