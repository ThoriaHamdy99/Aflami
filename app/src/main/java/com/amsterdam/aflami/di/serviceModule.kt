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
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceProviderBindsModule {

    @Binds
    @Singleton
    abstract fun bindAuthServiceProvider(
        impl: AuthenticationServiceProviderImpl
    ): AuthenticationServiceProvider

    @Binds
    @Singleton
    abstract fun bindCategoryServiceProvider(
        impl: CategoryServiceProviderImpl
    ): CategoryServiceProvider

    @Binds
    @Singleton
    abstract fun bindCountryServiceProvider(
        impl: CountryServiceProviderImpl
    ): CountryServiceProvider

    @Binds
    @Singleton
    abstract fun bindMovieServiceProvider(
        impl: MovieServiceProviderImpl
    ): MovieServiceProvider

    @Binds
    @Singleton
    abstract fun bindTvShowsServiceProvider(
        impl: TvShowsServiceProviderImpl
    ): TvShowsServiceProvider
}

@Module
@InstallIn(SingletonComponent::class)
object ServiceProvidesModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideRetrofitClient(json: Json): RetrofitClient = RetrofitClient(json)

    @Provides
    @Singleton
    fun provideAuthenticationApiService(client: RetrofitClient): AuthenticationApiService =
        client.authenticationApiService()

    @Provides
    @Singleton
    fun provideMovieApiService(client: RetrofitClient): MovieApiService =
        client.movieApiService()

    @Provides
    @Singleton
    fun provideCategoryApiService(client: RetrofitClient): CategoryApiService =
        client.categoryApiService()

    @Provides
    @Singleton
    fun provideCountryApiService(client: RetrofitClient): CountryApiService =
        client.countryApiService()

    @Provides
    @Singleton
    fun provideTvShowsApiService(client: RetrofitClient): TvShowsApiService =
        client.tvApiService()
}
