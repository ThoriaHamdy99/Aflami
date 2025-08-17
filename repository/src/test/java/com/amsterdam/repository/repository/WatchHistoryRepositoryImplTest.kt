package com.amsterdam.repository.repository

import com.amsterdam.repository.datasource.local.AppLocalPreferences
import com.amsterdam.repository.datasource.local.MovieLocalDataSource
import com.amsterdam.repository.datasource.local.TvShowLocalDataSource
import com.amsterdam.repository.datasource.local.WatchHistoryLocalDataSource
import com.amsterdam.repository.datasource.remote.MovieRemoteDataSource
import com.amsterdam.repository.datasource.remote.TvShowsRemoteDataSource
import com.amsterdam.repository.dto.local.MovieLocalDto
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import com.amsterdam.repository.dto.remote.MovieDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.mapper.toLocalDto
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WatchHistoryRepositoryImplTest {

    private val watchHistoryLocalDataSource: WatchHistoryLocalDataSource = mockk()
    private val movieLocalDataSource: MovieLocalDataSource = mockk()
    private val movieRemoteDataSource: MovieRemoteDataSource = mockk()
    private val tvShowLocalDataSource: TvShowLocalDataSource = mockk()
    private val tvShowRemoteSource: TvShowsRemoteDataSource = mockk()
    private val preferences: AppLocalPreferences = mockk()

    private lateinit var repository: WatchHistoryRepositoryImpl

    @BeforeEach
    fun setup() {
        clearAllMocks()
        repository = WatchHistoryRepositoryImpl(
            watchHistoryLocalDataSource = watchHistoryLocalDataSource,
            movieLocalDataSource = movieLocalDataSource,
            movieRemoteDataSource = movieRemoteDataSource,
            tvShowLocalDataSource = tvShowLocalDataSource,
            tvShowRemoteSource = tvShowRemoteSource,
            preferences = preferences,
            localTvDataSource = tvShowLocalDataSource
        )
        coEvery { preferences.getAppLanguage() } returns flowOf(language)
    }
    @Test
    fun `addMovieToWatchHistory should call local data source to upsert movie`() = runTest {
        coEvery { watchHistoryLocalDataSource.upsertMovieToWatchHistory(any()) } just Runs

        repository.addMovieToWatchHistory(movieId)

        coVerify(exactly = 1) {
            watchHistoryLocalDataSource.upsertMovieToWatchHistory(
                withArg { movieDto ->
                    assertThat(movieDto.movieId).isEqualTo(movieId)
                }
            )
        }
    }

    @Test
    fun `getContinueWatchingMovies should return movies from local cache when available`() =
        runTest {
            coEvery {
                watchHistoryLocalDataSource.getMoviesWatchHistory(
                    page,
                    pageSize
                )
            } returns flowOf(listOf(movieWatchHistoryDto))
            coEvery {
                movieLocalDataSource.getMovieById(
                    movieId,
                    language
                )
            } returns fakeMovieLocalDto

            val result = repository.getContinueWatchingMovies(page, pageSize).first()

            assertThat(result.first().movie.id).isEqualTo(movieId)
            assertThat(result.first().movie.name).isEqualTo(fakeMovieLocalDto.name)
            coVerify(exactly = 0) { movieRemoteDataSource.getMovieDetailsById(any()) }
        }

    @Test
    fun `getContinueWatchingTvShows should return tv shows from local cache when available`() =
        runTest {
            coEvery {
                watchHistoryLocalDataSource.getTvShowsWatchHistory(
                    page,
                    pageSize
                )
            } returns flowOf(listOf(tvShowWatchHistoryDto))
            coEvery {
                tvShowLocalDataSource.getTvShowById(
                    tvShowId,
                    language
                )
            } returns fakeTvShowLocalDto

            val result = repository.getContinueWatchingTvShows(page, pageSize).first()

            assertThat(result.first().tvShow.id).isEqualTo(tvShowId)
            assertThat(result.first().tvShow.name).isEqualTo(fakeTvShowLocalDto.name)
            coVerify(exactly = 0) { tvShowRemoteSource.getTvShowDetailsById(any()) }
        }

    @Test
    fun `addTvShowToWatchHistory should call local data source to upsert tv show`() = runTest {
        coEvery { watchHistoryLocalDataSource.upsertTvShowToWatchHistory(any()) } just Runs

        repository.addTvShowToWatchHistory(tvShowId)

        coVerify(exactly = 1) {
            watchHistoryLocalDataSource.upsertTvShowToWatchHistory(
                withArg { tvShowDto ->
                    assertThat(tvShowDto.tvShowId).isEqualTo(tvShowId)
                }
            )
        }
    }


    private val language = "en"
    private val movieId = 101L
    private val tvShowId = 202L
    private val page = 1
    private val pageSize = 10

    private val movieWatchHistoryDto = MovieWatchHistoryDto(movieId = movieId)
    private val tvShowWatchHistoryDto = TvShowWatchHistoryDto(tvShowId = tvShowId)

    private val fakeMovieLocalDto = MovieLocalDto(
        movieId = movieId,
        storedLanguage = language,
        name = "Local Movie",
        description = "A movie from the local database.",
        poster = "/local.jpg",
        releaseDate = LocalDate.parse("2025-01-01"),
        popularity = 150.5,
        rating = 8.0f,
        originCountry = "US",
        movieLength = 120,
        isAdult = false
    )

    private val fakeTvShowLocalDto = TvShowLocalDto(
        tvShowId = tvShowId,
        storedLanguage = language,
        name = "Local TV Show",
        description = "...",
        poster = "/local.jpg",
        airDate = null,
        rating = 7.0f,
        popularity = 100.0,
        seasonCount = 2,
        originCountry = "US"
    )
}