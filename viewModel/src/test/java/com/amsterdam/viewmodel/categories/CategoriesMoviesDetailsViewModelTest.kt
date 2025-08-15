package com.amsterdam.viewmodel.categories


import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import app.cash.turbine.test
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMovieDetailsArgs
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsPagingSource
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsUiEffect
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsViewModel
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


@OptIn(ExperimentalCoroutinesApi::class)
class CategoriesMoviesDetailsViewModelTest {
    private val categoriesMoviesDetailsPagingSource: CategoriesMoviesDetailsPagingSource =
        mockk(relaxed = true)
    private val args: CategoriesMovieDetailsArgs = mockk(relaxed = true)
    private val viewModel by lazy {
        CategoriesMoviesDetailsViewModel(
            categoriesMoviesDetailsPagingSource,
            categoriesMovieDetailsArgs = args,
            dispatcherProvider = TestDispatcherProvider()
        )
    }

    @BeforeEach
    fun setUp() {
        every { args.genreName } returns MovieGenre.COMEDY.name
       coEvery { args.genreName } returns MovieGenre.COMEDY.name
    }

    @Test
    fun `onClickBack should send NavigateBack effect`() = runTest {
        viewModel
        viewModel.effect.test {
            viewModel.onClickBack()
            assertThat(awaitItem()).isEqualTo(CategoriesMoviesDetailsUiEffect.NavigateBack)
        }
    }

    @Test
    fun `onClickMovieCard should send NavigateToMovieDetails effect`() = runTest {
        viewModel.effect.test {
            launch { viewModel.onClickMovieCard(movieId) }
            assertThat(awaitItem()).isEqualTo(
                CategoriesMoviesDetailsUiEffect.NavigateToMovieDetails(movieId)
            )
        }
    }

    @Test
    fun `onClickGenre with same genre should not reload`() = runTest {
        viewModel.onClickGenre(initialState.selectedGenre)
        assertThat(viewModel.state.value).isEqualTo(initialState)
    }

    @Test
    fun `onClickGenre with different genre should update state and reload movies`() = runTest {
        viewModel.onClickGenre(newGenre)
        advanceUntilIdle()
        assertThat(viewModel.state.value.selectedGenre).isEqualTo(newGenre)
    }

    @Test
    fun `onPagingLoadStateChanged with Loading should set isLoading true`() = runTest {

        viewModel.onPagingLoadStateChanged(loadStates)
        assertThat(viewModel.state.value.isLoading).isTrue()
    }

    @Test
    fun `onPagingLoadStateChanged with NotLoading should set isLoading false`() = runTest {

        viewModel.onPagingLoadStateChanged(notLoadStates)
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `onPagingLoadStateChanged with Error should set error state`() = runTest {
        val exception = mockk<AflamiException>()
        val loadStates = CombinedLoadStates(
            refresh = LoadState.Error(exception),
            prepend = LoadState.NotLoading(false),
            append = LoadState.NotLoading(false),
            source = mockk(relaxed = true),
            mediator = null
        )
        viewModel.onPagingLoadStateChanged(loadStates)
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `onClickRetryRequest should reload movies`() = runTest {
        viewModel.onClickRetryRequest()
        advanceUntilIdle()
        assertThat(viewModel.state.value.isLoading).isTrue()
    }

    private val movieId = 42L
    private val newGenre = MovieGenre.COMEDY
    private val initialState = viewModel.state.value
    private val loadStates = CombinedLoadStates(
        refresh = LoadState.Loading,
        prepend = LoadState.NotLoading(false),
        append = LoadState.NotLoading(false),
        source = mockk(relaxed = true),
        mediator = null
    )
    private val notLoadStates = CombinedLoadStates(
        refresh = LoadState.NotLoading(false),
        prepend = LoadState.NotLoading(false),
        append = LoadState.NotLoading(false),
        source = mockk(relaxed = true),
        mediator = null
    )
}
