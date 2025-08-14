package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.remotedatasource.util.actorSearchItemDto
import com.amsterdam.remotedatasource.util.remoteMovieDetailsResponse
import com.amsterdam.remotedatasource.util.remoteMovieItemDto
import com.amsterdam.repository.dto.remote.RatingResponse
import com.amsterdam.repository.dto.remote.RemoteActorSearchResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MovieRemoteDataSourceImplTest {

    private lateinit var movieApiService: MovieApiService
    private lateinit var movieRemoteDataSourceImpl: MovieRemoteDataDataSourceImpl

    @BeforeEach
    fun setUp() {
        movieApiService = mockk()
        movieRemoteDataSourceImpl = MovieRemoteDataDataSourceImpl(movieApiService)
    }

    @Test
    fun `getMoviesByKeyword should return a list of movies when successful`() = runTest {

        coEvery { movieApiService.getMoviesByKeyword(keyword, page) } returns expectedResponse

        val movies = movieRemoteDataSourceImpl.getMoviesByKeyword(keyword, page)

        assertThat(movies).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.getMoviesByKeyword(keyword, page) }
    }

    @Test
    fun `getMoviesByKeyword should throw NetworkException when api call fails`() = runTest {

        coEvery { movieApiService.getMoviesByKeyword(keyword, page) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMoviesByKeyword(keyword, page)
        }
    }

    @Test
    fun `getMoviesByActorIds should return movies for a given actor when successful`() = runTest {

        coEvery { movieApiService.getMoviesByActorId("6193") } returns expectedResponse

        val movies = movieRemoteDataSourceImpl.getMoviesByActorIds(actorIds, 1)

        assertThat(movies).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.getMoviesByActorId("6193") }
    }

    @Test
    fun `getMoviesByActorIds should throw NetworkException when api call fails`() = runTest {

        coEvery { movieApiService.getMoviesByActorId("6193") } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMoviesByActorIds(actorIds, page)
        }
    }

    @Test
    fun `getActorIdsByName should return actor IDs when successful`() = runTest {

        coEvery { movieApiService.getActorIdByName(name, page) } returns expectedActorSearchResponse

        val actorIds = movieRemoteDataSourceImpl.getActorIdsByName(name, page)

        assertThat(actorIds).containsExactly(6193)
        coVerify(exactly = 1) { movieApiService.getActorIdByName(name, page) }
    }

    @Test
    fun `getActorIdsByName should return an empty list when no actors are found`() = runTest {

        coEvery { movieApiService.getActorIdByName(name, page) } returns expectedEmptyActorSearchResponse

        val actorIds = movieRemoteDataSourceImpl.getActorIdsByName(name, page)

        assertThat(actorIds).isEmpty()
    }

    @Test
    fun `getActorIdsByName should throw NetworkException when api call fails`() = runTest {

        coEvery { movieApiService.getActorIdByName(name, page) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getActorIdsByName(name, page)
        }
    }

    @Test
    fun `getMoviesByCountryIsoCode should return movies from a specific country when successful`() =
        runTest {
            coEvery {
                movieApiService.getMoviesByCountryIsoCode(
                    countryIsoCode,
                    page
                )
            } returns expectedResponse

            val movies = movieRemoteDataSourceImpl.getMoviesByCountryIsoCode(countryIsoCode, page)

            assertThat(movies).isEqualTo(expectedResponse)
            coVerify(exactly = 1) {
                movieApiService.getMoviesByCountryIsoCode(
                    countryIsoCode,
                    page
                )
            }
        }

    @Test
    fun `getMoviesByCountryIsoCode should throw NetworkException when api call fails`() = runTest {
        coEvery {
            movieApiService.getMoviesByCountryIsoCode(
                countryIsoCode,
                page
            )
        } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMoviesByCountryIsoCode(countryIsoCode, page)
        }
    }

    @Test
    fun `getCastByMovieId should throw NetworkException when api call fails`() = runTest {

        coEvery { movieApiService.getCastByMovieId(movieId) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getCastByMovieId(movieId)
        }
    }

    @Test
    fun `getMovieDetailsById should return movie details including nested data when successful`() =
        runTest {
            val expectedResponse = remoteMovieDetailsResponse
            coEvery { movieApiService.getMovieDetailsById(movieId) } returns expectedResponse

            val details = movieRemoteDataSourceImpl.getMovieDetailsById(movieId)

            assertThat(details).isEqualTo(expectedResponse)
            coVerify(exactly = 1) { movieApiService.getMovieDetailsById(movieId) }
        }

    @Test
    fun `getMovieDetailsById should throw NetworkException when api call fails`() = runTest {

        coEvery { movieApiService.getMovieDetailsById(movieId) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMovieDetailsById(movieId)
        }
    }

    @Test
    fun `getPopularMovies should return popular movies when successful`() = runTest {

        coEvery { movieApiService.getPopularMovies() } returns expectedResponse

        val popularMovies = movieRemoteDataSourceImpl.getPopularMovies()

        assertThat(popularMovies).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.getPopularMovies() }
    }

    @Test
    fun `getPopularMovies should throw NetworkException when api call fails`() = runTest {

        coEvery { movieApiService.getPopularMovies() } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getPopularMovies()
        }
    }

    @Test
    fun `getUpcomingMovies should return upcoming movies when successful`() = runTest {

        coEvery { movieApiService.getUpcomingMovies() } returns expectedResponse

        val upcomingMovies = movieRemoteDataSourceImpl.getUpcomingMovies()

        assertThat(upcomingMovies).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.getUpcomingMovies() }
    }

    @Test
    fun `getUpcomingMovies should throw NetworkException when api call fails`() = runTest {

        coEvery { movieApiService.getUpcomingMovies() } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getUpcomingMovies()
        }
    }

    @Test
    fun `getTopRatedMovies should return top rated movies when successful`() = runTest {

        coEvery { movieApiService.getTopRatedMovies(page) } returns expectedResponse

        val topRatedMovies = movieRemoteDataSourceImpl.getTopRatedMovies(page)

        assertThat(topRatedMovies).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.getTopRatedMovies(page) }
    }

    @Test
    fun `getTopRatedMovies should throw NetworkException when api call fails`() = runTest {
        coEvery { movieApiService.getTopRatedMovies(page) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getTopRatedMovies(page)
        }
    }

    @Test
    fun `getMoviesByGenreIds should return movies for given genre IDs when successful`() = runTest {

        coEvery { movieApiService.getMoviesByGenreIds(genreIds, 1) } returns expectedResponse

        val movies = movieRemoteDataSourceImpl.getMoviesByGenreIds(genreIds, 1)

        assertThat(movies).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.getMoviesByGenreIds(genreIds, 1) }
    }

    @Test
    fun `getMoviesByGenreIds should throw NetworkException when api call fails`() = runTest {

        coEvery { movieApiService.getMoviesByGenreIds(genreIds, 1) } throws NetworkException()


        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMoviesByGenreIds(genreIds, 1)
        }
    }

    @Test
    fun `getRatedMovie should return Unit when successful`() = runTest {

        coEvery { movieApiService.getRatedMovies(0) } returns expectedResponse

        val result = movieRemoteDataSourceImpl.getRatedMovies()

        assertThat(result).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.getRatedMovies(0) }
    }

    @Test
    fun `getRatedMovie should throw NetworkException when api call fails`() = runTest {

        coEvery { movieApiService.getRatedMovies(0) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getRatedMovies()
        }
    }

    @Test
    fun `deleteMovieRate should call deleteMovieRate in API with correct parameters`() = runTest {

        coEvery {
            movieApiService.deleteMovieRate(movieId)
        } returns Unit


        movieRemoteDataSourceImpl.deleteMovieRate(movieId)


        coVerify(exactly = 1) {
            movieApiService.deleteMovieRate(movieId)
        }
    }

    @Test
    fun `getMoviesByGenreId should return movies for given genre ID when successful `() = runTest {
        coEvery {
            movieApiService.getMoviesByGenreIds(
                listOf(genreId),
                page
            )
        } returns expectedResponse

        val movies = movieRemoteDataSourceImpl.getMoviesByGenreId(genreId, page)

        assertThat(movies).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.getMoviesByGenreIds(listOf(genreId), page) }

    }

    val movieId = 1399L
    val genreId = 28L
    val page = 1
    val rating = 5.0f
    val genreIds = listOf(28L)
    val countryIsoCode = "US"
    val name = "test"
    val actorIds = listOf(6193)
    val keyword = "Inception"
    val expectedRatingResponse = RatingResponse(statusCode = 12, statusMessage = "success")
    val expectedResponse = RemoteMovieResponse(
        page = page,
        results = listOf(remoteMovieItemDto),
        totalPages = 1,
        totalResults = 1
    )
    val expectedCastAndCrewResponse = RemoteCastAndCrewResponse(
        id = movieId,
        cast = emptyList(),
        crew = emptyList()
    )
    val expectedActorSearchResponse = RemoteActorSearchResponse(
        page = 1,
        totalPages = 1,
        totalResults = 1,
        actors = listOf(
            actorSearchItemDto
        )
    )
    val expectedEmptyActorSearchResponse = RemoteActorSearchResponse(
        page = 1,
        totalPages = 1,
        totalResults = 1,
        actors = emptyList()
    )
}





