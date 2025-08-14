package com.amsterdam.viewmodel.categories

import app.cash.turbine.test
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.amsterdam.domain.useCase.details.GetTvShowsByGenreUseCase
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.viewmodel.categoriesDetails.tvShow.CategoriesTvShowsDetailsArgs
import com.amsterdam.viewmodel.categoriesDetails.tvShow.CategoriesTvShowsDetailsUiEffect
import com.amsterdam.viewmodel.categoriesDetails.tvShow.CategoriesTvShowsDetailsViewModel
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class CategoriesTvShowsDetailsViewModelTest {

    private val getTvShowsByGenreUseCase: GetTvShowsByGenreUseCase = mockk(relaxed = true)
    private val args: CategoriesTvShowsDetailsArgs = mockk(relaxed = true)
    private val viewModel by lazy {
        CategoriesTvShowsDetailsViewModel(
            getTvShowsByGenreIdUseCase = getTvShowsByGenreUseCase,
            categoriesTvShowsDetailsArgs = args,
            dispatcherProvider = TestDispatcherProvider()
        )
    }

    @Test
    fun `init should set initial genre`() = runTest {
        coEvery { args.genreName } returns TvShowGenre.COMEDY.name
        coEvery { getTvShowsByGenreUseCase.invoke(TvShowGenre.COMEDY.name) } returns mockk()
        assertThat(viewModel.state.value.selectedGenre).isEqualTo(TvShowGenre.COMEDY)
    }

    @Test
    fun `onClickBack should send NavigateBack effect`() = runTest {
        viewModel.effect.test {
            viewModel.onClickBack()
            assertThat(awaitItem()).isEqualTo(CategoriesTvShowsDetailsUiEffect.NavigateBack)
        }
    }

    @Test
    fun `onClickTvShowCard should send NavigateToTvShowDetails effect`() = runTest {
        viewModel.effect.test {
            launch { viewModel.onClickTvShowCard(tvShowId) }
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(
                CategoriesTvShowsDetailsUiEffect.NavigateToTvShowDetails(tvShowId)
            )
        }
    }

    @Test
    fun `onClickGenre with different genre should update selectedGenre`() = runTest {
        val newGenre = TvShowGenre.COMEDY
        viewModel.onClickGenre(newGenre)
        advanceUntilIdle()
        assertThat(viewModel.state.value.selectedGenre).isEqualTo(newGenre)
    }

    @Test
    fun `onClickGenre with same genre should not change selectedGenre`() = runTest {
        viewModel.onClickGenre(initialGenre)
        advanceUntilIdle()
        assertThat(viewModel.state.value.selectedGenre).isEqualTo(initialGenre)
    }

    @Test
    fun `onClickRetryRequest should trigger loadTvShowsForSelectedGenre`() = runTest {
        viewModel.onClickRetryRequest()
        advanceUntilIdle()
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `onPagingLoadStateChanged NotLoading should set isLoading false`() = runTest {
        val loadStates = mockk<CombinedLoadStates> {
            every { refresh } returns LoadState.NotLoading(endOfPaginationReached = false)
        }
        viewModel.onPagingLoadStateChanged(loadStates)
        assertThat(viewModel.state.value.isLoading).isFalse()
    }
    private val tvShowId = 123L
    private val initialGenre = viewModel.state.value.selectedGenre

}
