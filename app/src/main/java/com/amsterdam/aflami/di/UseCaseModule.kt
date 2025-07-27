package com.amsterdam.aflami.di

import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.authentication.LoginAsGuestUseCase
import com.amsterdam.domain.useCase.authentication.LoginWithPasswordUseCase
import com.amsterdam.domain.useCase.common.AddWatchHistoryUseCase
import com.amsterdam.domain.useCase.details.GetEpisodesBySeasonNumberUseCase
import com.amsterdam.domain.useCase.details.GetMovieCastUseCase
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase
import com.amsterdam.domain.useCase.home.GetContinueWatchingMoviesUseCase
import com.amsterdam.domain.useCase.home.GetHomeScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetMoviesByMoodUseCase
import com.amsterdam.domain.useCase.home.GetPopularMoviesUseCase
import com.amsterdam.domain.useCase.home.GetTopRatedMoviesUseCase
import com.amsterdam.domain.useCase.home.GetUpcomingMoviesUseCase
import com.amsterdam.domain.useCase.search.GetAndFilterMoviesByKeywordUseCase
import com.amsterdam.domain.useCase.search.GetAndFilterTvShowsByKeywordUseCase
import com.amsterdam.domain.useCase.search.GetMoviesByActorUseCase
import com.amsterdam.domain.useCase.search.GetMoviesByCountryUseCase
import com.amsterdam.domain.useCase.search.GetSuggestedCountriesUseCase
import com.amsterdam.domain.useCase.search.RecentSearchesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetsSessionType(repo: AuthenticationRepository): GetsSessionType =
        GetsSessionType(repo)

    @Provides
    fun provideLoginAsGuestUseCase(repo: AuthenticationRepository): LoginAsGuestUseCase =
        LoginAsGuestUseCase(repo)

    @Provides
    fun provideLoginWithPasswordUseCase(repo: AuthenticationRepository): LoginWithPasswordUseCase =
        LoginWithPasswordUseCase(repo)

    @Provides
    fun provideGetAndFilterMoviesByKeywordUseCase(repo: MovieRepository): GetAndFilterMoviesByKeywordUseCase =
        GetAndFilterMoviesByKeywordUseCase(repo)

    @Provides
    fun provideGetMoviesByCountryUseCase(repo: MovieRepository): GetMoviesByCountryUseCase =
        GetMoviesByCountryUseCase(repo)

    @Provides
    fun provideGetMovieCastUseCase(repo: MovieRepository): GetMovieCastUseCase =
        GetMovieCastUseCase(repo)

    @Provides
    fun provideGetMovieDetailsUseCase(repo: MovieRepository, addWatchHistoryUseCase: AddWatchHistoryUseCase): GetMovieDetailsUseCase =
        GetMovieDetailsUseCase(repo, addWatchHistoryUseCase)

    @Provides
    fun provideGetMoviesByActorUseCase(repo: MovieRepository): GetMoviesByActorUseCase =
        GetMoviesByActorUseCase(repo)

    @Provides
    fun provideGetSuggestedCountriesUseCase(repo: CountryRepository): GetSuggestedCountriesUseCase =
        GetSuggestedCountriesUseCase(repo)

    @Provides
    fun provideRecentSearchesUseCase(repo: RecentSearchRepository): RecentSearchesUseCase =
        RecentSearchesUseCase(repo)

    @Provides
    fun provideGetAndFilterTvShowsByKeywordUseCase(repo: TvShowRepository): GetAndFilterTvShowsByKeywordUseCase =
        GetAndFilterTvShowsByKeywordUseCase(repo)

    @Provides
    fun provideGetPopularMoviesUseCase(repo: MovieRepository): GetPopularMoviesUseCase =
        GetPopularMoviesUseCase(repo)

    @Provides
    fun provideGetEpisodesBySeasonNumberUseCase(repo: TvShowRepository): GetEpisodesBySeasonNumberUseCase =
        GetEpisodesBySeasonNumberUseCase(repo)

    @Provides
    fun provideGetTvShowDetailsUseCase(repo: TvShowRepository): GetTvShowDetailsUseCase =
        GetTvShowDetailsUseCase(repo)

    @Provides
    fun provideGetUpcomingMoviesUseCase(repo: MovieRepository): GetUpcomingMoviesUseCase =
        GetUpcomingMoviesUseCase(repo)

    @Provides
    fun provideGetTopRatedMoviesUseCase(repo: MovieRepository): GetTopRatedMoviesUseCase =
        GetTopRatedMoviesUseCase(repo)

    @Provides
    fun provideGetContinueWatchingMoviesUseCase(repo: WatchHistoryRepository): GetContinueWatchingMoviesUseCase =
        GetContinueWatchingMoviesUseCase(repo)

    @Provides
    fun provideGetHomeScreenDataUseCase(
        getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
        getPopularMoviesUseCase: GetPopularMoviesUseCase,
        getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
        getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase
    ): GetHomeScreenDataUseCase =
        GetHomeScreenDataUseCase(
            getTopRatedMoviesUseCase,
            getPopularMoviesUseCase,
            getUpcomingMoviesUseCase,
            getContinueWatchingMoviesUseCase
        )

    @Provides
    fun provideAddWatchHistoryUseCase(repo: WatchHistoryRepository): AddWatchHistoryUseCase =
        AddWatchHistoryUseCase(repo)

    @Provides
    fun provideGetMoviesByMoodUseCase(repo: MovieRepository): GetMoviesByMoodUseCase =
        GetMoviesByMoodUseCase(repo)
}