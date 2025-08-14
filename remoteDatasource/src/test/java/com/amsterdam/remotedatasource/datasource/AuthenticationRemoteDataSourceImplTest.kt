package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.AuthenticationApiService
import com.amsterdam.repository.dto.remote.authentication.AuthenticationRemoteResponse
import com.amsterdam.repository.dto.remote.authentication.CreateSessionRemoteDto
import com.amsterdam.repository.dto.remote.authentication.CreateSessionRemoteResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AuthenticationRemoteDataSourceImplTest {

    private lateinit var authenticationApiService: AuthenticationApiService
    private lateinit var json: Json
    private lateinit var authenticationRemoteDataSourceImpl: AuthenticationRemoteDataSourceImpl

    @BeforeEach
    fun setUp() {
        authenticationApiService = mockk()
        json = Json { ignoreUnknownKeys = true }
        authenticationRemoteDataSourceImpl =
            AuthenticationRemoteDataSourceImpl(json, authenticationApiService)
    }

    @Test
    fun `When loginWithPassword and all API calls are successful should return session ID`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery {
                authenticationApiService.createSessionWithLogin(
                    CreateSessionRemoteDto(
                        username,
                        password,
                        requestToken
                    )
                )
            } returns activeTokenResponse
            coEvery { authenticationApiService.createSession(activeRequestToken) } returns createSessionResponse

            val result = authenticationRemoteDataSourceImpl.loginWithPassword(username, password)

            assertThat(result).isEqualTo(sessionId)
            coVerify(exactly = 1) { authenticationApiService.createRequestToken() }
            coVerify(exactly = 1) { authenticationApiService.createSessionWithLogin(any()) }
            coVerify(exactly = 1) { authenticationApiService.createSession(any()) }
        }

    @Test
    fun `When loginWithPassword and createRequestToken API call fails should throw NetworkException`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } throws NetworkException()

            assertThrows<NetworkException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
            coVerify(exactly = 1) { authenticationApiService.createRequestToken() }
            coVerify(exactly = 0) { authenticationApiService.createSessionWithLogin(any()) }
            coVerify(exactly = 0) { authenticationApiService.createSession(any()) }
        }

    @Test
    fun `When loginWithPassword and createSessionWithLogin API call fails should throw NetworkException`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws NetworkException()

            assertThrows<NetworkException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
            coVerify(exactly = 1) { authenticationApiService.createRequestToken() }
            coVerify(exactly = 1) { authenticationApiService.createSessionWithLogin(any()) }
            coVerify(exactly = 0) { authenticationApiService.createSession(any()) }
        }

    @Test
    fun `When loginWithPassword and createSession API call fails should throw NetworkException`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } returns activeTokenResponse
            coEvery { authenticationApiService.createSession(any()) } throws NetworkException()

            assertThrows<NetworkException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(
                    username,
                    password
                )
            }
            coVerify(exactly = 1) { authenticationApiService.createRequestToken() }
            coVerify(exactly = 1) { authenticationApiService.createSessionWithLogin(any()) }
            coVerify(exactly = 1) { authenticationApiService.createSession(any()) }
        }

    private val username = "testUser"
    private val password = "testPassword"
    private val requestToken = "unactive_request_token"
    private val activeRequestToken = "active_request_token"
    private val sessionId = "final_session_id"

    private val unactiveTokenResponse = AuthenticationRemoteResponse(
        isSuccess = true,
        expiresAt = "2025-08-04",
        requestToken = requestToken,
        statusCode = 200
    )
    private val activeTokenResponse = AuthenticationRemoteResponse(
        isSuccess = true,
        expiresAt = "2025-08-04",
        requestToken = activeRequestToken,
        statusCode = 200
    )
    private val createSessionResponse = CreateSessionRemoteResponse(
        success = true,
        sessionId = sessionId
    )
}