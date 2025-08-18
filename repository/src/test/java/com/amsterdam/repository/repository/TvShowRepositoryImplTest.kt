package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.entity.Episode
import com.amsterdam.entity.Season
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.repository.datasource.local.AppLocalPreferences
import com.amsterdam.repository.datasource.local.CategoryLocalDataSource
import com.amsterdam.repository.datasource.local.TvShowLocalDataSource
import com.amsterdam.repository.datasource.remote.CategoryRemoteDataSource
import com.amsterdam.repository.datasource.remote.TvShowsRemoteDataSource
import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto
import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategories
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.EpisodeRemoteDto
import com.amsterdam.repository.dto.remote.TvShowRemoteResponse
import com.amsterdam.repository.dto.remote.VideoRemoteResponse
import com.amsterdam.repository.mapper.toEntity
import com.amsterdam.repository.mapper.toEntityList
import com.amsterdam.repository.mapper.toTvShowUserRateEntityList
import com.amsterdam.repository.utils.remoteCastDto
import com.amsterdam.repository.utils.remoteTvShowItemDto
import com.amsterdam.repository.utils.tvShowDetailsRemoteResponse
import com.amsterdam.repository.utils.videoDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.test.Test

class TvShowRepositoryImplTest {

    private val tvShowRemoteDataSource: TvShowsRemoteDataSource = mockk()
    private val tvShowLocalDataSource: TvShowLocalDataSource = mockk(relaxed = true)
    private val categoryLocalDataSource: CategoryLocalDataSource = mockk(relaxed = true)
    private val categoryRemoteDataSource: CategoryRemoteDataSource = mockk(relaxed = true)
    private val preferences: AppLocalPreferences = mockk()

    private val tvShowRepository: TvShowRepository by lazy {
        TvShowRepositoryImpl(
            localTvDataSource = tvShowLocalDataSource,
            remoteTvDataSource = tvShowRemoteDataSource,
            preferences = preferences,
            categoryLocalDataSource = categoryLocalDataSource,
            categoryRemoteDataSource = categoryRemoteDataSource
        )
    }

    init {
        mockkObject(Clock.System)
        every { Clock.System.now() } returns Instant.parse("2025-08-17T12:00:00Z")
        coEvery { preferences.getAppLanguage() } returns flowOf("en")
    }


    @Test
    fun `getTvShowByKeyword should return list of TvShow from remote`() = runTest {
        coEvery {
            tvShowRemoteDataSource.getTvShowsByKeyword(
                any(),
                any()
            )
        } returns remoteTvShowResponse

        val result = tvShowRepository.getTvShowByKeyword(keyword, page, tvShowsPerPage)

        assertThat(result).isEqualTo(expectedTvShows.toEntityList())
        coVerify(exactly = 1) { tvShowRemoteDataSource.getTvShowsByKeyword(keyword, page) }
    }

    @Test
    fun `getPopularTvShows should return data from remote when local is empty`() = runTest {
        coEvery { tvShowLocalDataSource.getPopularTvShows(any()) } returns emptyList()
        coEvery { tvShowRemoteDataSource.getPopularTvShows() } returns remoteTvShowResponse

        val result = tvShowRepository.getPopularTvShows()

        assertThat(result).isEqualTo(remoteTvShowResponse.results.toEntityList())
        coVerify(exactly = 1) { tvShowRemoteDataSource.getPopularTvShows() }
        coVerify(exactly = 1) { tvShowLocalDataSource.upsertPopularTvShows(any()) }
    }

    @Test
    fun `getPopularTvShows should return data from local when available`() = runTest {
        coEvery { tvShowLocalDataSource.getPopularTvShows(any()) } returns listOf(
            fakeTvShowWithCategories
        )

        val result = tvShowRepository.getPopularTvShows()

        assertThat(result).isEqualTo(listOf(fakeTvShowWithCategories).map { it.toEntity() })
        coVerify(exactly = 0) { tvShowRemoteDataSource.getPopularTvShows() }
    }

    @Test
    fun `getTopRatedTvShows should fetch from remote only when page is greater than 1`() = runTest {
        val pageGreaterThanOne = 2
        coEvery { tvShowRemoteDataSource.getTopRatedTvShows(pageGreaterThanOne) } returns remoteTvShowResponse

        val result = tvShowRepository.getTopRatedTvShows(pageGreaterThanOne)

        assertThat(result).isEqualTo(remoteTvShowResponse.results.toEntityList())
        coVerify(exactly = 1) { tvShowRemoteDataSource.getTopRatedTvShows(pageGreaterThanOne) }
        coVerify(exactly = 0) { tvShowLocalDataSource.getTopRatedTvShows(any()) }
    }

