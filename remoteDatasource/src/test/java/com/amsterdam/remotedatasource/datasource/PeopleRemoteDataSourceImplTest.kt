package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.PeopleApiService
import com.amsterdam.repository.dto.remote.RemotePeopleItemDto
import com.amsterdam.repository.dto.remote.RemotePeopleResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PeopleRemoteDataSourceImplTest {

    private val peopleApiService: PeopleApiService = mockk()
    private val peopleRemoteDataSourceImpl: PeopleRemoteDataSourceImpl =
        PeopleRemoteDataSourceImpl(peopleApiService)

    private val networkException = NetworkException()

    // Shared data and helper functions
    private val trendingPeopleResponse = RemotePeopleResponse(
        page = 1,
        totalPages = 2,
        totalResults = 4,
        results = listOf(
            createPeopleItemDto(id = 1, name = "Person A", profilePath = "path/a.jpg"),
            createPeopleItemDto(id = 2, name = "Person B", profilePath = "path/b.jpg")
        )
    )

    private val emptyPeopleResponse = RemotePeopleResponse(
        page = 1,
        totalPages = 1,
        totalResults = 0,
        results = emptyList()
    )

    private fun createPeopleItemDto(
        id: Int,
        name: String,
        profilePath: String?,
    ) = RemotePeopleItemDto(
        id = id,
        name = name,
        profilePath = profilePath,
        adult = false,
        gender = 1,
        popularity = 5.0,
        mediaType = "person",
        originalName = name,
        knownForDepartment = "Acting"
    )

    private fun createMockPeopleResponse(
        totalPages: Int,
        results: List<RemotePeopleItemDto>
    ): RemotePeopleResponse {
        return mockk {
            coEvery { this@mockk.totalPages } returns totalPages
            coEvery { this@mockk.results } returns results
        }
    }

    private val peopleForRandomizedTest = listOf(
        createPeopleItemDto(id = 1, name = "Tom", profilePath = "path1"),
        createPeopleItemDto(id = 2, name = "Jerry", profilePath = "path2")
    )

    private val peopleWithDuplicatesAndIncompleteData = listOf(
        createPeopleItemDto(id = 1, name = "Tom", profilePath = "path1"),
        createPeopleItemDto(id = 2, name = "", profilePath = "path2"),
        createPeopleItemDto(id = 3, name = "Jerry", profilePath = null),
        createPeopleItemDto(id = 4, name = "Spike", profilePath = "path3")
    )

    private val peopleWithExtraData = listOf(
        createPeopleItemDto(id = 1, name = "Tom", profilePath = "path1"),
        createPeopleItemDto(id = 2, name = "Jerry", profilePath = "path2"),
        createPeopleItemDto(id = 3, name = "Spike", profilePath = "path3")
    )

    private val peopleWithDuplicateId = listOf(
        createPeopleItemDto(id = 1, name = "Tom", profilePath = "path1"),
        createPeopleItemDto(id = 1, name = "Tom", profilePath = "path1"),
        createPeopleItemDto(id = 2, name = "Jerry", profilePath = "path2")
    )

    private val peopleWithIncompleteData = listOf(
        createPeopleItemDto(id = 1, name = "", profilePath = "path1"),
        createPeopleItemDto(id = 2, name = "Jerry", profilePath = null)
    )

    @Test
    fun `getTrendingPeople should return a list of people on successful API call`() = runTest {
        coEvery { peopleApiService.getTrendingPeople(any()) } returns trendingPeopleResponse
        val people = peopleRemoteDataSourceImpl.getTrendingPeople(page = 1)
        assertThat(people).isEqualTo(trendingPeopleResponse)
    }

    @Test
    fun `getTrendingPeople should call getTrendingPeople exactly once on a successful API call`() =
        runTest {
            coEvery { peopleApiService.getTrendingPeople(any()) } returns trendingPeopleResponse
            peopleRemoteDataSourceImpl.getTrendingPeople(page = 1)
            coVerify(exactly = 1) { peopleApiService.getTrendingPeople(any()) }
        }

    @Test
    fun `getTrendingPeople should rethrow a NetworkException when the service provider throws one`() =
        runTest {
            coEvery { peopleApiService.getTrendingPeople(any()) } throws networkException
            assertThrows<NetworkException> { peopleRemoteDataSourceImpl.getTrendingPeople(page = 1) }
        }

    @Test
    fun `getTrendingPeople should call the API exactly once when it throws a NetworkException`() = runTest {
        coEvery { peopleApiService.getTrendingPeople(any()) } throws networkException
        try {
            peopleRemoteDataSourceImpl.getTrendingPeople(page = 1)
        } catch (e: NetworkException) {
        }
        coVerify(exactly = 1) { peopleApiService.getTrendingPeople(any()) }
    }

    @Test
    fun `getTrendingPeople should return an empty list when the API service returns an empty list`() =
        runTest {
            coEvery { peopleApiService.getTrendingPeople(any()) } returns emptyPeopleResponse
            val people = peopleRemoteDataSourceImpl.getTrendingPeople(page = 1)
            assertThat(people.results).isEmpty()
        }

    @Test
    fun `getTrendingPeople should call the API exactly once when it returns an empty list`() = runTest {
        coEvery { peopleApiService.getTrendingPeople(any()) } returns emptyPeopleResponse
        peopleRemoteDataSourceImpl.getTrendingPeople(page = 1)
        coVerify(exactly = 1) { peopleApiService.getTrendingPeople(any()) }
    }

    @Test
    fun `getRandomizedTrendingPeople should return the required number of people`() = runTest {
        val requiredNumber = 3
        coEvery { peopleApiService.getTrendingPeople(page = 1) } returns createMockPeopleResponse(
            totalPages = 2, results = peopleForRandomizedTest
        )
        coEvery { peopleApiService.getTrendingPeople(page = 2) } returns createMockPeopleResponse(
            totalPages = 2, results = listOf(
                createPeopleItemDto(id = 3, name = "Spike", profilePath = "path3"),
                createPeopleItemDto(id = 4, name = "Tyke", profilePath = "path4")
            )
        )

        val people = peopleRemoteDataSourceImpl.getRandomizedTrendingPeople(requiredNumber)

        assertThat(people.size).isEqualTo(requiredNumber)
    }

    @Test
    fun `getRandomizedTrendingPeople should call the API multiple times (at least twice) to get enough people`() =
        runTest {
            val requiredNumber = 3
            coEvery { peopleApiService.getTrendingPeople(page = 1) } returns createMockPeopleResponse(
                totalPages = 2, results = peopleForRandomizedTest
            )
            coEvery { peopleApiService.getTrendingPeople(page = 2) } returns createMockPeopleResponse(
                totalPages = 2, results = listOf(
                    createPeopleItemDto(id = 3, name = "Spike", profilePath = "path3")
                )
            )

            peopleRemoteDataSourceImpl.getRandomizedTrendingPeople(requiredNumber)

            coVerify(atLeast = 2) { peopleApiService.getTrendingPeople(page = any()) }
        }

    @Test
    fun `getRandomizedTrendingPeople should filter out people with incomplete data`() = runTest {
        val requiredNumber = 2
        coEvery { peopleApiService.getTrendingPeople(any()) } returns createMockPeopleResponse(
            totalPages = 1,
            results = peopleWithDuplicatesAndIncompleteData
        )

        val people = peopleRemoteDataSourceImpl.getRandomizedTrendingPeople(requiredNumber)

        assertThat(people.size).isEqualTo(requiredNumber)
        assertThat(people.any { it.name.isBlank() || it.profilePath.isNullOrBlank() }).isFalse()
    }

    @Test
    fun `getRandomizedTrendingPeople should not return more than the required number of people`() =
        runTest {
            val requiredNumber = 2
            coEvery { peopleApiService.getTrendingPeople(any()) } returns createMockPeopleResponse(
                totalPages = 1,
                results = peopleWithExtraData
            )

            val people = peopleRemoteDataSourceImpl.getRandomizedTrendingPeople(requiredNumber)

            assertThat(people.size).isEqualTo(requiredNumber)
        }

    @Test
    fun `getRandomizedTrendingPeople should handle duplicate people from different pages`() =
        runTest {
            val requiredNumber = 2
            coEvery { peopleApiService.getTrendingPeople(page = 1) } returns createMockPeopleResponse(
                totalPages = 2,
                results = listOf(createPeopleItemDto(id = 1, name = "Tom", profilePath = "path1"))
            )
            coEvery { peopleApiService.getTrendingPeople(page = 2) } returns createMockPeopleResponse(
                totalPages = 2, results = peopleWithDuplicateId
            )

            val people = peopleRemoteDataSourceImpl.getRandomizedTrendingPeople(requiredNumber)

            assertThat(people.size).isEqualTo(requiredNumber)
            val collectedIds = people.map { it.id }
            assertThat(collectedIds).containsNoDuplicates()
        }

    @Test
    fun `getRandomizedTrendingPeople should return an empty list when no high-quality people are found`() =
        runTest {
            val requiredNumber = 2
            coEvery { peopleApiService.getTrendingPeople(any()) } returns createMockPeopleResponse(
                totalPages = 1,
                results = peopleWithIncompleteData
            )

            val people = peopleRemoteDataSourceImpl.getRandomizedTrendingPeople(requiredNumber)

            assertThat(people).isEmpty()
        }
}