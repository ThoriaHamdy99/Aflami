package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.AccessDeniedException
import com.amsterdam.domain.exceptions.AccessRestrictedException
import com.amsterdam.domain.exceptions.AccountDisabledException
import com.amsterdam.domain.exceptions.InvalidCredentialsException
import com.amsterdam.domain.exceptions.InvalidSessionException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.exceptions.ServerErrorException
import com.amsterdam.domain.exceptions.VerificationRequiredException
import com.amsterdam.remotedatasource.api.AuthenticationApiService
import com.amsterdam.repository.dto.remote.authentication.AuthenticationRemoteResponse
import com.amsterdam.repository.dto.remote.authentication.CreateSessionRemoteResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException

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
    fun `loginWithPassword should return the correct session ID on success`() = runTest {
        mockSuccessfulLoginFlow()
        val result = authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        assertThat(result).isEqualTo(sessionId)
    }

    @Test
    fun `loginWithPassword should call createRequestToken exactly once`() = runTest {
        mockSuccessfulLoginFlow()
        authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        coVerify(exactly = 1) { authenticationApiService.createRequestToken() }
    }

    @Test
    fun `loginWithPassword should call createSessionWithLogin exactly once`() = runTest {
        mockSuccessfulLoginFlow()
        authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        coVerify(exactly = 1) { authenticationApiService.createSessionWithLogin(any()) }
    }

    @Test
    fun `loginWithPassword should call createSession exactly once`() = runTest {
        mockSuccessfulLoginFlow()
        authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        coVerify(exactly = 1) { authenticationApiService.createSession(any()) }
    }

    @Test
    fun `createRequestToken should throw NoInternetException on ConnectException`() = runTest {
        coEvery { authenticationApiService.createRequestToken() } throws ConnectException()
        assertThrows<NoInternetException> {
            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        }
    }

    @Test
    fun `createRequestToken should throw ServerErrorException on SerializationException`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } throws SerializationException()
            assertThrows<ServerErrorException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createRequestToken should throw ServerErrorException on generic HttpException`() =
        runTest {
            val httpException = HttpException(Response.error<Any>(500, "".toResponseBody()))
            coEvery { authenticationApiService.createRequestToken() } throws httpException
            assertThrows<ServerErrorException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createRequestToken should throw NetworkException on generic Exception`() = runTest {
        coEvery { authenticationApiService.createRequestToken() } throws Exception("Generic error")
        assertThrows<NetworkException> {
            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        }
    }

    @Test
    fun `loginWithPassword should throw NetworkException if createRequestToken returns a null request token`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse.copy(
                requestToken = null
            )
            assertThrows<NetworkException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
            coVerify(exactly = 1) { authenticationApiService.createRequestToken() }
        }


    @Test
    fun `createSessionWithLogin should throw NoInternetException on ConnectException`() = runTest {
        coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
        coEvery { authenticationApiService.createSessionWithLogin(any()) } throws ConnectException()
        assertThrows<NoInternetException> {
            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        }
    }

    @Test
    fun `createSessionWithLogin should throw ServerErrorException on SerializationException`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws SerializationException()
            assertThrows<ServerErrorException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw InvalidCredentialsException on 401 with code 3`() =
        runTest {
            val errorBody = "{\"status_code\": 3, \"status_message\": \"Invalid credentials\"}"
            val httpException = HttpException(
                Response.error<Any>(
                    401,
                    errorBody.toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws httpException
            assertThrows<InvalidCredentialsException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw InvalidSessionException on 401 with code 7`() =
        runTest {
            val errorBody = "{\"status_code\": 7, \"status_message\": \"Invalid session\"}"
            val httpException = HttpException(
                Response.error<Any>(
                    401,
                    errorBody.toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws httpException
            assertThrows<InvalidSessionException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw AccountDisabledException on 401 with code 31`() =
        runTest {
            val errorBody = "{\"status_code\": 31, \"status_message\": \"Account disabled\"}"
            val httpException = HttpException(
                Response.error<Any>(
                    401,
                    errorBody.toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws httpException
            assertThrows<AccountDisabledException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw VerificationRequiredException on 401 with code 32`() =
        runTest {
            val errorBody = "{\"status_code\": 32, \"status_message\": \"Verification required\"}"
            val httpException = HttpException(
                Response.error<Any>(
                    401,
                    errorBody.toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws httpException
            assertThrows<VerificationRequiredException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw AccessDeniedException on 401 with code 38`() =
        runTest {
            val errorBody = "{\"status_code\": 38, \"status_message\": \"Access denied\"}"
            val httpException = HttpException(
                Response.error<Any>(
                    401,
                    errorBody.toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws httpException
            assertThrows<AccessDeniedException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw AccessRestrictedException on 401 with code 45`() =
        runTest {
            val errorBody = "{\"status_code\": 45, \"status_message\": \"Access restricted\"}"
            val httpException = HttpException(
                Response.error<Any>(
                    401,
                    errorBody.toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws httpException
            assertThrows<AccessRestrictedException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw NetworkException on 401 with unknown code`() =
        runTest {
            val errorBody = "{\"status_code\": 999, \"status_message\": \"Unknown error\"}"
            val httpException = HttpException(
                Response.error<Any>(
                    401,
                    errorBody.toResponseBody("application/json".toMediaTypeOrNull())
                )
            )
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws httpException
            assertThrows<NetworkException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw ServerErrorException on generic HttpException`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            val httpException = HttpException(Response.error<Any>(500, "".toResponseBody()))
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws httpException
            assertThrows<ServerErrorException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw NetworkException on generic Exception`() = runTest {
        coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
        coEvery { authenticationApiService.createSessionWithLogin(any()) } throws Exception("Generic error")
        assertThrows<NetworkException> {
            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        }
    }

    @Test
    fun `loginWithPassword should throw NetworkException if createSessionWithLogin returns a null request token`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } returns activeTokenResponse.copy(
                requestToken = null
            )
            assertThrows<NetworkException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
            coVerify(exactly = 1) { authenticationApiService.createRequestToken() }
        }


    @Test
    fun `createSession should throw NoInternetException on ConnectException`() = runTest {
        coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
        coEvery { authenticationApiService.createSessionWithLogin(any()) } returns activeTokenResponse
        coEvery { authenticationApiService.createSession(any()) } throws ConnectException()
        assertThrows<NoInternetException> {
            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        }
    }

    @Test
    fun `createSession should throw ServerErrorException on SerializationException`() = runTest {
        coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
        coEvery { authenticationApiService.createSessionWithLogin(any()) } returns activeTokenResponse
        coEvery { authenticationApiService.createSession(any()) } throws SerializationException()
        assertThrows<ServerErrorException> {
            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        }
    }

    @Test
    fun `createSession should throw ServerErrorException on generic HttpException`() = runTest {
        coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
        coEvery { authenticationApiService.createSessionWithLogin(any()) } returns activeTokenResponse
        val httpException = HttpException(Response.error<Any>(500, "".toResponseBody()))
        coEvery { authenticationApiService.createSession(any()) } throws httpException
        assertThrows<ServerErrorException> {
            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        }
    }

    @Test
    fun `createSession should throw NetworkException on generic Exception`() = runTest {
        coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
        coEvery { authenticationApiService.createSessionWithLogin(any()) } returns activeTokenResponse
        coEvery { authenticationApiService.createSession(any()) } throws Exception("Generic error")
        assertThrows<NetworkException> {
            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        }
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

    private fun mockSuccessfulLoginFlow() {
        coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
        coEvery { authenticationApiService.createSessionWithLogin(any()) } returns activeTokenResponse
        coEvery { authenticationApiService.createSession(any()) } returns createSessionResponse
    }
}