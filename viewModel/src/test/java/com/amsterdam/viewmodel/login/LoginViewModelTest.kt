package com.amsterdam.viewmodel.login

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.authentication.LoginAsGuestUseCase
import com.amsterdam.domain.useCase.authentication.LoginWithPasswordUseCase
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel
    private lateinit var loginAsGuestUseCase: LoginAsGuestUseCase

    private val testDispatcherProvider = TestDispatcherProvider()
    private var testScope = TestScope(testDispatcherProvider.testDispatcher)
    private val loginWithPasswordUseCase: LoginWithPasswordUseCase = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        loginAsGuestUseCase = mockk(relaxed = true)
        viewModel = LoginViewModel(
            dispatcherProvider = testDispatcherProvider,
            loginWithPasswordUseCase = loginWithPasswordUseCase,
            loginAsGuestUseCase = loginAsGuestUseCase
        )
    }

    @Test
    fun `onUserNameUpdated should update username`() =
        testScope.runTest {
            val username = "testuser"

            viewModel.onUserNameUpdated(username)
            advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.username).isEqualTo(username)
        }

    @Test
    fun `onPasswordUpdate should update password`() =
        testScope.runTest {
            val password = "testpassword"

            viewModel.onPasswordUpdate(password)
            advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.password).isEqualTo(password)
        }

    @Test
    fun `onUserNameUpdated should clear username errors`() =
        testScope.runTest {

            val username = "testuser"

            viewModel.onUserNameUpdated(username)
            advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.loginError).isNull()
        }

    @Test
    fun `onPasswordUpdate should clear password errors`() =
        testScope.runTest {

            val password = "testpassword"

            viewModel.onPasswordUpdate(password)
            advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.loginError).isNull()
        }

    @Test
    fun `should set username error when login fail happens`() =
        testScope.runTest {
            viewModel.onUserNameUpdated("testuser")
            testScope.testScheduler.advanceUntilIdle()
            viewModel.onLoginClicked()
            advanceUntilIdle()
            val state = viewModel.state.value
            assertThat(state.loginError).isNull()
        }


    @Test
    fun `should set password error when login fail happens`() =
        testScope.runTest {
            viewModel.onPasswordUpdate("testpassword")
            testScope.testScheduler.advanceUntilIdle()
            viewModel.onLoginClicked()
            advanceUntilIdle()
            val state = viewModel.state.value
            assertThat(state.loginError).isNull()
        }

    @Test
    fun `onShowPasswordClicked should toggle password visibility when called`() =
        testScope.runTest {
            val initialState = viewModel.state.value.isPasswordShown

            viewModel.onShowPasswordClicked()
            advanceUntilIdle()

            assertThat(viewModel.state.value.isPasswordShown).isEqualTo(!initialState)

            viewModel.onShowPasswordClicked()
            advanceUntilIdle()

            assertThat(viewModel.state.value.isPasswordShown).isEqualTo(initialState)
        }

    @Test
    fun `onUserNameUpdated should set login button to be enabled when both username and password are not blank`() =
        testScope.runTest {
            viewModel.onPasswordUpdate("testpassword")
            viewModel.onUserNameUpdated("testuser")
            advanceUntilIdle()
            val result = viewModel.state.value.isLoginButtonEnabled
            assertThat(result).isTrue()
        }

    @Test
    fun `onUserNameUpdated should set login button to be non enabled when both username is blank`() =
        testScope.runTest {
            viewModel.onUserNameUpdated("")
            viewModel.onPasswordUpdate("testpassword")
            advanceUntilIdle()

            val result = viewModel.state.value.isLoginButtonEnabled
            assertThat(result).isFalse()
        }

    @Test
    fun `login button should be disabled when username is blank`() =
        testScope.runTest {
            viewModel.onUserNameUpdated("")
            viewModel.onPasswordUpdate("testpassword")
            advanceUntilIdle()

            val result = viewModel.state.value.isLoginButtonLoading
            assertThat(result).isFalse()
        }

    @Test
    fun `login button should be disabled when password is blank`() =
        testScope.runTest {
            viewModel.onUserNameUpdated("testuser")
            viewModel.onPasswordUpdate("")
            advanceUntilIdle()

            val result = viewModel.state.value.isLoginButtonLoading
            assertThat(result).isFalse()
        }

    @Test
    fun `login button should be disabled when both username and password are blank`() =
        testScope.runTest {
            viewModel.onUserNameUpdated("")
            viewModel.onPasswordUpdate("")
            advanceUntilIdle()

            val result = viewModel.state.value.isLoginButtonLoading
            assertThat(result).isFalse()
        }

    @Test
    fun `clearing username should disable login button when password is set`() =
        testScope.runTest {


            viewModel.onUserNameUpdated("")
            advanceUntilIdle()

            val result = viewModel.state.value.isLoginButtonLoading
            assertThat(result).isFalse()
        }

    @Test
    fun `clearing password should disable login button when username is set`() =
        testScope.runTest {


            viewModel.onPasswordUpdate("")
            testScope.testScheduler.advanceUntilIdle()

            val result = viewModel.state.value.isLoginButtonLoading
            assertThat(result).isFalse()
        }


    @Test
    fun `onLoginClicked should set loading to false after operation regardless of outcome`() =
        testScope.runTest {
            coEvery { loginWithPasswordUseCase(any(), any()) } throws AflamiException()
            viewModel.onLoginClicked()
            advanceUntilIdle()
            val result = viewModel.state.value.isLoginButtonLoading
            assertThat(result).isFalse()
        }

    @Test
    fun `onLoginClicked should send NavigateToHome effect when login is successful`() =
        testScope.runTest {
            val effects = mutableListOf<LoginEffect>()
            val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }
            viewModel.onLoginClicked()
            advanceUntilIdle()
            job.cancel()
            assertThat(effects).contains(LoginEffect.NavigateToHome)
        }

    @Test
    fun `onContinueAsGuestClicked should send NavigateToHome effect after success`() =
        testScope.runTest {
            val effects = mutableListOf<LoginEffect>()
            val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }
            viewModel.onContinueAsGuestClicked()
            advanceUntilIdle()
            job.cancel()
            assertThat(effects).contains(LoginEffect.NavigateToHome)
        }

    @Test
    fun `onForgotPasswordClicked should send NavigateToResetPassword effect`() =
        testScope.runTest {
            val effects = mutableListOf<LoginEffect>()
            val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }
            viewModel.onForgotPasswordClicked()
            advanceUntilIdle()
            job.cancel()
            assertThat(effects).contains(LoginEffect.NavigateToResetPassword)
        }

    @Test
    fun `onCreateAccountClicked should send NavigateToRegister effect`() =
        testScope.runTest {
            val effects = mutableListOf<LoginEffect>()
            val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }
            viewModel.onCreateAccountClicked()
            advanceUntilIdle()
            job.cancel()
            assertThat(effects).contains(LoginEffect.NavigateToRegister)
        }

    @Test
    fun `onLoginClicked should send ShowCredentialsError effect when login fails`() =
        testScope.runTest {
            coEvery { loginWithPasswordUseCase(any(), any()) } throws AflamiException()
            viewModel.onLoginClicked()
            val effects = mutableListOf<LoginEffect>()
            val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }
            advanceUntilIdle()
            job.cancel()
            assertThat(effects).contains(LoginEffect.ShowCredentialsError)
        }

    @Test
    fun `onLoginClicked should set ShowCredentialsError effect when login fails`() =
        testScope.runTest {
            coEvery { loginWithPasswordUseCase(any(), any()) } throws AflamiException()
            viewModel.onLoginClicked()
            val effects = mutableListOf<LoginEffect>()
            val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }
            advanceUntilIdle()
            job.cancel()
            assertThat(effects).contains(LoginEffect.ShowCredentialsError)
        }

    @Test
    fun `onContinueAsGuestClicked should update error state when login fails`() = testScope
        .runTest {
            coEvery { loginAsGuestUseCase() } throws AflamiException()

            viewModel.onContinueAsGuestClicked()
            advanceUntilIdle()
            val result = viewModel.state.value.loginError
            assertThat(result).isNotNull()
        }
}