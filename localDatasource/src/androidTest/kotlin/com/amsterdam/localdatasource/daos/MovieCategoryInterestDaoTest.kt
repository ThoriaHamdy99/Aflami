package com.amsterdam.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.localdatasource.roomDataBase.AflamiDatabase
import com.amsterdam.localdatasource.roomDataBase.daos.MovieCategoryInterestDao
import com.amsterdam.repository.dto.local.LocalMovieCategoryInterestDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MovieCategoryInterestDaoTest {

    private lateinit var database: AflamiDatabase
    private lateinit var interestDao: MovieCategoryInterestDao

    @BeforeEach
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AflamiDatabase::class.java).build()
        interestDao = database.movieCategoryInterestDao()
    }

    @AfterEach
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertInterest_shouldAddNewInterest() = runTest {
        // Given
        val dto = LocalMovieCategoryInterestDto(genre = MovieGenre.ACTION, interestCount = 1)

        // When
        interestDao.insertInterest(dto)

        // Then
        val stored = interestDao.getAllInterests()
        assertThat(stored).containsExactly(dto)
    }

    @Test
    fun insertInterest_shouldUpdateExistingInterest() = runTest {
        // Given
        val initial = LocalMovieCategoryInterestDto(genre = MovieGenre.ACTION, interestCount = 1)
        interestDao.insertInterest(initial)

        val updated = LocalMovieCategoryInterestDto(genre = MovieGenre.ACTION, interestCount = 5)

        // When
        interestDao.insertInterest(updated)

        // Then
        val stored = interestDao.getAllInterests()
        assertThat(stored).containsExactly(updated)
    }

    @Test
    fun getInterestCount_shouldReturnCorrectCount() = runTest {
        // Given
        interestDao.insertInterest(LocalMovieCategoryInterestDto(genre = MovieGenre.COMEDY, interestCount = 4))

        // When
        val count = interestDao.getInterestCount(MovieGenre.COMEDY)

        // Then
        assertThat(count).isEqualTo(4)
    }

    @Test
    fun getInterestCount_shouldReturnNull_whenNotStored() = runTest {
        // When
        val count = interestDao.getInterestCount(MovieGenre.FANTASY)

        // Then
        assertThat(count).isNull()
    }

    @Test
    fun incrementInterest_shouldAddNewRecord_ifNotExist() = runTest {
        // When
        interestDao.incrementInterest(MovieGenre.DOCUMENTARY)

        // Then
        val stored = interestDao.getAllInterests()
        assertThat(stored).containsExactly(
            LocalMovieCategoryInterestDto(genre = MovieGenre.DOCUMENTARY, interestCount = 1)
        )
    }

    @Test
    fun incrementInterest_shouldIncrementExistingRecord() = runTest {
        // Given
        interestDao.insertInterest(LocalMovieCategoryInterestDto(MovieGenre.WAR, 2))

        // When
        interestDao.incrementInterest(MovieGenre.WAR)

        // Then
        val count = interestDao.getInterestCount(MovieGenre.WAR)
        assertThat(count).isEqualTo(3)
    }

    @Test
    fun getAllInterests_shouldReturnAllInsertedItems() = runTest {
        // Given
        val list = listOf(
            LocalMovieCategoryInterestDto(MovieGenre.ACTION, 1),
            LocalMovieCategoryInterestDto(MovieGenre.HORROR, 3)
        )
        list.forEach { interestDao.insertInterest(it) }

        // When
        val stored = interestDao.getAllInterests()

        // Then
        assertThat(stored).containsExactlyElementsIn(list)
    }
}
