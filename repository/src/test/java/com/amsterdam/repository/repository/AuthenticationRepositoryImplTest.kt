import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.repository.datasource.local.AuthenticationLocalSource
import com.amsterdam.repository.datasource.remote.AuthenticationRemoteSource
import com.amsterdam.repository.mapper.local.toLocalDto
import com.amsterdam.repository.repository.AuthenticationRepositoryImpl
import com.amsterdam.repository.security.CryptoData
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AuthenticationRepositoryImplTest {

    private lateinit var repository: AuthenticationRepository

    private val authenticationRemoteSource: AuthenticationRemoteSource = mockk()
    private val authenticationLocalSource: AuthenticationLocalSource = mockk()
    private val cryptoData: CryptoData = mockk()

    private val testUsername = "testUser"
    private val testPassword = "testPassword"
    private val testSessionId = "some_session_id_from_remote"
    private val encryptedSessionId = "encrypted_session_id"

    @BeforeEach
    fun setup() {
        clearAllMocks()
        repository = AuthenticationRepositoryImpl(
            authenticationRemoteSource = authenticationRemoteSource,
            authenticationLocalSource = authenticationLocalSource,
            cryptoData = cryptoData
        )
    }

    @Test
    fun `loginWithPassword should encrypt and cache session ID and set session type on success`() =
        runTest {
            // Given
            coEvery {
                authenticationRemoteSource.loginWithPassword(
                    testUsername,
                    testPassword
                )
            } returns testSessionId
            every { cryptoData.encryptString(testSessionId) } returns encryptedSessionId
            coJustRun { authenticationLocalSource.cacheSessionId(encryptedSessionId) }
            coJustRun { authenticationLocalSource.setSessionType(SessionType.LOGGED_IN.toLocalDto()) }

            // When
            val result = repository.loginWithPassword(testUsername, testPassword)

            // Then
            assertThat(result).isEqualTo(Unit)
            coVerify(exactly = 1) {
                authenticationRemoteSource.loginWithPassword(
                    testUsername,
                    testPassword
                )
            }
            coVerify(exactly = 1) { authenticationLocalSource.cacheSessionId(encryptedSessionId) }
            coVerify(exactly = 1) { authenticationLocalSource.setSessionType(SessionType.LOGGED_IN.toLocalDto()) }
        }

    @Test
    fun `loginWithPassword should throw exception if remote login fails and not cache data`() =
        runTest {
            // Given
            val expectedException = RuntimeException("Remote login failed")
            coEvery {
                authenticationRemoteSource.loginWithPassword(
                    any(),
                    any()
                )
            } throws expectedException

            // When
            val thrownException = assertThrows<RuntimeException> {
                repository.loginWithPassword(testUsername, testPassword)
            }

            // Then
            assertThat(thrownException).isEqualTo(expectedException)
            coVerify(exactly = 1) {
                authenticationRemoteSource.loginWithPassword(
                    testUsername,
                    testPassword
                )
            }
            verify(exactly = 0) { cryptoData.encryptString(any()) }
            coVerify(exactly = 0) { authenticationLocalSource.cacheSessionId(any()) }
            coVerify(exactly = 0) { authenticationLocalSource.setSessionType(any()) }
        }

    @Test
    fun `getSessionId should return decrypted session ID when available`() = runTest {
        // Given
        coEvery { authenticationLocalSource.getCachedSessionId() } returns encryptedSessionId
        every { cryptoData.decryptString(encryptedSessionId) } returns testSessionId

        // When
        val result = repository.getSessionId()

        // Then
        assertThat(result).isEqualTo(testSessionId)
        coVerify(exactly = 1) { authenticationLocalSource.getCachedSessionId() }
        verify(exactly = 1) { cryptoData.decryptString(encryptedSessionId) }
    }

    @Test
    fun `getSessionId should throw UnknownException if decrypted result is null`() = runTest {
        // Given
        coEvery { authenticationLocalSource.getCachedSessionId() } returns "invalid_data"
        every { cryptoData.decryptString("invalid_data") } returns null

        // When / Then
        assertThrows<UnknownException> { repository.getSessionId() }

        coVerify(exactly = 1) { authenticationLocalSource.getCachedSessionId() }
        verify(exactly = 1) { cryptoData.decryptString("invalid_data") }
    }

    @Test
    fun `setSessionType should store the correct string representation in local source`() =
        runTest {
            // Given
            val sessionType = SessionType.GUEST
            coJustRun { authenticationLocalSource.setSessionType(sessionType.toLocalDto()) }

            // When
            repository.setSessionType(sessionType)

            // Then
            coVerify(exactly = 1) { authenticationLocalSource.setSessionType(sessionType.toLocalDto()) }
        }

    @Test
    fun `getSessionType should return the correct domain session type from local source`() =
        runTest {
            // Given
            coEvery { authenticationLocalSource.getSessionType() } returns "GUEST"

            // When
            val result = repository.getSessionType()

            // Then
            assertThat(result).isEqualTo(SessionType.GUEST)
            coVerify(exactly = 1) { authenticationLocalSource.getSessionType() }
        }

    @Test
    fun `logout should delete remote session and then clear local data on success`() = runTest {
        // Given
        coEvery { authenticationLocalSource.getCachedSessionId() } returns encryptedSessionId
        every { cryptoData.decryptString(encryptedSessionId) } returns testSessionId
        coJustRun { authenticationLocalSource.clearCachedSessionId() }
        coJustRun { authenticationLocalSource.setSessionType(SessionType.NOT_LOGGED_IN.toLocalDto()) }

        // When
        val result = repository.logout()

        // Then
        assertThat(result).isEqualTo(Unit)
        coVerify(exactly = 1) { authenticationLocalSource.clearCachedSessionId() }
        coVerify(exactly = 1) { authenticationLocalSource.setSessionType(SessionType.NOT_LOGGED_IN.toLocalDto()) }
    }


}
