package com.amsterdam.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.amsterdam.localdatasource.roomDataBase.AflamiDatabase
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.amsterdam.repository.dto.local.LocalTvShowCategoryInterestDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TvShowCategoryInterestDaoTest {
    private lateinit var interestDao: TvShowCategoryInterestDao
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }
    private val database by lazy {
        Room.inMemoryDatabaseBuilder(context, AflamiDatabase::class.java).build()
    }

    @BeforeEach
    fun setup() {
        interestDao = database.tvShowCategoryInterestDao()
    }

    @AfterEach
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsertInterest_shouldAddNewInterest() = runTest {
        interestDao.upsertInterest(initialTvShowCategoryInterestDto)
        val stored = interestDao.getInterestCount(initialTvShowCategoryInterestDto.categoryId)

        assertThat(stored).isEqualTo(initialTvShowCategoryInterestDto.interestCount)
    }

    @Test
    fun upsertInterest_shouldUpdateExistingInterest() = runTest {
        interestDao.upsertInterest(initialTvShowCategoryInterestDto)
        interestDao.upsertInterest(updatedTvShowCategoryInterestDto)
        val stored = interestDao.getInterestCount(initialTvShowCategoryInterestDto.categoryId)

        assertThat(stored).isEqualTo(updatedTvShowCategoryInterestDto.interestCount)
    }

    @Test
    fun getInterestCount_shouldReturnCorrectCount() = runTest {
        interestDao.upsertInterest(initialTvShowCategoryInterestDto)

        val count = interestDao.getInterestCount(initialTvShowCategoryInterestDto.categoryId)

        assertThat(count).isEqualTo(initialTvShowCategoryInterestDto.interestCount)
    }

    @Test
    fun getInterestCount_shouldReturnNull_whenNotStored() = runTest {
        val count = interestDao.getInterestCount(1)

        assertThat(count).isNull()
    }

    @Test
    fun incrementInterest_shouldAddNewRecord_ifNotExist() = runTest {
        interestDao.incrementInterest(1)
        val stored = interestDao.getInterestCount(1)

        assertThat(stored).isEqualTo(1)
    }

    @Test
    fun incrementInterest_shouldIncrementExistingRecord() = runTest {
        interestDao.upsertInterest(initialTvShowCategoryInterestDto)

        interestDao.incrementInterest(initialTvShowCategoryInterestDto.categoryId)
        val count = interestDao.getInterestCount(initialTvShowCategoryInterestDto.categoryId)

        assertThat(count).isEqualTo(initialTvShowCategoryInterestDto.interestCount + 1)
    }
}

private val initialTvShowCategoryInterestDto = LocalTvShowCategoryInterestDto(1, interestCount = 1)

private val updatedTvShowCategoryInterestDto = LocalTvShowCategoryInterestDto(1, interestCount = 5)