    @Test
    fun `getTvShowDetails should return details and trigger caching`() = runTest {
        coEvery { tvShowRemoteDataSource.getTvShowDetailsById(tvShowId) } returns tvShowDetailsRemoteResponse

        val result = tvShowRepository.getTvShowDetails(tvShowId)

        assertThat(result).isEqualTo(tvShowDetailsRemoteResponse.toEntity())
        val expectedNumberOfGenreCalls = tvShowDetailsRemoteResponse.genres.size
        coVerify(exactly = expectedNumberOfGenreCalls) {
            tvShowLocalDataSource.incrementGenreInterest(
                any()
            )
        }
        coVerify(exactly = 1) { tvShowLocalDataSource.upsertTvShow(any()) }
    }

    @Test
    fun `getEpisodesBySeasonNumber should return list of episodes`() = runTest {
        val episodeResponse = mockk<com.amsterdam.repository.dto.remote.EpisodeRemoteResponse>()
        coEvery { episodeResponse.episodes } returns listOf(episodeRemoteDto)
        coEvery {
            tvShowRemoteDataSource.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        } returns episodeResponse

        val result = tvShowRepository.getEpisodesBySeasonNumber(tvShowId, seasonNumber)

        assertThat(result).isEqualTo(listOf(expectedEpisode))
        coVerify(exactly = 1) {
            tvShowRemoteDataSource.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        }
    }

