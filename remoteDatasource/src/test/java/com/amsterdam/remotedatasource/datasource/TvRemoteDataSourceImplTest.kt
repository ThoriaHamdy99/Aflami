package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.TvShowsApiService
import com.amsterdam.remotedatasource.util.TV_SHOW_TEST_EPISODE_NUMBER
import com.amsterdam.remotedatasource.util.TV_SHOW_TEST_GENRE_ID
import com.amsterdam.remotedatasource.util.TV_SHOW_TEST_ID
import com.amsterdam.remotedatasource.util.TV_SHOW_TEST_KEYWORD
import com.amsterdam.remotedatasource.util.TV_SHOW_TEST_PAGE
import com.amsterdam.remotedatasource.util.TV_SHOW_TEST_RATING
import com.amsterdam.remotedatasource.util.TV_SHOW_TEST_SEASON_NUMBER
import com.amsterdam.remotedatasource.util.episodeRemoteResponse
import com.amsterdam.remotedatasource.util.tvShowCastAndCrewRemoteResponse
import com.amsterdam.remotedatasource.util.tvShowDetailsRemoteResponse
import com.amsterdam.remotedatasource.util.tvShowItemRemoteDto
import com.amsterdam.remotedatasource.util.tvShowRatingRemoteResponse
import com.amsterdam.remotedatasource.util.tvShowVideoRemoteResponse
import com.amsterdam.repository.dto.remote.TvShowRemoteResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TvRemoteDataSourceImplTest {

    private val tvShowsApiService: TvShowsApiService = mockk()
    private val tvRemoteDataSourceImpl: TvRemoteDataSourceImpl =
        TvRemoteDataSourceImpl(tvShowsApiService)

    @Test
    fun `getPopularTvShows should return a list of TV shows when the API call is successful`() =
        runTest {
            val expectedResponse = TvShowRemoteResponse(
                page = TV_SHOW_TEST_PAGE,
                results = listOf(tvShowItemRemoteDto),
                totalPages = 1,
                totalResults = 1
            )
            coEvery { tvShowsApiService.getPopularTvShows() } returns expectedResponse

            val popularTvShows = tvRemoteDataSourceImpl.getPopularTvShows()

            assertThat(popularTvShows).isEqualTo(expectedResponse)
            coVerify(exactly = 1) { tvShowsApiService.getPopularTvShows() }
        }

    @Test
    fun `getPopularTvShows should rethrow a NetworkException when the service provider throws one`() =
        runTest {
            coEvery { tvShowsApiService.getPopularTvShows() } throws NetworkException()

            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getPopularTvShows()
            }
            coVerify(exactly = 1) { tvShowsApiService.getPopularTvShows() }
        }

    @Test
    fun `getPopularTvShows should return an empty list when the API response is empty`() = runTest {
        val expectedResponse = TvShowRemoteResponse(
            page = 1,
            results = emptyList(),
            totalPages = 1,
            totalResults = 0
        )
        coEvery { tvShowsApiService.getPopularTvShows() } returns expectedResponse

        val popularTvShows = tvRemoteDataSourceImpl.getPopularTvShows()

        assertThat(popularTvShows.results).isEmpty()
        coVerify(exactly = 1) { tvShowsApiService.getPopularTvShows() }
    }


    @Test
    fun `getTopRatedTvShows should return a list of top rated TV shows when the API call is successful`() =
        runTest {
            val expectedResponse = TvShowRemoteResponse(
                page = TV_SHOW_TEST_PAGE,
                results = listOf(tvShowItemRemoteDto),
                totalPages = 1,
                totalResults = 1
            )
            coEvery { tvShowsApiService.getTopRatedTvShows(TV_SHOW_TEST_PAGE) } returns expectedResponse

            val topRatedTvShows = tvRemoteDataSourceImpl.getTopRatedTvShows(TV_SHOW_TEST_PAGE)

            assertThat(topRatedTvShows).isEqualTo(expectedResponse)
            coVerify(exactly = 1) { tvShowsApiService.getTopRatedTvShows(TV_SHOW_TEST_PAGE) }
        }

    @Test
    fun `getTopRatedTvShows should rethrow a NetworkException when the service provider throws one`() =
        runTest {
            coEvery { tvShowsApiService.getTopRatedTvShows(TV_SHOW_TEST_PAGE) } throws NetworkException()

            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getTopRatedTvShows(TV_SHOW_TEST_PAGE)
            }
            coVerify(exactly = 1) { tvShowsApiService.getTopRatedTvShows(TV_SHOW_TEST_PAGE) }
        }


    @Test
    fun `getTvShowsByKeyword should return a list of TV shows when the API call is successful`() =
        runTest {
            val expectedResponse = TvShowRemoteResponse(
                page = TV_SHOW_TEST_PAGE,
                results = listOf(tvShowItemRemoteDto),
                totalPages = 1,
                totalResults = 1
            )
            coEvery {
                tvShowsApiService.getTvShowsByKeyword(
                    TV_SHOW_TEST_KEYWORD,
                    TV_SHOW_TEST_PAGE
                )
            } returns expectedResponse

            val tvShows =
                tvRemoteDataSourceImpl.getTvShowsByKeyword(TV_SHOW_TEST_KEYWORD, TV_SHOW_TEST_PAGE)

            assertThat(tvShows).isEqualTo(expectedResponse)
            coVerify(exactly = 1) {
                tvShowsApiService.getTvShowsByKeyword(
                    TV_SHOW_TEST_KEYWORD,
                    TV_SHOW_TEST_PAGE
                )
            }
        }

    @Test
    fun `getTvShowsByKeyword should rethrow a NetworkException when the service provider throws one`() =
        runTest {
            coEvery {
                tvShowsApiService.getTvShowsByKeyword(
                    TV_SHOW_TEST_KEYWORD,
                    TV_SHOW_TEST_PAGE
                )
            } throws NetworkException()

            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getTvShowsByKeyword(TV_SHOW_TEST_KEYWORD, TV_SHOW_TEST_PAGE)
            }
            coVerify(exactly = 1) {
                tvShowsApiService.getTvShowsByKeyword(
                    TV_SHOW_TEST_KEYWORD,
                    TV_SHOW_TEST_PAGE
                )
            }
        }


    @Test
    fun `getTvShowDetailsById should return detailed TV show information when the API call is successful`() =
        runTest {
            coEvery { tvShowsApiService.getTvShowDetailsById(TV_SHOW_TEST_ID) } returns tvShowDetailsRemoteResponse

            val details = tvRemoteDataSourceImpl.getTvShowDetailsById(TV_SHOW_TEST_ID)

            assertThat(details).isEqualTo(tvShowDetailsRemoteResponse)
            coVerify(exactly = 1) { tvShowsApiService.getTvShowDetailsById(TV_SHOW_TEST_ID) }
        }

    @Test
    fun `getTvShowDetailsById should rethrow a NetworkException when the service provider throws one`() =
        runTest {
            coEvery { tvShowsApiService.getTvShowDetailsById(TV_SHOW_TEST_ID) } throws NetworkException()

            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getTvShowDetailsById(TV_SHOW_TEST_ID)
            }
            coVerify(exactly = 1) { tvShowsApiService.getTvShowDetailsById(TV_SHOW_TEST_ID) }
        }

    @Test
    fun `getTvShowCast should return cast and crew for a TV show when the API call is successful`() =
        runTest {
            coEvery { tvShowsApiService.getTvShowCast(TV_SHOW_TEST_ID) } returns tvShowCastAndCrewRemoteResponse

            val castAndCrew = tvRemoteDataSourceImpl.getTvShowCast(TV_SHOW_TEST_ID)

            assertThat(castAndCrew).isEqualTo(tvShowCastAndCrewRemoteResponse)
            coVerify(exactly = 1) { tvShowsApiService.getTvShowCast(TV_SHOW_TEST_ID) }
        }

    @Test
    fun `getTvShowCast should rethrow a NetworkException when the service provider throws one`() =
        runTest {
            coEvery { tvShowsApiService.getTvShowCast(TV_SHOW_TEST_ID) } throws NetworkException()

            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getTvShowCast(TV_SHOW_TEST_ID)
            }
            coVerify(exactly = 1) { tvShowsApiService.getTvShowCast(TV_SHOW_TEST_ID) }
        }

    @Test
    fun `getEpisodesBySeasonNumber should return a list of episodes when the API call is successful`() =
        runTest {
            coEvery {
                tvShowsApiService.getEpisodesBySeasonNumber(
                    TV_SHOW_TEST_ID,
                    TV_SHOW_TEST_SEASON_NUMBER
                )
            } returns episodeRemoteResponse

            val episodes = tvRemoteDataSourceImpl.getEpisodesBySeasonNumber(
                TV_SHOW_TEST_ID,
                TV_SHOW_TEST_SEASON_NUMBER
            )

            assertThat(episodes).isEqualTo(episodeRemoteResponse)
            coVerify(exactly = 1) {
                tvShowsApiService.getEpisodesBySeasonNumber(
                    TV_SHOW_TEST_ID,
                    TV_SHOW_TEST_SEASON_NUMBER
                )
            }
        }

    @Test
    fun `getEpisodesBySeasonNumber should rethrow a NetworkException when the service provider throws one`() =
        runTest {
            coEvery {
                tvShowsApiService.getEpisodesBySeasonNumber(
                    TV_SHOW_TEST_ID,
                    TV_SHOW_TEST_SEASON_NUMBER
                )
            } throws NetworkException()

            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getEpisodesBySeasonNumber(
                    TV_SHOW_TEST_ID,
                    TV_SHOW_TEST_SEASON_NUMBER
                )
            }
            coVerify(exactly = 1) {
                tvShowsApiService.getEpisodesBySeasonNumber(
                    TV_SHOW_TEST_ID,
                    TV_SHOW_TEST_SEASON_NUMBER
                )
            }
        }

    @Test
    fun `getEpisodeVideos should return a VideoResponse object when the API call is successful`() =
        runTest {
            coEvery {
                tvShowsApiService.getEpisodeVideos(
                    TV_SHOW_TEST_ID,
                    TV_SHOW_TEST_SEASON_NUMBER,
                    TV_SHOW_TEST_EPISODE_NUMBER
                )
            } returns tvShowVideoRemoteResponse

            val videoResponse = tvRemoteDataSourceImpl.getEpisodeVideos(
                TV_SHOW_TEST_ID,
                TV_SHOW_TEST_SEASON_NUMBER,
                TV_SHOW_TEST_EPISODE_NUMBER
            )

            assertThat(videoResponse).isEqualTo(tvShowVideoRemoteResponse)
            coVerify(exactly = 1) {
                tvShowsApiService.getEpisodeVideos(
                    TV_SHOW_TEST_ID,
                    TV_SHOW_TEST_SEASON_NUMBER,
                    TV_SHOW_TEST_EPISODE_NUMBER
                )
            }
        }

    @Test
    fun `getEpisodeVideos should return an empty list when the API response is empty`() = runTest {
        val expectedResponse = TvShowRemoteResponse(
            page = 1,
            results = emptyList(),
            totalPages = 1,
            totalResults = 0
        )
        coEvery {
            tvShowsApiService.getEpisodeVideos(
                TV_SHOW_TEST_ID,
                TV_SHOW_TEST_SEASON_NUMBER,
                TV_SHOW_TEST_EPISODE_NUMBER
            )
        } returns tvShowVideoRemoteResponse.copy(results = emptyList())

        val videoResponse = tvRemoteDataSourceImpl.getEpisodeVideos(
            TV_SHOW_TEST_ID,
            TV_SHOW_TEST_SEASON_NUMBER,
            TV_SHOW_TEST_EPISODE_NUMBER
        )

        assertThat(videoResponse.results).isEmpty()
        coVerify(exactly = 1) {
            tvShowsApiService.getEpisodeVideos(
                TV_SHOW_TEST_ID,
                TV_SHOW_TEST_SEASON_NUMBER,
                TV_SHOW_TEST_EPISODE_NUMBER
            )
        }
    }

    @Test
    fun `setTvShowRate should return a RatingResponse when the API call is successful`() = runTest {
        val rate = TV_SHOW_TEST_RATING.toInt()

        coEvery {
            tvShowsApiService.postTvRating(
                TV_SHOW_TEST_ID,
                rate.toFloat(),
            )
        } returns tvShowRatingRemoteResponse

        val result = tvRemoteDataSourceImpl.setTvShowRate(
            rate = rate,
            tvShowId = TV_SHOW_TEST_ID,
        )

        assertThat(result).isEqualTo(tvShowRatingRemoteResponse)
        coVerify(exactly = 1) {
            tvShowsApiService.postTvRating(TV_SHOW_TEST_ID, rate.toFloat())
        }
    }


    @Test
    fun `getRatedTvShows should return rated TV shows when the API call is successful`() = runTest {
        val expectedResponse = TvShowRemoteResponse(
            page = TV_SHOW_TEST_PAGE,
            results = listOf(tvShowItemRemoteDto),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { tvShowsApiService.getRatedTvShows() } returns expectedResponse

        val ratingResponse = tvRemoteDataSourceImpl.getRatedTvShows()

        assertThat(ratingResponse).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { tvShowsApiService.getRatedTvShows() }
    }

    @Test
    fun `getRatedTvShows should throw NetworkException when the API call fails`() = runTest {
        coEvery { tvShowsApiService.getRatedTvShows() } throws NetworkException()

        assertThrows<NetworkException> {
            tvRemoteDataSourceImpl.getRatedTvShows()
        }
        coVerify(exactly = 1) { tvShowsApiService.getRatedTvShows() }
    }


    @Test
    fun `deleteTvShowRate should call the deleteTvRating API with the correct parameters`() =
        runTest {
            coEvery {
                tvShowsApiService.deleteTvRating(tvId = TV_SHOW_TEST_ID)
            } returns tvShowRatingRemoteResponse

            tvRemoteDataSourceImpl.deleteTvShowRate(TV_SHOW_TEST_ID)

            coVerify(exactly = 1) {
                tvShowsApiService.deleteTvRating(tvId = TV_SHOW_TEST_ID)
            }
        }

    @Test
    fun `getTvShowsByGenreId should return a list of TV shows when the API call is successful`() =
        runTest {
            val expectedResponse = TvShowRemoteResponse(
                page = TV_SHOW_TEST_PAGE,
                results = listOf(tvShowItemRemoteDto),
                totalPages = 1,
                totalResults = 1
            )
            val genreList = listOf(TV_SHOW_TEST_GENRE_ID)
            coEvery {
                tvShowsApiService.getTvShowsByGenreIds(
                    genreList,
                    TV_SHOW_TEST_PAGE
                )
            } returns expectedResponse

            val tvShows =
                tvRemoteDataSourceImpl.getTvShowsByGenreId(TV_SHOW_TEST_GENRE_ID, TV_SHOW_TEST_PAGE)

            assertThat(tvShows).isEqualTo(expectedResponse)
            coVerify(exactly = 1) {
                tvShowsApiService.getTvShowsByGenreIds(
                    genreList,
                    TV_SHOW_TEST_PAGE
                )
            }
        }

    @Test
    fun `getEpisodeVideos should use season 1 if a season number less than or equal to 0 is provided`() =
        runTest {
            val invalidSeasonNumber = 0
            val expectedSeasonNumber = 1

            coEvery {
                tvShowsApiService.getEpisodeVideos(
                    TV_SHOW_TEST_ID,
                    expectedSeasonNumber,
                    TV_SHOW_TEST_EPISODE_NUMBER
                )
            } returns tvShowVideoRemoteResponse

            val videoResponse = tvRemoteDataSourceImpl.getEpisodeVideos(
                TV_SHOW_TEST_ID,
                invalidSeasonNumber,
                TV_SHOW_TEST_EPISODE_NUMBER
            )

            assertThat(videoResponse).isEqualTo(tvShowVideoRemoteResponse)

            coVerify(exactly = 1) {
                tvShowsApiService.getEpisodeVideos(
                    TV_SHOW_TEST_ID,
                    expectedSeasonNumber,
                    TV_SHOW_TEST_EPISODE_NUMBER
                )
            }
        }
}