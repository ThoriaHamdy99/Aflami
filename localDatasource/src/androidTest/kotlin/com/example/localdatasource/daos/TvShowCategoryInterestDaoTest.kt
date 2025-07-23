package com.example.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre
import com.example.localdatasource.roomDataBase.AflamiDatabase
import com.example.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.example.repository.dto.local.LocalMovieCategoryInterestDto
import com.example.repository.dto.local.LocalTvShowCategoryInterestDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TvShowCategoryInterestDaoTest {

    private lateinit var database: AflamiDatabase
    private lateinit var interestDao: TvShowCategoryInterestDao

    @BeforeEach
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AflamiDatabase::class.java).build()
        interestDao = database.tvShowCategoryInterestDao()
    }

    @AfterEach
    fun tearDown() {
        database.close()
    }


    @Test
    fun insertInterest_shouldAddNewInterest() = runTest {
        // Given
        val dto = LocalTvShowCategoryInterestDto(genre = TvShowGenre.DRAMA, interestCount = 1)

        //When
        interestDao.insertInterest(dto)

        //Then
        val stored = interestDao.getAllInterests()
        assertThat(stored).containsExactly(dto)
    }

    @Test
    fun insertInterest_shouldUpdateExistingInterest() = runTest {
        // Given
        val initial = LocalTvShowCategoryInterestDto(genre = TvShowGenre.DRAMA, interestCount = 1)
        interestDao.insertInterest(initial)

        val updated = LocalTvShowCategoryInterestDto(genre = TvShowGenre.DRAMA, interestCount = 5)

        // When
        interestDao.insertInterest(updated)

        // Then
        val stored = interestDao.getAllInterests()
        assertThat(stored).containsExactly(updated)
    }

    @Test
    fun getInterestCount_shouldReturnCorrectCount() = runTest {
        // Given
        interestDao.insertInterest(LocalTvShowCategoryInterestDto(genre = TvShowGenre.KIDS, interestCount = 3))

        // When
        val count = interestDao.getInterestCount(TvShowGenre.KIDS)

        // Then
        assertThat(count).isEqualTo(3)
    }

    @Test
    fun getInterestCount_shouldReturnNull_whenNotStored() = runTest {
        // When
        val count = interestDao.getInterestCount(TvShowGenre.MYSTERY)

        // Then
        assertThat(count).isNull()
    }

    @Test
    fun incrementInterest_shouldAddNewRecord_ifNotExist() = runTest {
        // When
        interestDao.incrementInterest(TvShowGenre.KIDS)

        // Then
        val stored = interestDao.getAllInterests()
        assertThat(stored).containsExactly(
            LocalTvShowCategoryInterestDto(genre = TvShowGenre.KIDS, interestCount = 1)
        )
    }

    @Test
    fun incrementInterest_shouldIncrementExistingRecord() = runTest {
        // Given
        interestDao.insertInterest(LocalTvShowCategoryInterestDto(TvShowGenre.DOCUMENTARY, 2))

        // When
        interestDao.incrementInterest(TvShowGenre.DOCUMENTARY)

        // Then
        val count = interestDao.getInterestCount(TvShowGenre.DOCUMENTARY)
        assertThat(count).isEqualTo(3)
    }

    @Test
    fun getAllInterests_shouldReturnAllInsertedItems() = runTest {
        // Given
        val list = listOf(
            LocalTvShowCategoryInterestDto(TvShowGenre.ANIMATION, 1),
            LocalTvShowCategoryInterestDto(TvShowGenre.COMEDY, 2)
        )
        list.forEach { interestDao.insertInterest(it) }

        // When
        val stored = interestDao.getAllInterests()

        // Then
        assertThat(stored).containsExactlyElementsIn(list)

    }
}
