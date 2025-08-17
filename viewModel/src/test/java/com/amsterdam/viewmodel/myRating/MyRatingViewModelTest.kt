package com.amsterdam.viewmodel.myRating

import app.cash.turbine.test
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.model.category.MovieGenre
import com.amsterdam.domain.model.category.TvShowGenre
import com.amsterdam.domain.useCase.myRating.movie.DeleteUserRatedMovieUseCase
import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase
import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase.UserRatedMovie
import com.amsterdam.domain.useCase.myRating.tvShow.DeleteUserRatedTvShowUseCase
import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase
import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase.UserRatedTvShow
import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.viewmodel.shared.TabOption
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.amsterdam.viewmodel.utils.TestExtension
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(TestExtension::class)
class MyRatingViewModelTest {

    private val getUserRatedMoviesUseCase: GetUserRatedMoviesUseCase = mockk(relaxed = true)
    private val deleteUserRatedMoviesUseCase: DeleteUserRatedMovieUseCase = mockk()
    private val getUserRatedTvShowsUseCase: GetUserRatedTvShowsUseCase = mockk(relaxed = true)
    private val deleteUserRatedTvShowUseCase: DeleteUserRatedTvShowUseCase = mockk()

    private val viewModel: MyRatingViewModel by lazy {
        MyRatingViewModel(
            getUserRatedMoviesUseCase,
            deleteUserRatedMoviesUseCase,
            getUserRatedTvShowsUseCase,
            deleteUserRatedTvShowUseCase,
            TestDispatcherProvider()
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `init should Load Movies when ViewModel Is Created`() = runTest {
        coEvery { getUserRatedMoviesUseCase.getRatedMovies() } returns listOf(fakeUserRatedMovie)

        viewModel
        advanceUntilIdle()

        assertThat(viewModel.state.value.movies).isNotEmpty()
    }

    @Test
    fun `getRatedMovies should Set Error when UseCase Throws Exception`() = runTest {
        coEvery { getUserRatedMoviesUseCase.getRatedMovies() } throws NetworkException()

        viewModel
        advanceUntilIdle()

        assertThat(viewModel.state.value.error).isEqualTo(MyRatingUiState.MyRatingErrorState.NoInternetError)
    }

    //region Tests for Delete Movie
    @Test
    fun `onClickDeleteMyMovieRatingIcon should Remove Movie From State`() =
        runTest {
            coEvery { getUserRatedMoviesUseCase.getRatedMovies() } returns listOf(fakeUserRatedMovie)
            coEvery { deleteUserRatedMoviesUseCase.deleteMovieRate(any()) } returns Unit

            viewModel.onClickDeleteMyMovieRatingIcon(fakeUserRatedMovie.movie.id)
            advanceUntilIdle()

            assertThat(viewModel.state.value.movies).doesNotContain(fakeUserRatedMovie)
        }

    @Test
    fun `onClickDeleteMyMovieRatingIcon should Show Success Snack Bar when Delete Succeeds`() =
        runTest {
            coEvery { getUserRatedMoviesUseCase.getRatedMovies() } returns listOf(fakeUserRatedMovie)
            coEvery { deleteUserRatedMoviesUseCase.deleteMovieRate(any()) } returns Unit

            viewModel.effect.test {
                viewModel.onClickDeleteMyMovieRatingIcon(fakeUserRatedMovie.movie.id)
                assertThat(awaitItem()).isEqualTo(MyRatingUiEffect.ShowDeleteRateSuccessSnackBar)
            }
        }

    @Test
    fun `onClickDeleteMyMovieRatingIcon should Show Error Snack Bar when Delete Fail`() = runTest {
        coEvery { getUserRatedMoviesUseCase.getRatedMovies() } returns listOf(fakeUserRatedMovie)
        coEvery { deleteUserRatedMoviesUseCase.deleteMovieRate(any()) } throws NoInternetException()

        viewModel.effect.test {
            viewModel.onClickDeleteMyMovieRatingIcon(fakeUserRatedMovie.movie.id)
            assertThat(awaitItem()).isEqualTo(MyRatingUiEffect.ShowDeleteRateErrorSnackBar)
        }
    }
    //endregion

    //region Tests for Delete Tv Show
    @Test
    fun `onClickDeleteMyTvShowRatingIcon should Remove TvShow From State`() =
        runTest {
            coEvery { getUserRatedTvShowsUseCase.getRatedTvShows() } returns listOf(
                fakeUserRatedTvShow
            )
            coEvery { deleteUserRatedTvShowUseCase.deleteTvShowRate(any()) } returns Unit

            viewModel.onClickDeleteMyTvShowRatingIcon(fakeUserRatedMovie.movie.id)
            advanceUntilIdle()

            assertThat(viewModel.state.value.tvShows).doesNotContain(fakeUserRatedTvShow)
        }

    @Test
    fun `onClickDeleteMyTvShowRatingIcon should Show Success Snack Bar when Delete Succeeds`() =
        runTest {
            coEvery { getUserRatedTvShowsUseCase.getRatedTvShows() } returns listOf(
                fakeUserRatedTvShow
            )
            coEvery { deleteUserRatedTvShowUseCase.deleteTvShowRate(any()) } returns Unit

            viewModel.effect.test {
                viewModel.onClickDeleteMyTvShowRatingIcon(fakeUserRatedTvShow.tvShow.id)
                assertThat(awaitItem()).isEqualTo(MyRatingUiEffect.ShowDeleteRateSuccessSnackBar)
            }
        }

    @Test
    fun `onClickDeleteMyTvShowRatingIcon should Show Error Snack Bar when Delete Fail`() = runTest {
        coEvery { getUserRatedTvShowsUseCase.getRatedTvShows() } returns listOf(fakeUserRatedTvShow)
        coEvery { deleteUserRatedTvShowUseCase.deleteTvShowRate(any()) } throws NoInternetException()

        viewModel.effect.test {
            viewModel.onClickDeleteMyTvShowRatingIcon(fakeUserRatedTvShow.tvShow.id)
            assertThat(awaitItem()).isEqualTo(MyRatingUiEffect.ShowDeleteRateErrorSnackBar)
        }
    }
    //endregion

    //region Tests for Navigation from My Rate Screen
    @Test
    fun `onClickMovieCard should send NavigateToMovieDetails effect`() = runTest {
        viewModel.effect.test {
            viewModel.onClickMovieCard(fakeUserRatedMovie.movie.id)
            assertThat(awaitItem())
                .isEqualTo(MyRatingUiEffect.NavigateToMovieDetails(fakeUserRatedMovie.movie.id))
        }
    }

    @Test
    fun `onClickTvShowCard should send NavigateToTvShowDetails effect`() = runTest {
        viewModel.effect.test {
            viewModel.onClickMovieCard(fakeUserRatedTvShow.tvShow.id)
            assertThat(awaitItem())
                .isEqualTo(MyRatingUiEffect.NavigateToMovieDetails(fakeUserRatedTvShow.tvShow.id))
        }
    }

    @Test
    fun `onClickNavigateBack should send NavigateBack effect`() = runTest {
        viewModel.effect.test {
            viewModel.onClickNavigateBack()
            assertThat(awaitItem()).isEqualTo(MyRatingUiEffect.NavigateBack)
        }
    }
    //endregion

    //region Tests for Taps
    @Test
    fun `onChangeTabOption should do nothing when selectedTabOption is same as current`() =
        runTest {
            val sameTab = TabOption.MOVIES

            viewModel.onChangeTabOption(sameTab)

            assertThat(viewModel.state.value.selectedTabOption).isEqualTo(sameTab)
        }

    @Test
    fun `onChangeTabOption should update selectedTabOption when tab changes`() = runTest {
        val newTab = TabOption.TV_SHOWS

        viewModel.onChangeTabOption(newTab)
        advanceUntilIdle()

        assertThat(viewModel.state.value.selectedTabOption).isEqualTo(newTab)
    }
    //endregion

    //region Tests for Refresh Content
    @Test
    fun `refreshRatedContent loads movies when selectedTabOption is MOVIES`() = runTest {
        coEvery { getUserRatedMoviesUseCase.getRatedMovies() } returns listOf(fakeUserRatedMovie)

        viewModel.refreshRatedContent()
        advanceUntilIdle()

        assertThat(viewModel.state.value.movies).isNotEmpty()
    }

    @Test
    fun `refreshRatedContent loads tvShows when selectedTabOption is TV_SHOWS`() = runTest {
        coEvery { getUserRatedTvShowsUseCase.getRatedTvShows() } returns listOf(fakeUserRatedTvShow)

        viewModel.onChangeTabOption(TabOption.TV_SHOWS)
        advanceUntilIdle()

        viewModel.refreshRatedContent()
        advanceUntilIdle()

        assertThat(viewModel.state.value.tvShows).isNotEmpty()
    }
    //endregion

    @Test
    fun `onClickRetryRequest should Reload Movies when Current Tab Is Movies`() = runTest {
        coEvery { getUserRatedMoviesUseCase.getRatedMovies() } returns listOf(fakeUserRatedMovie)

        viewModel.onClickRetryRequest()
        advanceUntilIdle()

        coVerify(exactly = 2) { getUserRatedMoviesUseCase.getRatedMovies() } // init + retry
    }

    companion object {
        private val fakeUserRatedMovie = UserRatedMovie(
            movie = Movie(
                id = 1L,
                name = "Fake Movie",
                description = "A fake movie description",
                posterUrl = "https://example.com/fake-movie.jpg",
                releaseDate = LocalDate(2025, 8, 8),
                categories = listOf(MovieGenre.ACTION, MovieGenre.DRAMA).map { it.name },
                rating = 8.5f,
                popularity = 123.45,
                originCountry = "US",
                runTimeInMinutes = 120,
                videoUrl = "https://example.com/fake-movie.mp4"
            ),
            userRate = 9
        )

        private val fakeUserRatedTvShow = UserRatedTvShow(
            tvShow = TvShow(
                id = 2L,
                name = "Fake Show",
                description = "A fake TV show description",
                posterUrl = "https://example.com/fake-show.jpg",
                airDate = LocalDate(2025, 8, 8),
                categories = listOf(TvShowGenre.ACTION_ADVENTURE, TvShowGenre.DRAMA).map { it.name },
                rating = 8.0f,
                popularity = 98.76,
                seasonCount = 3,
                originCountry = "UK",
                videoUrl = "https://example.com/fake-show.mp4"
            ),
            userRate = 8
        )
    }

}
