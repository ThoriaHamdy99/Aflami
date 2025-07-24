package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.CountryDao
import com.google.common.truth.Truth.assertThat
import com.example.repository.dto.local.LocalCountryDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CountryLocalDataSourceImplTest {
    private lateinit var dao: CountryDao
    private lateinit var countryLocalDataSourceImpl: CountryLocalDataSourceImpl

    @BeforeEach
    fun setup() {
        dao = mockk(relaxed = true)
        countryLocalDataSourceImpl = CountryLocalDataSourceImpl(dao)
    }

    @Test
    fun `getCountries should return data from dao`() = runTest {
        //Given
        val expected = listOf(LocalCountryDto("1", "Egypt","en"), LocalCountryDto("2", "Palestine","en"))
        //When
        coEvery { dao.getAllCountries("en") } returns expected
        val result = countryLocalDataSourceImpl.getCountries("en")
        //Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getCountries should return emptyList from dao`() = runTest {
        //Given
        val expected = emptyList<LocalCountryDto>()
        //When
        coEvery { dao.getAllCountries("en") } returns expected
        val result = countryLocalDataSourceImpl.getCountries("en")
        //Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `addCountries should call dao with correct data`() = runTest {
        //Given
        val countries = listOf(LocalCountryDto("1", "Egypt","en"), LocalCountryDto("2", "Palestine","en"))
        //When
        countryLocalDataSourceImpl.addCountries(countries)
        //Then
        coVerify (exactly = 1) { dao.upsertAllCountries(countries) }

    }
}