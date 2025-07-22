package com.example.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.entity.category.MovieGenre
import com.example.localdatasource.roomDataBase.AflamiDatabase
import com.example.localdatasource.roomDataBase.daos.MovieCategoryInterestDao
import com.example.repository.dto.local.LocalMovieCategoryInterestDto
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
        // Arrange
        val dto = LocalMovieCategoryInterestDto(genre = MovieGenre.ACTION, interestCount = 1)

        // Act
        interestDao.insertInterest(dto)

        // Assert
        val stored = interestDao.getAllInterests()
        assertThat(stored).containsExactly(dto)
    }

    @Test
    fun insertInterest_shouldUpdateExistingInterest() = runTest {
        // Arrange
        val initial = LocalMovieCategoryInterestDto(genre = MovieGenre.ACTION, interestCount = 1)
        interestDao.insertInterest(initial)

        val updated = LocalMovieCategoryInterestDto(genre = MovieGenre.ACTION, interestCount = 5)

        // Act
        interestDao.insertInterest(updated)

        // Assert
        val stored = interestDao.getAllInterests()
        assertThat(stored).containsExactly(updated)
    }

    @Test
    fun getInterestCount_shouldReturnCorrectCount() = runTest {
        // Arrange
        interestDao.insertInterest(LocalMovieCategoryInterestDto(genre = MovieGenre.COMEDY, interestCount = 4))

        // Act
        val count = interestDao.getInterestCount(MovieGenre.COMEDY)

        // Assert
        assertThat(count).isEqualTo(4)
    }

    @Test
    fun getInterestCount_shouldReturnNull_whenNotStored() = runTest {
        // Act
        val count = interestDao.getInterestCount(MovieGenre.FANTASY)

        // Assert
        assertThat(count).isNull()
    }

    @Test
    fun incrementInterest_shouldAddNewRecord_ifNotExist() = runTest {
        // Act
        interestDao.incrementInterest(MovieGenre.DOCUMENTARY)

        // Assert
        val stored = interestDao.getAllInterests()
        assertThat(stored).containsExactly(
            LocalMovieCategoryInterestDto(genre = MovieGenre.DOCUMENTARY, interestCount = 1)
        )
    }

    @Test
    fun incrementInterest_shouldIncrementExistingRecord() = runTest {
        // Arrange
        interestDao.insertInterest(LocalMovieCategoryInterestDto(MovieGenre.WAR, 2))

        // Act
        interestDao.incrementInterest(MovieGenre.WAR)

        // Assert
        val count = interestDao.getInterestCount(MovieGenre.WAR)
        assertThat(count).isEqualTo(3)
    }

    @Test
    fun getAllInterests_shouldReturnAllInsertedItems() = runTest {
        // Arrange
        val list = listOf(
            LocalMovieCategoryInterestDto(MovieGenre.ACTION, 1),
            LocalMovieCategoryInterestDto(MovieGenre.HORROR, 3)
        )
        list.forEach { interestDao.insertInterest(it) }

        // Act
        val stored = interestDao.getAllInterests()

        // Assert
        assertThat(stored).containsExactlyElementsIn(list)
    }
}
