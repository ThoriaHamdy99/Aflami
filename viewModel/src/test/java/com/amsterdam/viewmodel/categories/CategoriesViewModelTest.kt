package com.amsterdam.viewmodel.categories

import app.cash.turbine.test
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase.Language
import com.amsterdam.viewmodel.shared.TabOption
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class CategoriesViewModelTest {
    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase = mockk(relaxed = true)
    private val viewModel by lazy {
        CategoriesViewModel(
            dispatcherProvider = TestDispatcherProvider()
        )
    }

    @BeforeEach
    fun setUp() {
        every { manageLocaleLanguageUseCase.getAppLanguage() } returns flowOf(Language.ENGLISH)
    }

    @Test
    fun `onClickMovieGenreCard should send NavigateToCategoriesMoviesDetailsScreenEffect`() =
        runTest {
            viewModel.effect.test {
                viewModel.onClickMovieGenreCard(genreName)
                advanceUntilIdle()
                assertThat(awaitItem()).isEqualTo(
                    CategoriesUiEffect.NavigateToCategoriesMoviesDetailsScreen(genreName)
                )
            }
        }

    @Test
    fun `onClickTvShowGenreCard should send NavigateToCategoriesTvShowsDetailsScreenEffect`() =
        runTest {
            viewModel.effect.test {
                viewModel.onClickTvShowGenreCard(genreName)
                advanceUntilIdle()
                assertThat(awaitItem()).isEqualTo(
                    CategoriesUiEffect.NavigateToCategoriesTvShowsDetailsScreen(genreName)
                )
            }
        }

    @Test
    fun `onChangeTabOption should update selectedTabOption in state`() = runTest {
        viewModel.onChangeTabOption(tabOption)
        advanceUntilIdle()
        assertThat(viewModel.state.value.selectedTabOption).isEqualTo(tabOption)

    }
    private val genreName = "action"
    private val tabOption = TabOption.MOVIES

}