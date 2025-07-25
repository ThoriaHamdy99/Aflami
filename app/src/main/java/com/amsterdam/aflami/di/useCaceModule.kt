package com.amsterdam.aflami.di

import com.amsterdam.domain.useCase.AddWatchHistoryUseCase
import com.amsterdam.domain.useCase.GetAndFilterMoviesByKeywordUseCase
import com.amsterdam.domain.useCase.GetAndFilterTvShowsByKeywordUseCase
import com.amsterdam.domain.useCase.GetContinueWatchingMoviesUseCase
import com.amsterdam.domain.useCase.GetEpisodesBySeasonNumberUseCase
import com.amsterdam.domain.useCase.GetHomeScreenDataUseCase
import com.amsterdam.domain.useCase.GetMovieCastUseCase
import com.amsterdam.domain.useCase.GetMovieDetailsUseCase
import com.amsterdam.domain.useCase.GetMoviesByActorUseCase
import com.amsterdam.domain.useCase.GetMoviesByCountryUseCase
import com.amsterdam.domain.useCase.GetMoviesByMoodUseCase
import com.amsterdam.domain.useCase.GetPopularMoviesUseCase
import com.amsterdam.domain.useCase.GetSuggestedCountriesUseCase
import com.amsterdam.domain.useCase.GetTopRatedMoviesUseCase
import com.amsterdam.domain.useCase.GetTvShowDetailsUseCase
import com.amsterdam.domain.useCase.GetUpcomingMoviesUseCase
import com.amsterdam.domain.useCase.RecentSearchesUseCase
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.authentication.LoginAsGuestUseCase
import com.amsterdam.domain.useCase.authentication.LoginWithPasswordUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetsSessionType)
    singleOf(::LoginAsGuestUseCase)
    singleOf(::LoginWithPasswordUseCase)
    singleOf(::GetAndFilterMoviesByKeywordUseCase)
    singleOf(::GetMoviesByCountryUseCase)
    singleOf(::GetMovieCastUseCase)
    singleOf(::GetMovieDetailsUseCase)
    singleOf(::GetMoviesByActorUseCase)
    singleOf(::GetSuggestedCountriesUseCase)
    singleOf(::RecentSearchesUseCase)
    singleOf(::GetAndFilterTvShowsByKeywordUseCase)
    singleOf(::GetPopularMoviesUseCase)
    singleOf(::GetEpisodesBySeasonNumberUseCase)
    singleOf(::GetTvShowDetailsUseCase)
    singleOf(::GetUpcomingMoviesUseCase)
    singleOf(::GetTopRatedMoviesUseCase)
    singleOf(::GetContinueWatchingMoviesUseCase)
    singleOf(::GetHomeScreenDataUseCase)
    singleOf(::AddWatchHistoryUseCase)
    singleOf(::GetMoviesByMoodUseCase)
}