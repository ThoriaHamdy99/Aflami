package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Country
import com.amsterdam.entity.Gender
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.datasource.local.AppLocalPreferences
import com.amsterdam.repository.datasource.local.CategoryLocalDataSource
import com.amsterdam.repository.datasource.local.MovieLocalDataSource
import com.amsterdam.repository.datasource.remote.CategoryRemoteDataSource
import com.amsterdam.repository.datasource.remote.MovieRemoteDataSource
import com.amsterdam.repository.dto.local.MovieCategoryLocalDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import com.amsterdam.repository.dto.remote.CategoryRemoteDto
import com.amsterdam.repository.dto.remote.CategoryRemoteResponse
import com.amsterdam.repository.dto.remote.MovieDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.MovieItemRemoteDto
import com.amsterdam.repository.dto.remote.MovieRemoteResponse
import com.amsterdam.repository.mapper.toDto
import com.amsterdam.repository.mapper.toEntity
import com.amsterdam.repository.mapper.toMovieEntityList
import com.amsterdam.repository.utils.remoteCastAndCrewResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MovieRepositoryImplTest {

    private val categoryLocalDataSource: CategoryLocalDataSource = mockk(relaxed = true)
    private val movieLocalDataSource: MovieLocalDataSource = mockk(relaxed = true)
    private val categoryRemoteDataSource: CategoryRemoteDataSource = mockk(relaxed = true)
    private val movieRemoteDataSource: MovieRemoteDataSource = mockk(relaxed = true)
    private val preferences: AppLocalPreferences = mockk(relaxed = true)

    private val movieRepository: MovieRepository by lazy {
        MovieRepositoryImpl(
            categoryLocalDataSource = categoryLocalDataSource,
            movieLocalDataSource = movieLocalDataSource,
            categoryRemoteDataSource = categoryRemoteDataSource,
            movieRemoteDataSource = movieRemoteDataSource,
            preferences = preferences
        )
    }

    @BeforeEach
    fun setup() {
        coEvery { preferences.getAppLanguage() } returns flowOf("en")
    }

    @Test
    fun `getMoviesByKeyword should return mapped movies from remote`() = runTest {
        coEvery { movieRemoteDataSource.getMoviesByKeyword(keyword, page) } returns remoteMovieResponse
        val result = movieRepository.getMoviesByKeyword(keyword, page, moviesPerPage)
        assertThat(result).isEqualTo(expectedEntityMovies)
    }

    @Test
    fun `getMoviesByActor should return movies when actor is found`() = runTest {
        coEvery { movieRemoteDataSource.getActorIdsByName(actorName, page) } returns actorIds
        coEvery { movieRemoteDataSource.getMoviesByActorIds(actorIds, page) } returns remoteMovieResponse
        val result = movieRepository.getMoviesByActor(actorName, page, moviesPerPage)
        assertThat(result).isEqualTo(expectedEntityMovies)
    }

    @Test
    fun `getMoviesByActor should return empty list when actor is not found`() = runTest {
        coEvery { movieRemoteDataSource.getActorIdsByName(actorName, page) } returns emptyList()
        val result = movieRepository.getMoviesByActor(actorName, page, moviesPerPage)
        assertThat(result).isEmpty()
    }

    @Test
    fun `getMoviesByCountry should return mapped movies from remote`() = runTest {
        coEvery { movieRemoteDataSource.getMoviesByCountryIsoCode(country.countryIsoCode, page) } returns remoteMovieResponse
        val result = movieRepository.getMoviesByCountry(country, page, moviesPerPage)
        assertThat(result).isEqualTo(expectedEntityMovies)
    }

    @Test
    fun `getActorsByMovieId should return mapped actors from remote`() = runTest {
        coEvery { movieRemoteDataSource.getCastByMovieId(movieId) } returns remoteCastAndCrewResponse
        val result = movieRepository.getActorsByMovieId(movieId)
        assertThat(result).isEqualTo(expectedActors)
    }

    @Test
    fun `getMoviesByGenres should return mapped movies from remote`() = runTest {
        coEvery { movieRemoteDataSource.getMoviesByGenreIds(expectedDtoGenres, page) } returns remoteMovieResponse
        val result = movieRepository.getMoviesByGenres(genres, page)
        assertThat(result).isEqualTo(expectedEntityMovies)
    }

    @Test
    fun `getMoviesByGenre should return mapped movies from remote`() = runTest {
        coEvery { movieRemoteDataSource.getMoviesByGenreId(MovieGenre.ACTION.toDto(), page) } returns remoteMovieResponse
        val result = movieRepository.getMoviesByGenre(MovieGenre.ACTION, page)
        assertThat(result).isEqualTo(expectedEntityMovies)
    }

        @Test
    fun `getUpcomingMovies should fetch from remote if local cache is empty`() = runTest {
        coEvery { movieLocalDataSource.getUpcomingMovies(any()) } returns emptyList()
        coEvery { movieRemoteDataSource.getUpcomingMovies() } returns remoteMovieResponse
        val result = movieRepository.getUpcomingMovies()
        assertThat(result).isNotEmpty()
        coVerify(exactly = 1) { movieRemoteDataSource.getUpcomingMovies() }
        coVerify(exactly = 1) { movieLocalDataSource.upsertUpcomingMovies(any()) }
    }


    @Test
    fun `getPopularMovies should fetch from remote if local cache is empty`() = runTest {
        coEvery { movieLocalDataSource.getPopularMovies(any()) } returns emptyList()
        coEvery { movieRemoteDataSource.getPopularMovies() } returns remoteMovieResponse
        val result = movieRepository.getPopularMovies()
        assertThat(result).isNotEmpty()
        coVerify(exactly = 1) { movieRemoteDataSource.getPopularMovies() }
        coVerify(exactly = 1) { movieLocalDataSource.upsertPopularMovies(any()) }
    }

    @Test
    fun `getTopRatedMovies for page 1 should fetch from remote if local cache is empty`() = runTest {
        coEvery { movieLocalDataSource.getTopRatedMovies(any()) } returns emptyList()
        coEvery { movieRemoteDataSource.getTopRatedMovies(1) } returns remoteMovieResponse
        val result = movieRepository.getTopRatedMovies(page = 1)
        assertThat(result).isNotEmpty()
        coVerify(exactly = 1) { movieRemoteDataSource.getTopRatedMovies(1) }
        coVerify(exactly = 1) { movieLocalDataSource.upsertTopRatedMovies(any()) }
    }

    @Test
    fun `getTopRatedMovies for page greater than 1 should always fetch from remote`() = runTest {
        coEvery { movieRemoteDataSource.getTopRatedMovies(2) } returns remoteMovieResponse
        val result = movieRepository.getTopRatedMovies(page = 2)
        assertThat(result).isNotEmpty()
        coVerify(exactly = 1) { movieRemoteDataSource.getTopRatedMovies(2) }
        coVerify(exactly = 0) { movieLocalDataSource.getTopRatedMovies(any()) }
    }

    @Test
    fun `setMovieRate should call remote with rate multiplied by 2`() = runTest {
        val userRate = 8
        val apiRate = 16.0f
        coJustRun { movieRemoteDataSource.setMovieRate(apiRate, movieId) }
        movieRepository.setMovieRate(userRate, movieId)
        coVerify(exactly = 1) { movieRemoteDataSource.setMovieRate(apiRate, movieId) }
    }

    @Test
    fun `getUserRatedMovies should return movies with rate multiplied by 2`() = runTest {
        coEvery { movieRemoteDataSource.getRatedMovies() } returns ratedMovieResponse
        val result = movieRepository.getUserRatedMovies()
        assertThat(result.first().userRate).isEqualTo(16)
    }

    @Test
    fun `deleteMovieRate should call remote to delete rate`() = runTest {
        coEvery { movieRemoteDataSource.deleteMovieRate(movieId) } just Runs
        movieRepository.deleteMovieRate(movieId)
        coVerify(exactly = 1) { movieRemoteDataSource.deleteMovieRate(movieId) }
    }

    @Test
    fun `saveMovieWithCategories should fetch and save categories if not cached`() = runTest {
        coEvery { movieLocalDataSource.getPopularMovies(any()) } returns emptyList()
        coEvery { movieRemoteDataSource.getPopularMovies() } returns remoteMovieResponse
        coEvery { categoryLocalDataSource.getMovieCategories() } returns emptyList()
        coEvery { categoryRemoteDataSource.getMovieCategories() } returns categoryRemoteResponse

        movieRepository.getPopularMovies()

        coVerify(exactly = 1) { categoryRemoteDataSource.getMovieCategories() }
        coVerify(exactly = 1) { categoryLocalDataSource.upsertMovieCategories(any()) }
    }

    private val keyword = "keyword"
    private val page = 1
    private val moviesPerPage = 20
    private val actorName = "Tom Hanks"
    private val actorIds = listOf(1, 2)
    private val country = Country("EG", "Egypt")
    private val movieId = 123L

    private val testMovieDto = MovieItemRemoteDto(
        id = 1L,
        title = "Test Movie",
        overview = "An overview.",
        posterPath = "/poster.jpg",
        backdropPath = "/backdrop.jpg",
        releaseDate = "2023-01-01",
        voteAverage = 8.0,
        adult = false,
        originalLanguage = "en",
        originalTitle = "Original Test Movie",
        popularity = 100.0,
        video = false,
        voteCount = 500
    )

    private val remoteMovieResponse = MovieRemoteResponse(
        page = 1,
        results = listOf(testMovieDto),
        totalPages = 1,
        totalResults = 1
    )

    private val ratedMovieResponse = MovieRemoteResponse(
        page = 1,
        results = listOf(testMovieDto.copy(rating = 8.0f)),
        totalPages = 1,
        totalResults = 1
    )

    private val expectedEntityMovies = listOf(testMovieDto).toMovieEntityList()

    private val localMovieWithCategories = MovieWithCategories(
        movie = mockk { coEvery { movieId } returns 1L },
        categories = listOf(MovieCategoryLocalDto(categoryId = 28L))
    )

    private val expectedMovieDetails = mockk<GetMovieDetailsUseCase.MovieDetails>()

    private val movieDetailsRemoteResponse = mockk<MovieDetailsRemoteResponse>(relaxed = true) {
        every { toEntity() } returns expectedMovieDetails
    }

    private val expectedActors = listOf(
        Actor(
            id = 1, name = "Actor One", imageUrl = "https://image.tmdb.org/t/p/w500/img1.jpg",
            popularity = 100.0, gender = Gender.Female
        )
    )

    private val genres = listOf(MovieGenre.ALL, MovieGenre.ACTION, MovieGenre.ADVENTURE)
    private val expectedDtoGenres = listOf(35L, 28L, 12L)

    private val categoryRemoteResponse = CategoryRemoteResponse(
        genres = listOf(CategoryRemoteDto(id = 28, name = "Action"))
    )
}