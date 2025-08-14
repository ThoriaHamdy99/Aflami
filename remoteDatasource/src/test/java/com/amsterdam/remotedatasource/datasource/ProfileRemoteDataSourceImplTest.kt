package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.ProfileApiService
import com.amsterdam.repository.dto.remote.profile.AccountDetailsRemoteDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class ProfileRemoteDataSourceImplTest {
    private lateinit var profileApiService: ProfileApiService
    private lateinit var profileDataSourceImpl: ProfileRemoteDataSourceImpl

    @BeforeEach
    fun setUp() {
        profileApiService = mockk()
        profileDataSourceImpl = ProfileRemoteDataSourceImpl(profileApiService)
    }

    @Test
    fun `getAccountDetails should return account details when api call is successful`() = runTest {
        val expectedAccountDetails = mockk<AccountDetailsRemoteDto>()
        coEvery { profileApiService.getAccountDetails() } returns expectedAccountDetails

        val result = profileDataSourceImpl.getAccountDetails()

        assertThat(result).isEqualTo(expectedAccountDetails)
        coVerify(exactly = 1) { profileApiService.getAccountDetails() }
    }

    @Test
    fun `getAccountDetails should throw NetworkException when api call throws an exception`() =
        runTest {
            coEvery { profileApiService.getAccountDetails() } throws NetworkException()

            assertThrows<NetworkException> { profileDataSourceImpl.getAccountDetails() }
            coVerify(exactly = 1) { profileApiService.getAccountDetails() }
        }
}