package com.example.localdatasource.roomDataBase.datasource

import com.example.entity.category.MovieGenre
import com.example.localdatasource.roomDataBase.daos.MovieCategoryInterestDao
import com.example.localdatasource.roomDataBase.daos.MovieDao
import com.example.localdatasource.utils.createMovie
import com.example.repository.dto.local.LocalMovieCategoryInterestDto
import com.example.repository.dto.local.SearchMovieCrossRefDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.utils.SearchType
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MovieLocalDataSourceImplTest {

    private lateinit var movieDao: MovieDao
    private lateinit var interestDao: MovieCategoryInterestDao
    private lateinit var dataSource: MovieLocalDataSourceImpl

    @BeforeEach
    fun setUp() {
        movieDao = mockk(relaxed = true)
        interestDao = mockk(relaxed = true)
        dataSource = MovieLocalDataSourceImpl(movieDao, interestDao)
    }

    @Test
    fun `getMoviesByKeywordAndSearchType should return correct list`() = runTest {
        //Given
        val expected = listOf(mockk<MovieWithCategories>())
        coEvery {
            movieDao.getMoviesByKeywordAndSearchType("action", SearchType.BY_KEYWORD, "en", 10, 0)
        } returns expected
        //When
        val result =
            dataSource.getMoviesByKeywordAndSearchType("action", SearchType.BY_KEYWORD, "en", 10, 0)
        //Then
        assertThat(result).isEqualTo(expected)
    }
    @Test
    fun `addMoviesBySearchData should insert movies and search entries`() = runTest {
        //Given
        val movies = listOf(
            createMovie(movieId = 1, storedLanguage = "en")
        )
        val keyword = "action"
        val searchType = SearchType.BY_KEYWORD
        val expireDate = Instant.parse("2025-07-22T00:00:00Z")
        //When
        dataSource.addMoviesBySearchData(movies, keyword, searchType, expireDate)
        //Then
        coVerify(exactly = 1) { movieDao.insertMovies(movies) }

        val expectedCrossRefs = movies.map {
            SearchMovieCrossRefDto(
                searchKeyword = keyword,
                searchType = searchType,
                movieId = it.movieId,
                storedLanguage = it.storedLanguage
            )
        }

        coVerify(exactly = 1) { movieDao.insertSearchEntries(expectedCrossRefs) }
    }

    @Test
    fun `getMovieById should return the correct movie`() = runTest {
        //Given
        val movie = createMovie(movieId = 42, storedLanguage = "en")
        coEvery { movieDao.getMovieById(42) } returns movie
        //When
        val result = dataSource.getMovieById(42)
        //Then
        assertThat(result).isEqualTo(movie)
    }

    @Test
    fun `incrementGenreInterest should call incrementInterest in the interestDao with correct genre`() =
        runTest {
            //Given
            val genre = MovieGenre.ACTION
            //When
            dataSource.incrementGenreInterest(genre)
            //Then
            coVerify(exactly = 1) { interestDao.incrementInterest(genre) }
        }

    @Test
    fun `getAllGenreInterests should return correct map`() = runTest {
        //Given
        val interestList = listOf(
            LocalMovieCategoryInterestDto(MovieGenre.COMEDY, 5)
        )
        coEvery { interestDao.getAllInterests() } returns interestList
        //When
        val result = dataSource.getAllGenreInterests()
        //Then
        assertThat(result).isEqualTo(mapOf(MovieGenre.COMEDY to 5))
    }
}
