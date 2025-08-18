package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.repository.datasource.local.RecentSearchLocalDataSource
import com.amsterdam.repository.dto.local.SearchLocalDto
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RecentSearchRepositoryImplTest {

    private lateinit var repository: RecentSearchRepository

    private val recentSearchLocalDataSource: RecentSearchLocalDataSource = mockk()
    private val fixedCurrentTime = Instant.parse("2025-07-27T10:00:00Z")

    @BeforeEach
    fun setup() {
        clearAllMocks()
        mockkObject(Clock.System)
        every { Clock.System.now() } returns fixedCurrentTime
        repository = RecentSearchRepositoryImpl(
            recentSearchLocalDataSource = recentSearchLocalDataSource,
        )
    }

    @Test
    fun `getAllRecentSearches should return mapped BY_KEYWORD searches from local source`() =
        runTest {
            coEvery { recentSearchLocalDataSource.getRecentSearches() } returns fakeLocalSearches

            val result = repository.getAllRecentSearches()

            assertThat(result).isEqualTo(expectedKeywords)
            coVerify(exactly = 1) { recentSearchLocalDataSource.getRecentSearches() }
        }

    @Test
    fun `getAllRecentSearches should return empty list if no BY_KEYWORD searches exist locally`() =
        runTest {
            coEvery { recentSearchLocalDataSource.getRecentSearches() } returns emptyList()

            val result = repository.getAllRecentSearches()

            assertThat(result).isEmpty()
            coVerify(exactly = 1) { recentSearchLocalDataSource.getRecentSearches() }
        }

    @Test
    fun `deleteAllRecentSearches should call local source to delete all searches`() = runTest {
        coJustRun { recentSearchLocalDataSource.deleteRecentSearches() }

        repository.deleteAllRecentSearches()

        coVerify(exactly = 1) { recentSearchLocalDataSource.deleteRecentSearches() }
    }

    @Test
    fun `getAllRecentSearches should propagate exception if local source fails`() = runTest {
        coEvery { recentSearchLocalDataSource.getRecentSearches() } throws fakeDbReadException

        val thrownException = assertThrows<RuntimeException> {
            repository.getAllRecentSearches()
        }

        assertThat(thrownException).isEqualTo(fakeDbReadException)
        coVerify(exactly = 1) { recentSearchLocalDataSource.getRecentSearches() }
    }

    @Test
    fun `deleteAllRecentSearches should propagate exception if local source fails`() = runTest {
        coEvery { recentSearchLocalDataSource.deleteRecentSearches() } throws fakeDbDeleteException

        val thrownException = assertThrows<RuntimeException> {
            repository.deleteAllRecentSearches()
        }

        assertThat(thrownException).isEqualTo(fakeDbDeleteException)
        coVerify(exactly = 1) { recentSearchLocalDataSource.deleteRecentSearches() }
    }

    @Test
    fun `addRecentSearch should propagate exception if local source upsert fails`() = runTest {
        coEvery { recentSearchLocalDataSource.upsertRecentSearch(any()) } throws fakeUpsertFailedException

        val thrownException = assertThrows<RuntimeException> {
            repository.addRecentSearch(failingAddKeyword)
        }

        assertThat(thrownException).isEqualTo(fakeUpsertFailedException)
        coVerify(exactly = 1) { recentSearchLocalDataSource.upsertRecentSearch(any()) }
    }

    @Test
    fun `deleteRecentSearch should call local source to delete by keyword`() = runTest {
        coJustRun { recentSearchLocalDataSource.deleteRecentSearchByKeyword(keywordToDelete) }

        repository.deleteRecentSearch(keywordToDelete)

        coVerify(exactly = 1) { recentSearchLocalDataSource.deleteRecentSearchByKeyword(keywordToDelete) }
    }

    private val fakeLocalSearches = listOf(
        SearchLocalDto("movie1"),
        SearchLocalDto("movie2")
    )

    private val expectedKeywords = listOf("movie1", "movie2")

    private val keywordToDelete = "movie to delete"

    private val failingAddKeyword = "failing add"

    private val fakeDbReadException = RuntimeException("DB read error!")
    private val fakeDbDeleteException = RuntimeException("DB delete error!")
    private val fakeUpsertFailedException = RuntimeException("Upsert failed!")
}