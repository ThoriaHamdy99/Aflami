package com.amsterdam.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.amsterdam.localdatasource.roomDataBase.AflamiDatabase
import com.amsterdam.localdatasource.roomDataBase.daos.CountryDao
import com.amsterdam.repository.dto.local.LocalCountryDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CountryDaoTest {
    private lateinit var countryDao: CountryDao
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }
    private val database by lazy {
        Room.inMemoryDatabaseBuilder(context, AflamiDatabase::class.java).build()
    }

    @BeforeEach
    fun setup() {
        countryDao = database.countryDao()
    }

    @AfterEach
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsertAllCountries_shouldAddLocalCountryDtoList_whenCalled() = runTest {
        countryDao.upsertAllCountries(countriesWithSameLanguage)
        val stored = countryDao.getAllCountries(countriesWithSameLanguage.first().storedLanguage)

        assertThat(stored).isEqualTo(countriesWithSameLanguage)
    }

    @Test
    fun upsertAllCountries_shouldUpdateItem_whenPrimaryKeyExists() = runTest {
        countryDao.upsertAllCountries(initialCountries)
        countryDao.upsertAllCountries(updatedCountries)
        val stored = countryDao.getAllCountries(initialCountries.first().storedLanguage)

        assertThat(stored.first().name).isEqualTo(updatedCountries.first().name)
    }

    @Test
    fun getAllCountries_shouldReturnOnlyMatchingLanguageEntries() = runTest {
        countryDao.upsertAllCountries(countriesWithDifferentLanguage)

        val resultEn = countryDao.getAllCountries(countriesWithDifferentLanguage[0].storedLanguage)

        assertThat(resultEn).containsExactly(countriesWithDifferentLanguage[0])
    }

    @Test
    fun getAllCountries_shouldReturnEmpty_whenNoDataMatchesLanguage() = runTest {
        countryDao.upsertAllCountries(initialCountries)

        val result = countryDao.getAllCountries("fr")

        assertThat(result).isEmpty()
    }

    @Test
    fun getAllCountries_shouldReturnEmpty_whenNoDataInserted() = runTest {
        val result = countryDao.getAllCountries("en")

        assertThat(result).isEmpty()
    }
}

private val countriesWithSameLanguage = listOf(
    LocalCountryDto(isoCode = "US", storedLanguage = "en", name = "United States"),
    LocalCountryDto(isoCode = "FR", storedLanguage = "en", name = "France")
)

private val initialCountries = listOf(
    LocalCountryDto(isoCode = "US", storedLanguage = "en", name = "United States")
)

private val updatedCountries = listOf(
    LocalCountryDto(isoCode = "US", storedLanguage = "en", name = "USA")
)

private val countriesWithDifferentLanguage = listOf(
    LocalCountryDto(isoCode = "US", storedLanguage = "en", name = "USA"),
    LocalCountryDto(isoCode = "EG", storedLanguage = "ar", name = "مصر")
)