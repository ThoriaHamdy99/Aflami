package com.example.viewmodel.movieDetails

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.useCase.GetMovieDetailsUseCase
import com.example.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras
import com.example.viewmodel.shared.Selectable
import com.example.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {

    private lateinit var viewModel: MovieDetailsViewModel
    private val testDispatcherProvider = TestDispatcherProvider()
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase = mockk(relaxed = true)
    private val movieDetailsUiStateMapper: MovieDetailsUiStateMapper = mockk(relaxed = true)
    private var testArgs: MovieDetailsArgs = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            movieDetailsUiStateMapper = movieDetailsUiStateMapper,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            testDispatcherProvider
        )
        Dispatchers.setMain(testDispatcherProvider.testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should update state with received  movie id`() = runTest {
        //Given
        val movieId = 1L
        every { testArgs.movieId } returns movieId
        coEvery { getMovieDetailsUseCase.invoke(any()) } throws NoInternetException()
        //When
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            movieDetailsUiStateMapper = movieDetailsUiStateMapper,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            testDispatcherProvider
        )
        advanceUntilIdle()
        //Then
        assertThat(viewModel.state.value.movieId).isEqualTo(movieId)
    }

    @Test
    fun `init should update state with movie details when success loaded the data`() = runTest {
        //Given
        val movieDetailsUiState = MovieDetailsUiState(movieId = 99)
        coEvery { movieDetailsUiStateMapper.toUiState(any()) } returns movieDetailsUiState
        //When
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            movieDetailsUiStateMapper = movieDetailsUiStateMapper,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            testDispatcherProvider
        )
        advanceUntilIdle()
        //Then
        assertThat(viewModel.state.value).isEqualTo(movieDetailsUiState)
    }

    @Test
    fun `init should update error state and stop loading when failed load the data `() = runTest {
        //Given
        coEvery { getMovieDetailsUseCase.invoke(any()) } throws NoInternetException()
        //When
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            movieDetailsUiStateMapper = movieDetailsUiStateMapper,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            testDispatcherProvider
        )
        advanceUntilIdle()
        //Then
        assertThat(viewModel.state.value.networkError).isTrue()
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `init should not do any thing for now when received unknown error`() = runTest {
        //Given
        coEvery { getMovieDetailsUseCase.invoke(any()) } throws AflamiException()
        val defultState  = MovieDetailsUiState()
        //When
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            movieDetailsUiStateMapper = movieDetailsUiStateMapper,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            testDispatcherProvider
        )
        advanceUntilIdle()
        //Then
        assertThat(viewModel.state.value).isEqualTo(defultState)
    }

    @Test
    fun `onClickMovieExtras should update the state to be true for selected movie extras when its call`() =
        runTest {
            //Given
            val selectedExtras = MovieExtras.REVIEWS
            val extraItem: List<Selectable<MovieExtras>> = listOf(
                Selectable(isSelected = false, MovieExtras.MORE_LIKE_THIS),
                Selectable(isSelected = true, MovieExtras.REVIEWS),
                Selectable(isSelected = false, MovieExtras.GALLERY),
                Selectable(isSelected = false, MovieExtras.COMPANY_PRODUCTION)
            )
            every { movieDetailsUiStateMapper.toUiState(any()) } returns MovieDetailsUiState()
            viewModel = MovieDetailsViewModel(
                args = testArgs,
                movieDetailsUiStateMapper = movieDetailsUiStateMapper,
                getMovieDetailsUseCase = getMovieDetailsUseCase,
                testDispatcherProvider
            )
            //When
            advanceUntilIdle()
            viewModel.onClickMovieExtras(selectedExtras)
            advanceUntilIdle()
            //Then
            assertThat(viewModel.state.value.extraItem).isEqualTo(extraItem)
        }

    @Test
    fun `onClickShowAllCast should send NavigateToCastsScreenEffect when its call`() = runTest {
        val effects = mutableListOf<MovieDetailsEffect>()
        val collectJob = launch {
            viewModel.effect.collect { effects.add(it!!) }
        }
        viewModel.onClickShowAllCast()
        advanceUntilIdle()
        collectJob.cancel()
        assertThat(effects.first()).isEqualTo(MovieDetailsEffect.NavigateToCastsScreenEffect)
    }

    @Test
    fun `onClickBack should send NavigateToCastsScreenEffect when its call`() = runTest {
        val effects = mutableListOf<MovieDetailsEffect>()
        val collectJob = launch {
            viewModel.effect.collect { effects.add(it!!) }
        }
        viewModel.onClickBack()
        advanceUntilIdle()
        collectJob.cancel()
        assertThat(effects.first()).isEqualTo(MovieDetailsEffect.NavigateBackEffect)
    }
    @Test
    fun `onClickRetryRequest should call the use case to get the movie details data`() = runTest{
        //When
        advanceUntilIdle()
        viewModel.onClickRetryRequest()
        advanceUntilIdle()
        //Then
        coVerify (exactly = 2){
            getMovieDetailsUseCase.invoke(0L)
        }
    }

}
