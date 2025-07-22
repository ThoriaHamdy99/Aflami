package com.example.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.localdatasource.roomDataBase.AflamiDatabase
import com.example.localdatasource.roomDataBase.daos.CategoryDao
import com.example.localdatasource.utils.createLocalMovieCategoryDto
import com.example.localdatasource.utils.createLocalTvShowCategoryDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CategoryDaoTest {

    private lateinit var aflamiDatabase: AflamiDatabase
    private lateinit var categoryDao: CategoryDao

    @BeforeEach
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        aflamiDatabase = Room.inMemoryDatabaseBuilder(appContext, AflamiDatabase::class.java).build()
        categoryDao = aflamiDatabase.categoryDao()
    }

    @AfterEach
    fun tearDown() {
        aflamiDatabase.close()
    }

    @Test
    fun upsertAllMovieCategories_shouldInsertMovieCategories_whenCalled() = runTest {
        // Arrange
        val categories = listOf(
            createLocalMovieCategoryDto(categoryId = 1, name = "A"),
            createLocalMovieCategoryDto(categoryId = 2, name = "B")
        )

        // Act
        categoryDao.upsertAllMovieCategories(categories)

        // Assert
        val stored = categoryDao.getAllMovieCategories()
        assertThat(stored).isEqualTo(categories)
    }

    @Test
    fun upsertAllMovieCategories_shouldUpdateMovieCategories_whenAlreadyStored() = runTest {
        // Arrange
        val initial = listOf(
            createLocalMovieCategoryDto(categoryId = 1, name = "Action", storedLanguage = "en"),
            createLocalMovieCategoryDto(categoryId = 2, name = "Drama", storedLanguage = "en")
        )
        categoryDao.upsertAllMovieCategories(initial)

        val updated = listOf(
            createLocalMovieCategoryDto(categoryId = 1, name = "Adventure", storedLanguage = "en"),
            createLocalMovieCategoryDto(categoryId = 2, name = "Comedy", storedLanguage = "en")
        )

        // Act
        categoryDao.upsertAllMovieCategories(updated)

        // Assert
        val stored = categoryDao.getAllMovieCategories()
        assertThat(stored).isEqualTo(updated)
    }

    @Test
    fun getAllMovieCategories_shouldReturnEmptyList_whenNoDataInserted() = runTest {
        // Act
        val result = categoryDao.getAllMovieCategories()

        // Assert
        assertThat(result).isEmpty()
    }

    @Test
    fun upsertAllMovieCategories_shouldReplaceDuplicateCategory_whenSamePrimaryKeyExists() = runTest {
        // Arrange
        val original = listOf(
            createLocalMovieCategoryDto(categoryId = 5, name = "Sci-Fi", storedLanguage = "en")
        )
        categoryDao.upsertAllMovieCategories(original)

        val updated = listOf(
            createLocalMovieCategoryDto(categoryId = 5, name = "Fantasy", storedLanguage = "en")
        )

        // Act
        categoryDao.upsertAllMovieCategories(updated)

        // Assert
        val stored = categoryDao.getAllMovieCategories()
        assertThat(stored).containsExactly(updated.first())
    }

    @Test
    fun upsertAllMovieCategories_shouldInsertOnlyOne_whenDuplicateNameExists() = runTest {
        // Arrange
        val categories = listOf(
            createLocalMovieCategoryDto(categoryId = 1, name = "Old", storedLanguage = "en"),
            createLocalMovieCategoryDto(categoryId = 2, name = "Old", storedLanguage = "en")
        )

        // Act
        categoryDao.upsertAllMovieCategories(categories)

        // Assert
        val stored = categoryDao.getAllMovieCategories()
        assertThat(stored).containsExactly(categories.first())
    }

    @Test
    fun upsertAllTvShowCategories_shouldInsertTvShowCategories_whenCalled() = runTest {
        // Arrange
        val categories = listOf(
            createLocalTvShowCategoryDto(categoryId = 1, name = "Popular"),
            createLocalTvShowCategoryDto(categoryId = 2, name = "Trending")
        )

        // Act
        categoryDao.upsertAllTvShowCategories(categories)

        // Assert
        val stored = categoryDao.getAllTvShowCategories()
        assertThat(stored).isEqualTo(categories)
    }

    @Test
    fun upsertAllTvShowCategories_shouldInsertOnlyOne_whenDuplicateNameExists() = runTest {
        // Arrange
        val categories = listOf(
            createLocalTvShowCategoryDto(categoryId = 1, name = "Old", storedLanguage = "en"),
            createLocalTvShowCategoryDto(categoryId = 2, name = "Old", storedLanguage = "en")
        )

        // Act
        categoryDao.upsertAllTvShowCategories(categories)

        // Assert
        val stored = categoryDao.getAllTvShowCategories()
        assertThat(stored).containsExactly(categories.first())
    }

    @Test
    fun getAllTvShowCategories_shouldReturnEmptyList_whenNoDataInserted() = runTest {
        // Act
        val result = categoryDao.getAllTvShowCategories()

        // Assert
        assertThat(result).isEmpty()
    }

    @Test
    fun upsertAllTvShowCategories_shouldReplaceDuplicateCategory_whenSamePrimaryKeyExists() = runTest {
        // Arrange
        val original = listOf(
            createLocalTvShowCategoryDto(categoryId = 10, name = "Original", storedLanguage = "en")
        )
        categoryDao.upsertAllTvShowCategories(original)

        val updated = listOf(
            createLocalTvShowCategoryDto(categoryId = 10, name = "Updated", storedLanguage = "en")
        )

        // Act
        categoryDao.upsertAllTvShowCategories(updated)

        // Assert
        val stored = categoryDao.getAllTvShowCategories()
        assertThat(stored).containsExactly(updated.first())
    }
}
