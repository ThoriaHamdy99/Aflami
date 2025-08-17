package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.entity.Character
import com.amsterdam.entity.Movie
import com.amsterdam.repository.datasource.local.GameLocalDataSource
import com.amsterdam.repository.datasource.local.GameSessionLocalDataSource
import com.amsterdam.repository.datasource.remote.CharacterRemoteDataSource
import com.amsterdam.repository.datasource.remote.MovieRemoteDataSource
import com.amsterdam.repository.dto.remote.MovieItemRemoteDto
import com.amsterdam.repository.dto.remote.RemoteCharacterItemDto
import com.amsterdam.repository.mapper.toEntityList
import com.amsterdam.repository.mapper.toMovieEntityList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GameRepositoryImplTest {

    private lateinit var repository: GameRepository

    private val gameLocalDataSource: GameLocalDataSource = mockk()
    private val gameSessionLocalDataSource: GameSessionLocalDataSource = mockk()
    private val movieRemoteDataSource: MovieRemoteDataSource = mockk()
    private val characterRemoteDataSource: CharacterRemoteDataSource = mockk()

    @BeforeEach
    fun setup() {
        repository = GameRepositoryImpl(
            gameLocalDataSource = gameLocalDataSource,
            gameSessionLocalDataSource = gameSessionLocalDataSource,
            movieRemoteDataSource = movieRemoteDataSource,
            characterRemoteDataSource = characterRemoteDataSource
        )
    }

    @Test
    fun `getRandomMoviesWithReleaseDate should fetch and map movies from remote source`() =
        runTest {
            coEvery { movieRemoteDataSource.getRandomMoviesWithReleaseDate(any()) } returns testMoviesDtoList
            val result = repository.getRandomMoviesWithReleaseDate(requiredMoviesNumber = 2)
            assertThat(result).isEqualTo(expectedMoviesList)
            coVerify(exactly = 1) { movieRemoteDataSource.getRandomMoviesWithReleaseDate(2) }
        }

    @Test
    fun `getRandomMoviesWithPoster should fetch and map movies from remote source`() = runTest {
        coEvery { movieRemoteDataSource.getRandomMoviesWithPoster(any()) } returns testMoviesDtoList
        val result = repository.getRandomMoviesWithPoster(requiredMoviesNumber = 2)
        assertThat(result).isEqualTo(expectedMoviesList)
        coVerify(exactly = 1) { movieRemoteDataSource.getRandomMoviesWithPoster(2) }
    }

    @Test
    fun `updatePoints should call local data source to upsert points`() = runTest {
        coJustRun { gameLocalDataSource.upsertPoints(testPoints) }
        repository.updatePoints(testPoints)
        coVerify(exactly = 1) { gameLocalDataSource.upsertPoints(testPoints) }
    }

    @Test
    fun `getUserPoints should return points flow from local data source`() = runTest {
        every { gameLocalDataSource.getUserPoints() } returns flowOf(testPoints)
        val result = repository.getUserPoints().first()
        assertThat(result).isEqualTo(testPoints)
        verify(exactly = 1) { gameLocalDataSource.getUserPoints() }
    }

    @Test
    fun `getCharacterDataQuestions should fetch and map characters from remote source`() = runTest {
        coEvery { characterRemoteDataSource.getRandomizedTrendingCharacter(any()) } returns testCharactersDtoList

        val result = repository.getCharacterDataQuestions(requiredNumber = 2)

        assertThat(result).isEqualTo(expectedCharactersList)
        coVerify(exactly = 1) { characterRemoteDataSource.getRandomizedTrendingCharacter(2) }
    }

    @Test
    fun `addOneSecondToGameTime should call session data source`() {
        justRun { gameSessionLocalDataSource.addOneSecond(testGameSessionId) }
        repository.addOneSecondToGameTime(testGameSessionId)
        verify(exactly = 1) { gameSessionLocalDataSource.addOneSecond(testGameSessionId) }
    }

    @Test
    fun `getTotalSpentSeconds should get seconds from session data source`() {
        every { gameSessionLocalDataSource.getTotalSpentSeconds(testGameSessionId) } returns testSeconds
        val result = repository.getTotalSpentSeconds(testGameSessionId)
        assertThat(result).isEqualTo(testSeconds)
        verify(exactly = 1) { gameSessionLocalDataSource.getTotalSpentSeconds(testGameSessionId) }
    }

    @Test
    fun `addPointsToGame should call session data source with points and id`() {
        justRun { gameSessionLocalDataSource.addPoints(testPoints, testGameSessionId) }
        repository.addPointsToGame(testPoints, testGameSessionId)
        verify(exactly = 1) { gameSessionLocalDataSource.addPoints(testPoints, testGameSessionId) }
    }

    @Test
    fun `getCollectedPoints should get points from session data source`() {
        every { gameSessionLocalDataSource.getCollectedPoints(testGameSessionId) } returns testPoints
        val result = repository.getCollectedPoints(testGameSessionId)
        assertThat(result).isEqualTo(testPoints)
        verify(exactly = 1) { gameSessionLocalDataSource.getCollectedPoints(testGameSessionId) }
    }

    private val testMoviesDtoList = listOf(
        MovieItemRemoteDto(
            id = 1, title = "Movie 1", posterPath = "/poster1.jpg", voteAverage = 7.5,
            overview = "Overview 1", adult = false, backdropPath = "/backdrop1.jpg",
            originalLanguage = "en", originalTitle = "Original Movie 1", popularity = 100.0,
            releaseDate = "2023-01-15", video = false, voteCount = 500, runtime = 120,
            originCountry = listOf("US")
        ),
        MovieItemRemoteDto(
            id = 2, title = "Movie 2", posterPath = "/poster2.jpg", voteAverage = 8.0,
            overview = "Overview 2", adult = true, backdropPath = "/backdrop2.jpg",
            originalLanguage = "fr", originalTitle = "Original Movie 2", popularity = 200.0,
            releaseDate = "2024-02-20", video = false, voteCount = 1000, runtime = 95,
            originCountry = listOf("FR")
        )
    )

    private val expectedMoviesList: List<Movie> = testMoviesDtoList.toMovieEntityList()

    private val testCharactersDtoList = listOf(
        RemoteCharacterItemDto(
            id = 1,
            name = "Character 1",
            profilePath = "/char1.jpg",
            adult = false,
            gender = 2,
            popularity = 75.0,
            mediaType = "person",
            originalName = "Original Character 1"
        ),
        RemoteCharacterItemDto(
            id = 2,
            name = "Character 2",
            profilePath = "/char2.jpg",
            adult = false,
            gender = 1,
            popularity = 80.0,
            mediaType = "person",
            originalName = "Original Character 2"
        )
    )

    private val expectedCharactersList: List<Character> = testCharactersDtoList.toEntityList()

    private val testPoints = 100
    private val testGameSessionId = 12345L
    private val testSeconds = 60
}