    @Test
    fun `getEpisodeVideoUrl should return empty string when no video is available`() = runTest {
        coEvery {
            tvShowRemoteDataSource.getEpisodeVideos(
                any(),
                any(),
                any()
            )
        } returns VideoRemoteResponse(results = emptyList())

        val result = tvShowRepository.getEpisodeVideoUrl(tvShowId, seasonNumber, episodeNumber)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getEpisodeVideoUrl should return episode videos url`() = runTest {
        coEvery {
            tvShowRemoteDataSource.getEpisodeVideos(
                tvShowId,
                seasonNumber,
                episodeNumber
            )
        } returns expectedVideoResponse

        val result = tvShowRepository.getEpisodeVideoUrl(tvShowId, seasonNumber, episodeNumber)

        assertThat(result).isEqualTo(episodeVideosUrl)
    }

    @Test
    fun `getTvShowCast should return list of Actor`() = runTest {
        coEvery { tvShowRemoteDataSource.getTvShowCast(tvShowId) } returns castResponse

        val result = tvShowRepository.getTvShowCast(tvShowId)

        assertThat(result).isEqualTo(expectedActors)
    }

    @Test
    fun `getTvShowSeasons should return mapped season list`() = runTest {
        coEvery { tvShowRemoteDataSource.getTvShowDetailsById(tvShowId) } returns tvShowDetailsRemoteResponse

        val result = tvShowRepository.getTvShowSeasons(tvShowId)

        assertThat(result).isEqualTo(expectedSeasons)
    }

    @Test
    fun `getTvShowsByGenreIds should return list of TvShow`() = runTest {
        coEvery {
            tvShowRemoteDataSource.getTvShowsByGenreId(
                any(),
                any()
            )
        } returns remoteTvShowResponse

        val result = tvShowRepository.getTvShowsByGenre(genre, page)

        assertThat(result).isEqualTo(remoteTvShowResponse.results.toEntityList())
    }

    @Test
    fun `getUserRatedTvShows should return user rated shows`() = runTest {
        coEvery { tvShowRemoteDataSource.getRatedTvShows() } returns userRatedTvShowResponse

        val result = tvShowRepository.getUserRatedTvShows()

        assertThat(result).isEqualTo(userRatedTvShowResponse.results.toTvShowUserRateEntityList())
        coVerify(exactly = 1) { tvShowRemoteDataSource.getRatedTvShows() }
    }

    @Test
    fun `setTvShowRate should call remote data source with correct parameters`() = runTest {
        val rate = 8
        coJustRun { tvShowRemoteDataSource.setTvShowRate(rate, tvShowId) }

        tvShowRepository.setTvShowRate(rate, tvShowId)

        coVerify(exactly = 1) { tvShowRemoteDataSource.setTvShowRate(rate, tvShowId) }
    }

    @Test
    fun `deleteTvShowRate should call remote data source with correct id`() = runTest {
        coJustRun { tvShowRemoteDataSource.deleteTvShowRate(tvShowId) }

        tvShowRepository.deleteTvShowRate(tvShowId)

        coVerify(exactly = 1) { tvShowRemoteDataSource.deleteTvShowRate(tvShowId) }
    }


    @Test
    fun `getTopRatedTvShows for page greater than 1 should return data from remote source only`() =
        runTest {
            coEvery { tvShowRemoteDataSource.getTopRatedTvShows(pageNumber) } returns remoteTvShowResponse

            val result = tvShowRepository.getTopRatedTvShows(pageNumber)

            assertThat(result).isEqualTo(remoteTvShowResponse.results.toEntityList())
            coVerify(exactly = 1) { tvShowRemoteDataSource.getTopRatedTvShows(pageNumber) }
            coVerify(exactly = 0) { tvShowLocalDataSource.getTopRatedTvShows(any()) }
        }

    @Test
    fun `getTopRatedTvShows for page 1 should return data from local cache when available`() =
        runTest {
            val fakeLocalData = listOf(fakeTvShowWithCategories.tvShow)
            coEvery { tvShowLocalDataSource.getTopRatedTvShows(any()) } returns fakeLocalData

            val result = tvShowRepository.getTopRatedTvShows(1)

            assertThat(result).isEqualTo(fakeLocalData.map { it.toEntity() })
            coVerify(exactly = 1) { tvShowLocalDataSource.getTopRatedTvShows("en") }
            coVerify(exactly = 0) { tvShowRemoteDataSource.getTopRatedTvShows(any()) }
        }

    @Test
    fun `getTopRatedTvShows for page 1 should return data from remote when local cache is empty`() =
        runTest {
            coEvery { tvShowLocalDataSource.getTopRatedTvShows(any()) } returns emptyList()
            coEvery { tvShowRemoteDataSource.getTopRatedTvShows(1) } returns remoteTvShowResponse

            val result = tvShowRepository.getTopRatedTvShows(1)

            assertThat(result).isEqualTo(remoteTvShowResponse.results.toEntityList())
            coVerify(exactly = 1) { tvShowRemoteDataSource.getTopRatedTvShows(1) }
            coVerify(exactly = 1) { tvShowLocalDataSource.upsertTopRatedTvShows(any()) }
        }

    private val keyword = "Breaking"
    private val page = 1
    private val tvShowsPerPage = 20
    private val expectedTvShows = listOf(remoteTvShowItemDto)
    private val tvShowId = 1L
    private val seasonNumber = 1
    private val episodeNumber = 1
    val pageNumber = 2


    private val episodeRemoteDto = EpisodeRemoteDto(
        id = 1,
        episodeNumber = 1,
        title = "Pilot",
        overview = "Episode 1 overview",
        runtime = "45",
        airDate = "2025-01-20",
        stillPath = "/still.jpg",
        voteAverage = 8.5,
        seasonNumber = 1
    )
    private val expectedEpisode = Episode(
        id = 1,
        title = "Pilot",
        episodeNumber = 1,
        description = "Episode 1 overview",
        episodeImageUrl = "https://image.tmdb.org/t/p/w500/still.jpg",
        rating = 8.5f,
        airDate = LocalDate.parse("2025-01-20"),
        seasonNumber = 1,
        runTimeInMinutes = 45,
        videoUrl = ""
    )

    private val expectedVideoResponse = VideoRemoteResponse(results = listOf(videoDto))
    private val episodeVideosUrl = "https://www.youtube.com/watch?v=someKey"

    private val remoteCastList = listOf(remoteCastDto)
    private val castResponse = CastAndCrewRemoteResponse(cast = remoteCastList)
    private val expectedActors = remoteCastList.toEntityList()

    private val expectedSeasons = listOf(
        Season(id = 1, title = "Season 1", episodeCount = 10, seasonNumber = 1),
        Season(id = 2, title = "Season 2", episodeCount = 12, seasonNumber = 2)
    )

    private val remoteTvShowResponse = TvShowRemoteResponse(
        page = 1,
        results = expectedTvShows,
        totalPages = 1,
        totalResults = 1
    )

    private val userRatedTvShowResponse = mockk<TvShowRemoteResponse>(relaxed = true)

    private val genre = TvShowGenre.COMEDY

    private val fakeTvShowWithCategories = TvShowWithCategories(
        tvShow = TvShowLocalDto(
            tvShowId = 1L,
            storedLanguage = "en",
            name = "Fake Show From DB",
            description = "This is a fake overview from the local database.",
            poster = "/fake_poster.jpg",
            airDate = LocalDate.parse("2025-01-01"),
            rating = 8.5f,
            popularity = 99.9,
            seasonCount = 5,
            originCountry = "US"
        ),
        categories = listOf(
            TvShowCategoryLocalDto(categoryId = 1)
        )
    )
}