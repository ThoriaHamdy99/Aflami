package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.entity.Country
import com.amsterdam.repository.datasource.local.AppLocalPreferences
import com.amsterdam.repository.datasource.local.CountryLocalDataSource
import com.amsterdam.repository.datasource.remote.CountryRemoteDataSource
import com.amsterdam.repository.dto.local.CountryLocalDto
import com.amsterdam.repository.dto.remote.CountryRemoteDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CountryRepositoryImplTest {

    private lateinit var repository: CountryRepository

    private val localDataSource: CountryLocalDataSource = mockk()
    private val remoteDataSource: CountryRemoteDataSource = mockk()
    private val preferences: AppLocalPreferences = mockk()

    @BeforeEach
    fun setup() {
        repository = CountryRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            preferences = preferences
        )
        coEvery { preferences.getAppLanguage() } returns flowOf(testLanguage)
    }

    @Test
    fun `getCountries should return local data when cache is available`() = runTest {
        coEvery { localDataSource.getCountries(testLanguage) } returns localCountryDtos

        val result = repository.getCountries()

        assertThat(result).isEqualTo(expectedLocalCountries)
        coVerify(exactly = 1) { localDataSource.getCountries(testLanguage) }
        coVerify(exactly = 0) { remoteDataSource.getCountries() }
        coVerify(exactly = 0) { localDataSource.upsertCountries(any()) }
    }

    @Test
    fun `getCountries should fetch from remote and cache when local is empty`() = runTest {
        coEvery { localDataSource.getCountries(testLanguage) } returns emptyList()
        coEvery { remoteDataSource.getCountries() } returns remoteCountryDtos
        coJustRun { localDataSource.upsertCountries(localDtosToSave) }

        val result = repository.getCountries()

        assertThat(result).isEqualTo(expectedCountriesFromRemote)
        coVerify(exactly = 1) { localDataSource.getCountries(testLanguage) }
        coVerify(exactly = 1) { remoteDataSource.getCountries() }
        coVerify(exactly = 1) { localDataSource.upsertCountries(localDtosToSave) }
    }

    @Test
    fun `getCountries should fallback to remote when local source throws exception`() = runTest {
        coEvery { localDataSource.getCountries(testLanguage) } throws localDbException
        coEvery { remoteDataSource.getCountries() } returns remoteCountryDtos
        coJustRun { localDataSource.upsertCountries(localDtosToSave) }

        val result = repository.getCountries()

        assertThat(result).isEqualTo(expectedCountriesFromRemote)
        coVerify(exactly = 1) { localDataSource.getCountries(testLanguage) }
        coVerify(exactly = 1) { remoteDataSource.getCountries() }
        coVerify(exactly = 1) { localDataSource.upsertCountries(localDtosToSave) }
    }

    @Test
    fun `getCountries should throw exception if remote fails and local is empty`() = runTest {
        coEvery { localDataSource.getCountries(testLanguage) } returns emptyList()
        coEvery { remoteDataSource.getCountries() } throws remoteNetworkException

        val thrownException = assertThrows<RuntimeException> {
            repository.getCountries()
        }

        assertThat(thrownException).isEqualTo(remoteNetworkException)
        coVerify(exactly = 0) { localDataSource.upsertCountries(any()) }
    }

    @Test
    fun `getCountries should throw exception if both local and remote sources fail`() = runTest {
        coEvery { localDataSource.getCountries(testLanguage) } throws localDbException
        coEvery { remoteDataSource.getCountries() } throws remoteNetworkException

        val thrownException = assertThrows<RuntimeException> {
            repository.getCountries()
        }

        assertThat(thrownException).isEqualTo(remoteNetworkException)
        coVerify(exactly = 0) { localDataSource.upsertCountries(any()) }
    }

    private val testLanguage = "en"

    private val localCountryDtos = listOf(
        CountryLocalDto(isoCode = "US", name = "United States", storedLanguage = testLanguage),
        CountryLocalDto(isoCode = "CA", name = "Canada", storedLanguage = testLanguage)
    )

    private val expectedLocalCountries = listOf(
        Country(countryName = "United States", countryIsoCode = "US"),
        Country(countryName = "Canada", countryIsoCode = "CA")
    )

    private val remoteCountryDtos = listOf(
        CountryRemoteDto(englishName = "United States", isoCode = "US", nativeName = "الولايات المتحدة")
    )

    private val localDtosToSave = listOf(
        CountryLocalDto(isoCode = "US", name = "الولايات المتحدة", storedLanguage = testLanguage)
    )

    private val expectedCountriesFromRemote = listOf(
        Country(countryName = "الولايات المتحدة", countryIsoCode = "US")
    )

    private val localDbException = RuntimeException("Local DB corrupted!")
    private val remoteNetworkException = RuntimeException("Network unavailable!")
}