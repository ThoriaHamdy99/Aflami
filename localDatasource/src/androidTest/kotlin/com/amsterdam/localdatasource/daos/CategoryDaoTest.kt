package com.amsterdam.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.amsterdam.localdatasource.roomDataBase.AflamiDatabase
import com.amsterdam.localdatasource.roomDataBase.daos.CategoryDao
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CategoryDaoTest {
    private lateinit var categoryDao: CategoryDao
    private val appContext by lazy { InstrumentationRegistry.getInstrumentation().targetContext }
    private val aflamiDatabase by lazy {
        Room.inMemoryDatabaseBuilder(appContext, AflamiDatabase::class.java).build()
    }

    @BeforeEach
    fun setup() {
        categoryDao = aflamiDatabase.categoryDao()
    }

    @AfterEach
    fun tearDown() {
        aflamiDatabase.close()
    }

    @Test
    fun upsertAllMovieCategories_shouldInsertMovieCategories_whenCalled() = runTest {
        categoryDao.upsertAllMovieCategories(moviesCategoriesWithDifferentIds)
        val stored = categoryDao.getAllMovieCategories()

        assertThat(stored).isEqualTo(moviesCategoriesWithDifferentIds)
    }

    @Test
    fun upsertAllMovieCategories_shouldInsertOnlyOne_whenDuplicateNameExists() = runTest {
        categoryDao.upsertAllMovieCategories(moviesCategoriesWithSameIds)
        val stored = categoryDao.getAllMovieCategories()

        assertThat(stored).containsExactly(moviesCategoriesWithSameIds.first())
    }

    @Test
    fun upsertAllTvShowCategories_shouldInsertTvShowCategories_whenCalled() = runTest {
        categoryDao.upsertAllTvShowCategories(tvShowCategoriesWithDifferentIds)
        val stored = categoryDao.getAllTvShowCategories()

        assertThat(stored).isEqualTo(tvShowCategoriesWithDifferentIds)
    }

    @Test
    fun upsertAllTvShowCategories_shouldInsertOnlyOne_whenDuplicateNameExists() = runTest {
        categoryDao.upsertAllTvShowCategories(tvShowCategoriesWithSameIds)
        val stored = categoryDao.getAllTvShowCategories()

        assertThat(stored).containsExactly(tvShowCategoriesWithSameIds.first())
    }

    @Test
    fun getAllMovieCategories_shouldReturnEmptyList_whenNoDataInserted() = runTest {
        val result = categoryDao.getAllMovieCategories()

        assertThat(result).isEmpty()
    }

    @Test
    fun getAllTvShowCategories_shouldReturnEmptyList_whenNoDataInserted() = runTest {
        val result = categoryDao.getAllTvShowCategories()

        assertThat(result).isEmpty()
    }
}

private val moviesCategoriesWithDifferentIds = listOf(
    createLocalMovieCategoryDto(categoryId = 1),
    createLocalMovieCategoryDto(categoryId = 2)
)

private val moviesCategoriesWithSameIds = listOf(
    createLocalMovieCategoryDto(categoryId = 1),
    createLocalMovieCategoryDto(categoryId = 1)
)

private val tvShowCategoriesWithDifferentIds = listOf(
    createLocalTvShowCategoryDto(categoryId = 1L),
    createLocalTvShowCategoryDto(categoryId = 2L)
)

private val tvShowCategoriesWithSameIds = listOf(
    createLocalTvShowCategoryDto(categoryId = 1),
    createLocalTvShowCategoryDto(categoryId = 1)
)

private fun createLocalMovieCategoryDto(
    categoryId: Long = 0L
): LocalMovieCategoryDto {
    return LocalMovieCategoryDto(
        categoryId = categoryId
    )
}

private fun createLocalTvShowCategoryDto(
    categoryId: Long = 0,
): LocalTvShowCategoryDto {
    return LocalTvShowCategoryDto(categoryId)
}