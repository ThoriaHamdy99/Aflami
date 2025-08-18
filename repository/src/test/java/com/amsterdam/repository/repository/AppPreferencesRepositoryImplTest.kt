package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.utils.RestrictionLevel
import com.amsterdam.repository.datasource.local.AppLocalPreferences
import com.amsterdam.repository.mapper.toLocalDto
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AppPreferencesRepositoryImplTest {

    private lateinit var repository: AppPreferencesRepository
    private val preferences: AppLocalPreferences = mockk()

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        repository = AppPreferencesRepositoryImpl(preferences)
    }

    @Test
    fun `getAppLanguage should return flow from preferences`() = runTest {
        val expectedLanguage = "en"
        every { preferences.getAppLanguage() } returns flowOf(expectedLanguage)

        val result = repository.getAppLanguage().first()

        assertThat(result).isEqualTo(expectedLanguage)
        coVerify(exactly = 1) { preferences.getAppLanguage() }
    }

    @Test
    fun `setAppLanguage should call preferences to set language`() = runTest {
        val language = "es"
        coJustRun { preferences.setAppLanguage(language) }

        repository.setAppLanguage(language)

        coVerify(exactly = 1) { preferences.setAppLanguage(language) }
    }

    @Test
    fun `getAppTheme should return flow from preferences`() = runTest {
        val expectedTheme = true
        every { preferences.getAppTheme() } returns flowOf(expectedTheme)

        val result = repository.getAppTheme().first()

        assertThat(result).isEqualTo(expectedTheme)
        coVerify(exactly = 1) { preferences.getAppTheme() }
    }

    @Test
    fun `setAppTheme should call preferences to set theme`() = runTest {
        val isDarkTheme = true
        coJustRun { preferences.setAppTheme(isDarkTheme) }

        repository.setAppTheme(isDarkTheme)

        coVerify(exactly = 1) { preferences.setAppTheme(isDarkTheme) }
    }

    @Test
    fun `isOnboardingCompleted should return boolean from preferences`() = runTest {
        val isCompleted = true
        coEvery { preferences.isOnboardingCompleted() } returns isCompleted

        val result = repository.isOnboardingCompleted()

        assertThat(result).isEqualTo(isCompleted)
        coVerify(exactly = 1) { preferences.isOnboardingCompleted() }
    }

    @Test
    fun `setOnboardingCompleted should call preferences to set completion state`() = runTest {
        val isCompleted = true
        coJustRun { preferences.setOnboardingCompleted(isCompleted) }

        repository.setOnboardingCompleted(isCompleted)

        coVerify(exactly = 1) { preferences.setOnboardingCompleted(isCompleted) }
    }

    @Test
    fun `getRestrictionLevel should return mapped restriction level from preferences`() = runTest {
        val localDto = "OFF"
        val expectedLevel = RestrictionLevel.OFF
        every { preferences.getRestrictionLevel() } returns flowOf(localDto)

        val result = repository.getRestrictionLevel().first()

        assertThat(result).isEqualTo(expectedLevel)
        coVerify(exactly = 1) { preferences.getRestrictionLevel() }
    }

    @Test
    fun `setRestrictionLevel should call preferences with mapped DTO`() = runTest {
        val restrictionLevel = RestrictionLevel.STRICT
        val localDto = restrictionLevel.toLocalDto()
        coJustRun { preferences.setRestrictionLevel(localDto) }

        repository.setRestrictionLevel(restrictionLevel)

        coVerify(exactly = 1) { preferences.setRestrictionLevel(localDto) }
    }
}
