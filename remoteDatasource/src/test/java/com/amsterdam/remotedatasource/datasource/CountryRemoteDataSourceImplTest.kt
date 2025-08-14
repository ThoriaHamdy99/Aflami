package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.CountryApiService
import com.amsterdam.repository.dto.remote.CountryRemoteDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CountryRemoteDataSourceImplTest {

    private lateinit var countryApiService: CountryApiService
    private lateinit var countryRemoteDataSourceImpl: CountryRemoteDataSourceImpl

    @BeforeEach
    fun setUp() {
        countryApiService = mockk()
        countryRemoteDataSourceImpl = CountryRemoteDataSourceImpl(countryApiService)
    }

    @Test
    fun `getCountries should call CountryApiService`() = runTest {
        coEvery { countryApiService.getCountries() } returns emptyList()

        countryRemoteDataSourceImpl.getCountries()

        coVerify(exactly = 1) { countryApiService.getCountries() }
    }

    @Test
    fun `getCountries should return the correct data from CountryApiService`() = runTest {
        coEvery { countryApiService.getCountries() } returns expectedCountryList

        val actualResponse = countryRemoteDataSourceImpl.getCountries()

        assertThat(actualResponse).isEqualTo(expectedCountryList)
        coVerify(exactly = 1) { countryApiService.getCountries() }
    }

    @Test
    fun `getCountries should throw NetworkException when API service throws an exception`() =
        runTest {
            coEvery { countryApiService.getCountries() } throws NetworkException()

            assertThrows<NetworkException> {
                countryRemoteDataSourceImpl.getCountries()
            }
            coVerify(exactly = 1) { countryApiService.getCountries() }
        }

    @Test
    fun `getCountries should return an empty list when API service returns an empty list`() =
        runTest {
            val expectedResponse = emptyList<CountryRemoteDto>()
            coEvery { countryApiService.getCountries() } returns expectedResponse

            val actualResponse = countryRemoteDataSourceImpl.getCountries()

            assertThat(actualResponse).isEmpty()
            coVerify(exactly = 1) { countryApiService.getCountries() }
        }

    @Test
    fun `getCountries should rethrow NetworkException when API service throws Exception`() =
        runTest {
            coEvery { countryApiService.getCountries() } throws NetworkException()

            assertThrows<NetworkException> {
                countryRemoteDataSourceImpl.getCountries()
            }
            coVerify(exactly = 1) { countryApiService.getCountries() }
        }

    private val expectedCountryList = listOf(
        CountryRemoteDto(
            englishName = "United States of America",
            isoCode = "US",
            nativeName = "United States"
        ),
        CountryRemoteDto(englishName = "Canada", isoCode = "CA", nativeName = "Canada")
    )
}