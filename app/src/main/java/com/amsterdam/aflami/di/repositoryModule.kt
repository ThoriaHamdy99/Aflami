package com.amsterdam.aflami.di

import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.repository.CategoryRepository
import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.repository.repository.AuthenticationRepositoryImpl
import com.amsterdam.repository.repository.CategoryRepositoryImpl
import com.amsterdam.repository.repository.CountryRepositoryImpl
import com.amsterdam.repository.repository.MovieRepositoryImpl
import com.amsterdam.repository.repository.RecentSearchRepositoryImpl
import com.amsterdam.repository.repository.TvShowRepositoryImpl
import com.amsterdam.repository.repository.WatchHistoryRepositoryImpl
import com.amsterdam.repository.security.CryptoData
import com.amsterdam.repository.security.CryptoDataImpl
import com.amsterdam.repository.utils.RecentSearchHandler
import com.amsterdam.repository.utils.RecentSearchHandlerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    // crypto
    singleOf(::CryptoDataImpl) bind CryptoData::class

    // Handler
    singleOf(::RecentSearchHandlerImpl) bind RecentSearchHandler::class

    singleOf(::AuthenticationRepositoryImpl) bind AuthenticationRepository::class
    singleOf(::CountryRepositoryImpl) bind CountryRepository::class
    singleOf(::CategoryRepositoryImpl) bind CategoryRepository::class
    singleOf(::MovieRepositoryImpl) bind MovieRepository::class
    singleOf(::RecentSearchRepositoryImpl) bind RecentSearchRepository::class
    singleOf(::TvShowRepositoryImpl) bind TvShowRepository::class
    singleOf(::WatchHistoryRepositoryImpl) bind WatchHistoryRepository::class

}