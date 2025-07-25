package com.amsterdam.aflami.di

import com.amsterdam.remotedatasource.datasource.AuthenticationRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.CategoryRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.CountryRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.MovieRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.TvRemoteDataSourceImpl
import com.amsterdam.repository.datasource.remote.AuthenticationRemoteSource
import com.amsterdam.repository.datasource.remote.CategoryRemoteSource
import com.amsterdam.repository.datasource.remote.CountryRemoteSource
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.datasource.remote.TvShowsRemoteSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val remoteDataSourceModule = module {
    singleOf(::AuthenticationRemoteDataSourceImpl) bind AuthenticationRemoteSource::class
    singleOf(::CategoryRemoteDataSourceImpl) bind CategoryRemoteSource::class
    singleOf(::CountryRemoteDataSourceImpl) bind CountryRemoteSource::class
    singleOf(::MovieRemoteDataSourceImpl) bind MovieRemoteSource::class
    singleOf(::TvRemoteDataSourceImpl) bind TvShowsRemoteSource::class
}