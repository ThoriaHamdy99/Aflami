package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.remotedatasource.util.MOVIE_TEST_ACTOR_ID
import com.amsterdam.remotedatasource.util.MOVIE_TEST_ACTOR_NAME
import com.amsterdam.remotedatasource.util.MOVIE_TEST_COUNTRY_ISO_CODE
import com.amsterdam.remotedatasource.util.MOVIE_TEST_GENRE_ID
import com.amsterdam.remotedatasource.util.MOVIE_TEST_ID
import com.amsterdam.remotedatasource.util.MOVIE_TEST_KEYWORD
import com.amsterdam.remotedatasource.util.MOVIE_TEST_PAGE
import com.amsterdam.remotedatasource.util.MOVIE_TEST_RATING
import com.amsterdam.remotedatasource.util.actorSearchItemRemoteDto
import com.amsterdam.remotedatasource.util.movieDetailsRemoteResponse
import com.amsterdam.remotedatasource.util.movieItemRemoteDto
import com.amsterdam.remotedatasource.util.movieItemRemoteDtoWithNullDate
import com.amsterdam.remotedatasource.util.movieItemRemoteDtoWithNullPoster
import com.amsterdam.repository.dto.remote.ActorSearchRemoteResponse
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.MovieRemoteResponse
import com.amsterdam.repository.dto.remote.RatingRemoteResponse
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
    private lateinit var movieRemoteDataSourceImpl: MovieRemoteDataSourceImpl

    @BeforeEach
    fun setUp() {
        movieApiService = mockk()
        movieRemoteDataSourceImpl = MovieRemoteDataSourceImpl(movieApiService)
    }

    @Test
    fun `getMoviesByKeyword should return a list of movies when the API call is successful`() =
        runTest {
            val expectedResponse = MovieRemoteResponse(
                page = MOVIE_TEST_PAGE,
                results = listOf(movieItemRemoteDto),
                totalPages = 1,
                totalResults = 1
            )
            coEvery {
                movieApiService.getMoviesByKeyword(
                    MOVIE_TEST_KEYWORD,
                    MOVIE_TEST_PAGE
                )
            } returns expectedResponse

            val movies =
                movieRemoteDataSourceImpl.getMoviesByKeyword(MOVIE_TEST_KEYWORD, MOVIE_TEST_PAGE)

            assertThat(movies).isEqualTo(expectedResponse)
            coVerify(exactly = 1) {
                movieApiService.getMoviesByKeyword(
                    MOVIE_TEST_KEYWORD,
                    MOVIE_TEST_PAGE
                )
            }
        }

    @Test
    fun `getMoviesByKeyword should throw NetworkException when the API call fails`() = runTest {
        coEvery {
            movieApiService.getMoviesByKeyword(
                MOVIE_TEST_KEYWORD,
                MOVIE_TEST_PAGE
            )
        } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMoviesByKeyword(MOVIE_TEST_KEYWORD, MOVIE_TEST_PAGE)
        }
        coVerify(exactly = 1) {
            movieApiService.getMoviesByKeyword(
                MOVIE_TEST_KEYWORD,
                MOVIE_TEST_PAGE
            )
        }
    }

    @Test
    fun `getMoviesByActorIds should return movies for a given actor when the API call is successful`() =
        runTest {
            val actorIds = listOf(MOVIE_TEST_ACTOR_ID)
            val expectedResponse = MovieRemoteResponse(
                page = MOVIE_TEST_PAGE,
                results = listOf(movieItemRemoteDto),
                totalPages = 1,
                totalResults = 1
            )
            coEvery { movieApiService.getMoviesByActorId(MOVIE_TEST_ACTOR_ID.toString()) } returns expectedResponse

            val movies = movieRemoteDataSourceImpl.getMoviesByActorIds(actorIds, MOVIE_TEST_PAGE)

            assertThat(movies).isEqualTo(expectedResponse)
            coVerify(exactly = 1) { movieApiService.getMoviesByActorId(MOVIE_TEST_ACTOR_ID.toString()) }
        }

    @Test
    fun `getMoviesByActorIds should throw NetworkException when the API call fails`() = runTest {
        val actorIds = listOf(MOVIE_TEST_ACTOR_ID)
        coEvery { movieApiService.getMoviesByActorId(MOVIE_TEST_ACTOR_ID.toString()) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMoviesByActorIds(actorIds, MOVIE_TEST_PAGE)
        }
        coVerify(exactly = 1) { movieApiService.getMoviesByActorId(MOVIE_TEST_ACTOR_ID.toString()) }
    }

    @Test
    fun `getActorIdsByName should return actor IDs when the API call is successful`() = runTest {
        val expectedResponse = ActorSearchRemoteResponse(
            page = MOVIE_TEST_PAGE,
            totalPages = 1,
            totalResults = 1,
            actors = listOf(actorSearchItemRemoteDto)
        )
        coEvery {
            movieApiService.getActorIdByName(
                MOVIE_TEST_ACTOR_NAME,
                MOVIE_TEST_PAGE
            )
        } returns expectedResponse

        val actorIds =
            movieRemoteDataSourceImpl.getActorIdsByName(MOVIE_TEST_ACTOR_NAME, MOVIE_TEST_PAGE)

        assertThat(actorIds).containsExactly(MOVIE_TEST_ACTOR_ID)
        coVerify(exactly = 1) {
            movieApiService.getActorIdByName(
                MOVIE_TEST_ACTOR_NAME,
                MOVIE_TEST_PAGE
            )
        }
    }

    @Test
    fun `getActorIdsByName should return an empty list when no actors are found`() = runTest {
        val name = "No Actor"
        val expectedResponse = ActorSearchRemoteResponse(
            page = MOVIE_TEST_PAGE,
            totalPages = 1,
            totalResults = 0,
            actors = emptyList()
        )
        coEvery { movieApiService.getActorIdByName(name, MOVIE_TEST_PAGE) } returns expectedResponse

        val actorIds = movieRemoteDataSourceImpl.getActorIdsByName(name, MOVIE_TEST_PAGE)

        assertThat(actorIds).isEmpty()
        coVerify(exactly = 1) { movieApiService.getActorIdByName(name, MOVIE_TEST_PAGE) }
    }

    @Test
    fun `getActorIdsByName should throw NetworkException when the API call fails`() = runTest {
        val name = "test"
        coEvery {
            movieApiService.getActorIdByName(
                name,
                MOVIE_TEST_PAGE
            )
        } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getActorIdsByName(name, MOVIE_TEST_PAGE)
        }
        coVerify(exactly = 1) { movieApiService.getActorIdByName(name, MOVIE_TEST_PAGE) }
    }

    @Test
    fun `getMoviesByCountryIsoCode should return movies from a specific country when the API call is successful`() =
        runTest {
            val expectedResponse = MovieRemoteResponse(
                page = MOVIE_TEST_PAGE,
                results = listOf(movieItemRemoteDto),
                totalPages = 1,
                totalResults = 1
            )
            coEvery {
                movieApiService.getMoviesByCountryIsoCode(
                    MOVIE_TEST_COUNTRY_ISO_CODE,
                    MOVIE_TEST_PAGE
                )
            } returns expectedResponse

            val movies = movieRemoteDataSourceImpl.getMoviesByCountryIsoCode(
                MOVIE_TEST_COUNTRY_ISO_CODE,
                MOVIE_TEST_PAGE
            )

            assertThat(movies).isEqualTo(expectedResponse)
            coVerify(exactly = 1) {
                movieApiService.getMoviesByCountryIsoCode(
                    MOVIE_TEST_COUNTRY_ISO_CODE,
                    MOVIE_TEST_PAGE
                )
            }
        }

    @Test
    fun `getMoviesByCountryIsoCode should throw NetworkException when the API call fails`() =
        runTest {
            coEvery {
                movieApiService.getMoviesByCountryIsoCode(
                    MOVIE_TEST_COUNTRY_ISO_CODE,
                    MOVIE_TEST_PAGE
                )
            } throws NetworkException()

            assertThrows<NetworkException> {
                movieRemoteDataSourceImpl.getMoviesByCountryIsoCode(
                    MOVIE_TEST_COUNTRY_ISO_CODE,
                    MOVIE_TEST_PAGE
                )
            }
            coVerify(exactly = 1) {
                movieApiService.getMoviesByCountryIsoCode(
                    MOVIE_TEST_COUNTRY_ISO_CODE,
                    MOVIE_TEST_PAGE
                )
            }
        }

    @Test
    fun `getCastByMovieId should return cast and crew for a movie when the API call is successful`() =
        runTest {
            val expectedResponse = CastAndCrewRemoteResponse(
                id = MOVIE_TEST_ID,
                cast = emptyList(),
                crew = emptyList()
            )
            coEvery { movieApiService.getCastByMovieId(MOVIE_TEST_ID) } returns expectedResponse

            val castAndCrew = movieRemoteDataSourceImpl.getCastByMovieId(MOVIE_TEST_ID)

            assertThat(castAndCrew).isEqualTo(expectedResponse)
            coVerify(exactly = 1) { movieApiService.getCastByMovieId(MOVIE_TEST_ID) }
        }

    @Test
    fun `getCastByMovieId should throw NetworkException when the API call fails`() = runTest {
        coEvery { movieApiService.getCastByMovieId(MOVIE_TEST_ID) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getCastByMovieId(MOVIE_TEST_ID)
        }
        coVerify(exactly = 1) { movieApiService.getCastByMovieId(MOVIE_TEST_ID) }
    }

    @Test
    fun `getMovieDetailsById should return movie details including nested data when the API call is successful`() =
        runTest {
            val expectedResponse = movieDetailsRemoteResponse
            coEvery { movieApiService.getMovieDetailsById(MOVIE_TEST_ID) } returns expectedResponse

            val details = movieRemoteDataSourceImpl.getMovieDetailsById(MOVIE_TEST_ID)

            assertThat(details).isEqualTo(expectedResponse)
            coVerify(exactly = 1) { movieApiService.getMovieDetailsById(MOVIE_TEST_ID) }
        }

    @Test
    fun `getMovieDetailsById should throw NetworkException when the API call fails`() = runTest {
        coEvery { movieApiService.getMovieDetailsById(MOVIE_TEST_ID) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMovieDetailsById(MOVIE_TEST_ID)
        }
        coVerify(exactly = 1) { movieApiService.getMovieDetailsById(MOVIE_TEST_ID) }
    }

    @Test
    fun `getPopularMovies should return popular movies when the API call is successful`() =
        runTest {
            val expectedResponse = MovieRemoteResponse(
                page = MOVIE_TEST_PAGE,
                results = listOf(movieItemRemoteDto),
                totalPages = 1,
                totalResults = 1
            )
            coEvery { movieApiService.getPopularMovies(MOVIE_TEST_PAGE) } returns expectedResponse

            val popularMovies = movieRemoteDataSourceImpl.getPopularMovies(MOVIE_TEST_PAGE)

            assertThat(popularMovies).isEqualTo(expectedResponse)
            coVerify(exactly = 1) { movieApiService.getPopularMovies(MOVIE_TEST_PAGE) }
        }

    @Test
    fun `getPopularMovies should throw NetworkException when the API call fails`() = runTest {
        coEvery { movieApiService.getPopularMovies(MOVIE_TEST_PAGE) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getPopularMovies(MOVIE_TEST_PAGE)
        }
        coVerify(exactly = 1) { movieApiService.getPopularMovies(MOVIE_TEST_PAGE) }
    }

    @Test
    fun `getUpcomingMovies should return upcoming movies when the API call is successful`() =
        runTest {
            val expectedResponse = MovieRemoteResponse(
                page = MOVIE_TEST_PAGE,
                results = listOf(movieItemRemoteDto),
                totalPages = 1,
                totalResults = 1
            )
            coEvery { movieApiService.getUpcomingMovies() } returns expectedResponse

            val upcomingMovies = movieRemoteDataSourceImpl.getUpcomingMovies()

            assertThat(upcomingMovies).isEqualTo(expectedResponse)
            coVerify(exactly = 1) { movieApiService.getUpcomingMovies() }
        }

    @Test
    fun `getUpcomingMovies should throw NetworkException when the API call fails`() = runTest {
        coEvery { movieApiService.getUpcomingMovies() } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getUpcomingMovies()
        }
        coVerify(exactly = 1) { movieApiService.getUpcomingMovies() }
    }

    @Test
    fun `getTopRatedMovies should return top rated movies when the API call is successful`() =
        runTest {
            val expectedResponse = MovieRemoteResponse(
                page = MOVIE_TEST_PAGE,
                results = listOf(movieItemRemoteDto),
                totalPages = 1,
                totalResults = 1
            )
            coEvery { movieApiService.getTopRatedMovies(MOVIE_TEST_PAGE) } returns expectedResponse

            val topRatedMovies = movieRemoteDataSourceImpl.getTopRatedMovies(MOVIE_TEST_PAGE)

            assertThat(topRatedMovies).isEqualTo(expectedResponse)
            coVerify(exactly = 1) { movieApiService.getTopRatedMovies(MOVIE_TEST_PAGE) }
        }

    @Test
    fun `getTopRatedMovies should throw NetworkException when the API call fails`() = runTest {
        coEvery { movieApiService.getTopRatedMovies(MOVIE_TEST_PAGE) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getTopRatedMovies(MOVIE_TEST_PAGE)
        }
        coVerify(exactly = 1) { movieApiService.getTopRatedMovies(MOVIE_TEST_PAGE) }
    }

    @Test
    fun `getMoviesByGenreIds should return movies for given genre IDs when the API call is successful`() =
        runTest {
            val genreIds = listOf(MOVIE_TEST_GENRE_ID)
            val expectedResponse = MovieRemoteResponse(
                page = MOVIE_TEST_PAGE,
                results = listOf(movieItemRemoteDto),
                totalPages = 1,
                totalResults = 1
            )
            coEvery {
                movieApiService.getMoviesByGenreIds(
                    genreIds,
                    MOVIE_TEST_PAGE
                )
            } returns expectedResponse

            val movies = movieRemoteDataSourceImpl.getMoviesByGenreIds(genreIds, MOVIE_TEST_PAGE)

            assertThat(movies).isEqualTo(expectedResponse)
            coVerify(exactly = 1) { movieApiService.getMoviesByGenreIds(genreIds, MOVIE_TEST_PAGE) }
        }

    @Test
    fun `getMoviesByGenreIds should throw NetworkException when the API call fails`() = runTest {
        val genreIds = listOf(MOVIE_TEST_GENRE_ID)
        coEvery {
            movieApiService.getMoviesByGenreIds(
                genreIds,
                MOVIE_TEST_PAGE
            )
        } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMoviesByGenreIds(genreIds, MOVIE_TEST_PAGE)
        }
        coVerify(exactly = 1) { movieApiService.getMoviesByGenreIds(genreIds, MOVIE_TEST_PAGE) }
    }

    @Test
    fun `getMoviesByGenreId should call the API with a single genre ID`() = runTest {
        val genreId = MOVIE_TEST_GENRE_ID
        val expectedResponse = MovieRemoteResponse(
            page = MOVIE_TEST_PAGE,
            results = listOf(movieItemRemoteDto),
            totalPages = 1,
            totalResults = 1
        )
        coEvery {
            movieApiService.getMoviesByGenreIds(
                listOf(genreId),
                MOVIE_TEST_PAGE
            )
        } returns expectedResponse

        val movies = movieRemoteDataSourceImpl.getMoviesByGenreId(genreId, MOVIE_TEST_PAGE)

        assertThat(movies).isEqualTo(expectedResponse)
        coVerify(exactly = 1) {
            movieApiService.getMoviesByGenreIds(
                listOf(genreId),
                MOVIE_TEST_PAGE
            )
        }
    }

    @Test
    fun `setMovieRate should return a RatingResponse when the API call is successful`() = runTest {
        val expectedResponse = RatingRemoteResponse(statusCode = 12, statusMessage = "success")

        coEvery {
            movieApiService.postMovieRating(
                MOVIE_TEST_ID,
                MOVIE_TEST_RATING
            )
        } returns expectedResponse

        val result = movieRemoteDataSourceImpl.setMovieRate(MOVIE_TEST_RATING, MOVIE_TEST_ID)

        assertThat(result).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.postMovieRating(MOVIE_TEST_ID, MOVIE_TEST_RATING) }
    }

    @Test
    fun `getRatedMovies should return movies with user ratings when the API call is successful`() =
        runTest {
            val expectedResponse = MovieRemoteResponse(
                page = MOVIE_TEST_PAGE,
                results = listOf(movieItemRemoteDto),
                totalPages = 1,
                totalResults = 1
            )

            coEvery { movieApiService.getRatedMovies() } returns expectedResponse

            val result = movieRemoteDataSourceImpl.getRatedMovies()

            assertThat(result).isEqualTo(expectedResponse)
            coVerify(exactly = 1) { movieApiService.getRatedMovies() }
        }

    @Test
    fun `getRatedMovies should throw NetworkException when the API call fails`() = runTest {
        coEvery { movieApiService.getRatedMovies() } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getRatedMovies()
        }
        coVerify(exactly = 1) { movieApiService.getRatedMovies() }
    }

    @Test
    fun `deleteMovieRate should call the deleteMovieRate API method with the correct movie ID`() =
        runTest {
            coEvery { movieApiService.deleteMovieRate(MOVIE_TEST_ID) } returns Unit

            movieRemoteDataSourceImpl.deleteMovieRate(MOVIE_TEST_ID)

            coVerify(exactly = 1) { movieApiService.deleteMovieRate(MOVIE_TEST_ID) }
        }

    @Test
    fun `getRandomMoviesWithNotNullDate should return the required number of movies, handling null dates and duplicates`() =
        runTest {
            val requiredMoviesNumber = 3
            val moviesWithoutDate = listOf(movieItemRemoteDtoWithNullDate)
            val moviesWithDate = listOf(
                movieItemRemoteDto,
                movieItemRemoteDto.copy(id = 2),
                movieItemRemoteDto.copy(id = 3),
                movieItemRemoteDto.copy(id = 4)
            )

            coEvery { movieApiService.getPopularMovies(any()) } returns MovieRemoteResponse(
                1,
                moviesWithoutDate,
                1,
                10
            ) andThen
                    MovieRemoteResponse(2, moviesWithDate, 2, 10)

            val result =
                movieRemoteDataSourceImpl.getRandomMoviesWithNotNullDate(requiredMoviesNumber)

            assertThat(result.size).isEqualTo(requiredMoviesNumber)
            assertThat(result).containsNoDuplicates()
            coVerify(atLeast = 2) { movieApiService.getPopularMovies(any()) }
        }

    @Test
    fun `getRandomMoviesWithNotNullDate should break the loop when enough movies are collected`() =
        runTest {
            val requiredMoviesNumber = 2
            val moviesWithDate = listOf(
                movieItemRemoteDto,
                movieItemRemoteDto.copy(id = 2),
                movieItemRemoteDto.copy(id = 3)
            )

            coEvery { movieApiService.getPopularMovies(any()) } returns MovieRemoteResponse(
                1,
                moviesWithDate,
                1,
                10
            )

            val result =
                movieRemoteDataSourceImpl.getRandomMoviesWithNotNullDate(requiredMoviesNumber)

            assertThat(result.size).isEqualTo(requiredMoviesNumber)
            coVerify(exactly = 1) { movieApiService.getPopularMovies(any()) }
        }

    @Test
    fun `getRandomMoviesWithNotNullDate should handle duplicates correctly`() = runTest {
        val requiredMoviesNumber = 2
        val moviesWithDate = listOf(
            movieItemRemoteDto,
            movieItemRemoteDto.copy(id = 2),
            movieItemRemoteDto.copy(id = 2)
        )

        coEvery { movieApiService.getPopularMovies(any()) } returns MovieRemoteResponse(
            1,
            moviesWithDate,
            1,
            10
        )

        val result = movieRemoteDataSourceImpl.getRandomMoviesWithNotNullDate(requiredMoviesNumber)

        assertThat(result.size).isEqualTo(requiredMoviesNumber)
        assertThat(result).containsNoDuplicates()
    }

    @Test
    fun `getRandomMoviesWithNotNullPoster should return the required number of movies, handling null posters`() =
        runTest {
            val requiredMoviesNumber = 3
            val moviesWithoutPoster = listOf(movieItemRemoteDtoWithNullPoster)
            val moviesWithPoster = listOf(
                movieItemRemoteDto,
                movieItemRemoteDto.copy(id = 2),
                movieItemRemoteDto.copy(id = 3),
                movieItemRemoteDto.copy(id = 4)
            )

            coEvery { movieApiService.getPopularMovies(any()) } returns MovieRemoteResponse(
                1,
                moviesWithoutPoster,
                1,
                10
            ) andThen
                    MovieRemoteResponse(2, moviesWithPoster, 2, 10)

            val result =
                movieRemoteDataSourceImpl.getRandomMoviesWithNotNullPoster(requiredMoviesNumber)

            assertThat(result.size).isEqualTo(requiredMoviesNumber)
            coVerify(atLeast = 2) { movieApiService.getPopularMovies(any()) }
        }

    @Test
    fun `getRandomMoviesWithNotNullPoster should break the loop when enough movies are collected`() =
        runTest {
            val requiredMoviesNumber = 2
            val moviesWithPoster = listOf(
                movieItemRemoteDto,
                movieItemRemoteDto.copy(id = 2),
                movieItemRemoteDto.copy(id = 3)
            )

            coEvery { movieApiService.getPopularMovies(any()) } returns MovieRemoteResponse(
                1,
                moviesWithPoster,
                1,
                10
            )

            val result =
                movieRemoteDataSourceImpl.getRandomMoviesWithNotNullPoster(requiredMoviesNumber)

            assertThat(result.size).isEqualTo(requiredMoviesNumber)
            coVerify(exactly = 1) { movieApiService.getPopularMovies(any()) }
        }

    @Test
    fun `getRandomMoviesWithNotNullPoster should handle duplicates correctly`() = runTest {
        val requiredMoviesNumber = 2
        val moviesWithPoster = listOf(
            movieItemRemoteDto,
            movieItemRemoteDto.copy(id = 2),
            movieItemRemoteDto.copy(id = 2)
        )

        coEvery { movieApiService.getPopularMovies(any()) } returns MovieRemoteResponse(
            1,
            moviesWithPoster,
            1,
            10
        )

        val result =
            movieRemoteDataSourceImpl.getRandomMoviesWithNotNullPoster(requiredMoviesNumber)

        assertThat(result).containsNoDuplicates()
    }
}