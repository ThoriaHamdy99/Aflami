package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.CategoryApiService
import com.amsterdam.repository.dto.remote.CategoryRemoteDto
import com.amsterdam.repository.dto.remote.CategoryRemoteResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CategoryRemoteDataSourceImplTest {

    private lateinit var categoryApiService: CategoryApiService
    private lateinit var categoryRemoteDataSourceImpl: CategoryRemoteDataSourceImpl

    @BeforeEach
    fun setUp() {
        categoryApiService = mockk()
        categoryRemoteDataSourceImpl = CategoryRemoteDataSourceImpl(categoryApiService)
    }

    @Test
    fun `getMovieCategories should return a list of movie categories when successful`() = runTest {
        coEvery { categoryApiService.getMovieCategories() } returns movieCategoryResponse

        val categories = categoryRemoteDataSourceImpl.getMovieCategories()

        assertThat(categories).isEqualTo(movieCategoryResponse)
        coVerify(exactly = 1) { categoryApiService.getMovieCategories() }
    }

    @Test
    fun `getTvShowCategories should return a list of TV show categories when successful`() =
        runTest {
            coEvery { categoryApiService.getTvShowCategories() } returns tvShowCategoryResponse

            val categories = categoryRemoteDataSourceImpl.getTvShowCategories()

            assertThat(categories).isEqualTo(tvShowCategoryResponse)
            coVerify(exactly = 1) { categoryApiService.getTvShowCategories() }
        }

    @Test
    fun `getMovieCategories should rethrow NetworkException from service provider`() = runTest {
        coEvery { categoryApiService.getMovieCategories() } throws NetworkException()

        assertThrows<NetworkException> {
            categoryRemoteDataSourceImpl.getMovieCategories()
        }
        coVerify(exactly = 1) { categoryApiService.getMovieCategories() }
    }

    @Test
    fun `getTvShowCategories should rethrow NetworkException from service provider`() = runTest {
        coEvery { categoryApiService.getTvShowCategories() } throws NetworkException()

        assertThrows<NetworkException> {
            categoryRemoteDataSourceImpl.getTvShowCategories()
        }
        coVerify(exactly = 1) { categoryApiService.getTvShowCategories() }
    }

    @Test
    fun `getMovieCategories should return empty list when API service returns empty list`() =
        runTest {
            coEvery { categoryApiService.getMovieCategories() } returns emptyCategoryResponse

            val categories = categoryRemoteDataSourceImpl.getMovieCategories()

            assertThat(categories.genres).isEmpty()
            coVerify(exactly = 1) { categoryApiService.getMovieCategories() }
        }

    @Test
    fun `getTvShowCategories should return empty list when API service returns empty list`() =
        runTest {
            coEvery { categoryApiService.getTvShowCategories() } returns emptyCategoryResponse

            val categories = categoryRemoteDataSourceImpl.getTvShowCategories()

            assertThat(categories.genres).isEmpty()
            coVerify(exactly = 1) { categoryApiService.getTvShowCategories() }
        }

    private val movieCategoryResponse = CategoryRemoteResponse(
        genres = listOf(
            CategoryRemoteDto(id = 28, name = "Action"),
            CategoryRemoteDto(id = 12, name = "Adventure")
        )
    )

    private val tvShowCategoryResponse = CategoryRemoteResponse(
        genres = listOf(
            CategoryRemoteDto(id = 10759, name = "Action & Adventure"),
            CategoryRemoteDto(id = 16, name = "Animation")
        )
    )

    private val emptyCategoryResponse = CategoryRemoteResponse(genres = emptyList())
}