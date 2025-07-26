package com.amsterdam.remotedatasource

import com.amsterdam.remotedatasource.api.CategoryApiService
import com.amsterdam.remotedatasource.serviceProvider.implementation.CategoryServiceProviderImpl
import com.amsterdam.repository.dto.remote.RemoteCategoryResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class CategoryServiceProviderImplTest {

    private lateinit var categoryApiService: CategoryApiService
    private lateinit var categoryServiceProviderImpl: CategoryServiceProviderImpl

    @BeforeEach
    fun setUp() {
        categoryApiService = mockk()
        categoryServiceProviderImpl = CategoryServiceProviderImpl(categoryApiService)
    }

    @Test
    fun `getMovieCategories should call CategoryApiService`() = runTest {
        // Given
        val dummyResponse =
            RemoteCategoryResponse(emptyList())
        coEvery { categoryApiService.getMovieCategories() } returns dummyResponse

        // When
        val result = categoryServiceProviderImpl.getMovieCategories()

        // Then
        coVerify(exactly = 1) { categoryApiService.getMovieCategories() }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getTvShowCategories should call CategoryApiService`() = runTest {
        // Given
        val dummyResponse =
            RemoteCategoryResponse(emptyList())
        coEvery { categoryApiService.getTvShowCategories() } returns dummyResponse

        // When
        val result = categoryServiceProviderImpl.getTvShowCategories()

        // Then
        coVerify(exactly = 1) { categoryApiService.getTvShowCategories() }
        assertThat(result).isEqualTo(dummyResponse)
    }
}