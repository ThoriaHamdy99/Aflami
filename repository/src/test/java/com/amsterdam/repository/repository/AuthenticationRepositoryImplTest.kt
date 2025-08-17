package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.repository.datasource.local.AuthenticationLocalDataSource
import com.amsterdam.repository.datasource.local.ProfileLocalDataSource
import com.amsterdam.repository.datasource.remote.AuthenticationRemoteDataSource
import com.amsterdam.repository.mapper.toLocalDto
import com.amsterdam.repository.security.CryptoManager
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AuthenticationRepositoryImplTest {

    private lateinit var repository: AuthenticationRepository

    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource = mockk()
    private val authenticationLocalDataSource: AuthenticationLocalDataSource = mockk()
    private val profileLocalDataSource: ProfileLocalDataSource = mockk()
    private val cryptoManager: CryptoManager = mockk()

    @BeforeEach
    fun setup() {
        clearAllMocks()
        repository = AuthenticationRepositoryImpl(
            authenticationRemoteDataSource = authenticationRemoteDataSource,
            authenticationLocalDataSource = authenticationLocalDataSource,
            profileLocalDataSource = profileLocalDataSource,
            cryptoManager = cryptoManager
        )
    }

    @Test
    fun `loginWithPassword should throw exception on remote failure`() = runTest {
        val expectedException = RuntimeException("Remote login failed")
        coEvery {
            authenticationRemoteDataSource.loginWithPassword(
                any(),
                any()
            )
        } throws expectedException

        val thrownException = assertThrows<RuntimeException> {
            repository.loginWithPassword("user", "pass")
        }

        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 0) { authenticationLocalDataSource.cacheSessionId(any()) }
        coVerify(exactly = 0) { authenticationLocalDataSource.setSessionType(any()) }
    }

    @Test
    fun `setSessionType should call local data source`() = runTest {
        val sessionType = SessionType.GUEST
        coJustRun { authenticationLocalDataSource.setSessionType(sessionType.toLocalDto()) }

        repository.setSessionType(sessionType)

        coVerify(exactly = 1) { authenticationLocalDataSource.setSessionType(sessionType.toLocalDto()) }
    }

    @Test
    fun `setSessionType should rethrow exception if local source fails`() = runTest {
        val expectedException = RuntimeException("Failed to write session type")
        coEvery { authenticationLocalDataSource.setSessionType(any()) } throws expectedException

        val thrownException = assertThrows<RuntimeException> {
            repository.setSessionType(SessionType.GUEST)
        }

        assertThat(thrownException).isEqualTo(expectedException)
    }

    @Test
    fun `getSessionType should return correct type from local source`() = runTest {
        coEvery { authenticationLocalDataSource.getSessionType() } returns "GUEST"

        val result = repository.getSessionType()

        assertThat(result).isEqualTo(SessionType.GUEST)
        coVerify(exactly = 1) { authenticationLocalDataSource.getSessionType() }
    }

    @Test
    fun `getSessionType should rethrow exception if local source fails`() = runTest {
        val expectedException = RuntimeException("Failed to read session type")
        coEvery { authenticationLocalDataSource.getSessionType() } throws expectedException

        val thrownException = assertThrows<RuntimeException> {
            repository.getSessionType()
        }

        assertThat(thrownException).isEqualTo(expectedException)
    }

    @Test
    fun `logout should clear session and profile data`() = runTest {
        coJustRun { authenticationLocalDataSource.clearCachedSessionId() }
        coJustRun { authenticationLocalDataSource.setSessionType("") }
        coJustRun { profileLocalDataSource.deleteAccountDetails() }

        repository.logout()

        coVerify(exactly = 1) { authenticationLocalDataSource.clearCachedSessionId() }
        coVerify(exactly = 1) { authenticationLocalDataSource.setSessionType("") }
        coVerify(exactly = 1) { profileLocalDataSource.deleteAccountDetails() }
    }

    @Test
    fun `logout should throw exception if clearing session fails and stop execution`() = runTest {
        val expectedException = RuntimeException("Failed to clear session ID")
        coEvery { authenticationLocalDataSource.clearCachedSessionId() } throws expectedException

        val thrownException = assertThrows<RuntimeException> {
            repository.logout()
        }

        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 0) { authenticationLocalDataSource.setSessionType(any()) }
        coVerify(exactly = 0) { profileLocalDataSource.deleteAccountDetails() }
    }
}