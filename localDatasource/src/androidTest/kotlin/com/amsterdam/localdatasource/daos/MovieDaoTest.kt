package com.amsterdam.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.amsterdam.localdatasource.roomDataBase.AflamiDatabase
import com.amsterdam.localdatasource.roomDataBase.daos.MovieDao
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.MovieCategoryCrossRefDto
import com.amsterdam.repository.dto.local.utils.DatabaseConstants
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MovieDaoTest {

    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }
    private val database by lazy {
        Room.inMemoryDatabaseBuilder(context, AflamiDatabase::class.java).build()
    }
    private lateinit var dao: MovieDao

    @BeforeEach
    fun setup() {
        dao = database.movieDao()
    }

    @AfterEach
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsertMovie_shouldInsertMovie() = runTest {
        val movie = createMovie(movieId = 1L, storedLanguage = "en")

        dao.upsertMovie(movie)
        val result = dao.getMovieById(movie.movieId, movie.storedLanguage)

        assertThat(result).isEqualTo(movie)
    }

    @Test
    fun upsertMovie_shouldUpdateMovieWithoutDuplication_WhenMovieAlreadyStored() = runTest {
        val originalMovie = createMovie(movieId = 1L, storedLanguage = "en", name = "Original")
        val updatedMovie = originalMovie.copy(name = "Updated")
        dao.upsertMovie(originalMovie)

        dao.upsertMovie(updatedMovie)
        val result = dao.getMovieById(originalMovie.movieId, originalMovie.storedLanguage)

        assertThat(result?.name).isEqualTo("Updated")
    }

    @Test
    fun upsertMovies_shouldInsertMovies() = runTest {
        val movie = createMovie(movieId = 1L, storedLanguage = "en")

        dao.upsertMovies(listOf(movie))
        val result = dao.getMovieById(movie.movieId, movie.storedLanguage)

        assertThat(result).isEqualTo(movie)
    }

    @Test
    fun upsertMovies_shouldUpdateMovieWithoutDuplication_WhenMovieAlreadyStored() = runTest {
        val originalMovie = createMovie(movieId = 1L, storedLanguage = "en", name = "Original")
        val updatedMovie = originalMovie.copy(name = "Updated")
        dao.upsertMovies(listOf(originalMovie))

        dao.upsertMovies(listOf(updatedMovie))
        val result = dao.getMovieById(originalMovie.movieId, originalMovie.storedLanguage)

        assertThat(result?.name).isEqualTo("Updated")
    }

    @Test
    fun getMovieById_shouldGetMovie_WhenMovieIsExists() = runTest {
        val movie = createMovie(movieId = 1L, storedLanguage = "en", name = "Original")
        dao.upsertMovie(movie)

        val result = dao.getMovieById(movie.movieId, movie.storedLanguage)

        assertThat(result).isEqualTo(movie)
    }

    @Test
    fun getMovieById_shouldReturnNull_WhenMovieIsNotExists() = runTest {
        val result = dao.getMovieById(1L, "en")

        assertThat(result).isEqualTo(null)
    }
}

private val movieCategoryCrossRefDtoList = listOf(
    MovieCategoryCrossRefDto(
        movieId = 1,
        categoryId = 1,
        storedLanguage = "en"
    )
)

private fun createMovie(
    movieId: Long,
    storedLanguage: String,
    name: String = "Sample Movie"
): LocalMovieDto {
    return LocalMovieDto(
        movieId = movieId,
        storedLanguage = storedLanguage,
        name = name,
        description = "Test description",
        poster = "poster.jpg",
        releaseDate = LocalDate.parse("2020-01-01"),
        popularity = 9.5,
        rating = 4.3f,
        originCountry = "US",
        movieLength = 120
    )
}
