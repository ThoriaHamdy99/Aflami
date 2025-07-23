package com.example.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.localdatasource.roomDataBase.AflamiDatabase
import com.example.localdatasource.roomDataBase.daos.CountryDao
import com.example.repository.dto.local.LocalCountryDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CountryDaoTest {
    private lateinit var database: AflamiDatabase
    private lateinit var countryDao: CountryDao

    @BeforeEach
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AflamiDatabase::class.java).build()
        countryDao = database.countryDao()
    }

    @AfterEach
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsertAllCountries_shouldAddLocalCountryDtoList_whenCalled() = runTest {
        // Given
        val countries = listOf(
            LocalCountryDto(isoCode = "US", storedLanguage = "en", name = "United States"),
            LocalCountryDto(isoCode = "FR", storedLanguage = "en", name = "France")
        )

        // When
        countryDao.upsertAllCountries(countries)

        // Then
        val stored = countryDao.getAllCountries("en")
        assertThat(stored).isEqualTo(countries)
    }

    @Test
    fun upsertAllCountries_shouldUpdateItem_whenPrimaryKeyExists() = runTest {
        // Given
        val initial = listOf(
            LocalCountryDto(isoCode = "US", storedLanguage = "en", name = "United States")
        )
        countryDao.upsertAllCountries(initial)

        val updated = listOf(
            LocalCountryDto(isoCode = "US", storedLanguage = "en", name = "USA")
        )

        // When
        countryDao.upsertAllCountries(updated)

        // Then
        val stored = countryDao.getAllCountries("en")
        assertThat(stored).containsExactly(updated.first())
    }

    @Test
    fun getAllCountries_shouldReturnOnlyMatchingLanguageEntries() = runTest {
        // Given
        val countries = listOf(
            LocalCountryDto(isoCode = "US", storedLanguage = "en", name = "USA"),
            LocalCountryDto(isoCode = "EG", storedLanguage = "ar", name = "مصر")
        )
        countryDao.upsertAllCountries(countries)

        // When
        val resultEn = countryDao.getAllCountries("en")
        val resultAr = countryDao.getAllCountries("ar")

        // Then
        assertThat(resultEn).containsExactly(
            LocalCountryDto(isoCode = "US", storedLanguage = "en", name = "USA")
        )
        assertThat(resultAr).containsExactly(
            LocalCountryDto(isoCode = "EG", storedLanguage = "ar", name = "مصر")
        )
    }

    @Test
    fun getAllCountries_shouldReturnEmpty_whenNoDataMatchesLanguage() = runTest {
        // Given
        val countries = listOf(
            LocalCountryDto(isoCode = "US", storedLanguage = "en", name = "USA")
        )
        countryDao.upsertAllCountries(countries)

        // When
        val result = countryDao.getAllCountries("fr")

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun getAllCountries_shouldReturnEmpty_whenNoDataInserted() = runTest {
        // When
        val result = countryDao.getAllCountries("en")

        // Then
        assertThat(result).isEmpty()
    }
}
