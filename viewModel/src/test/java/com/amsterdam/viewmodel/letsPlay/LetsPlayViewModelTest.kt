package com.amsterdam.viewmodel.letsPlay

import app.cash.turbine.test
import com.amsterdam.domain.useCase.game.GetAvailableGamesUseCase
import com.amsterdam.domain.useCase.game.GetTotalUserPointsUseCase
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameDifficultyUiState
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameUiState
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.amsterdam.viewmodel.utils.TestExtension
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(TestExtension::class)
class LetsPlayViewModelTest {

    private val getTotalUserPointsUseCase: GetTotalUserPointsUseCase = mockk()
    private val getAvailableGamesUseCase: GetAvailableGamesUseCase = mockk()

    private val viewModel by lazy {
        LetsPlayViewModel(
            getTotalUserPointsUseCase = getTotalUserPointsUseCase,
            getAvailableGamesUseCase = getAvailableGamesUseCase,
            dispatcherProvider = TestDispatcherProvider()
        )
    }

    private fun toLetsPlayUiState(): LetsPlayUiState {
        return LetsPlayUiState(
            games = testGamesUiState,
            difficulties = testDifficultiesUiState
        )
    }

    @BeforeEach
    fun setUp() {
        coEvery { getTotalUserPointsUseCase() } returns flowOf(1000)
        coEvery { getAvailableGamesUseCase() } returns GetAvailableGamesUseCase.AvailableGames(emptyList(), emptyList())
    }

    @Test
    fun `init should update state with total user points on success`() = runTest {
        val expectedPoints = 2500
        coEvery { getTotalUserPointsUseCase() } returns flowOf(expectedPoints)

        viewModel
        advanceUntilIdle()

        assertThat(viewModel.state.value.totalUserPoint).isEqualTo(expectedPoints)
    }

    @Test
    fun `init should update state with available games and difficulties on success`() = runTest {
        val availableGamesResponse = GetAvailableGamesUseCase.AvailableGames(emptyList(), emptyList())
        coEvery { getAvailableGamesUseCase() } returns availableGamesResponse

        viewModel
        advanceUntilIdle()

        val expectedUiState = availableGamesResponse.toLetsPlayUiState()
        assertThat(viewModel.state.value.games).isEqualTo(expectedUiState.games)
        assertThat(viewModel.state.value.difficulties).isEqualTo(expectedUiState.difficulties)
    }

    @Test
    fun `init should handle exception from getTotalUserPointsUseCase`() = runTest {
        coEvery { getTotalUserPointsUseCase() } throws RuntimeException("Failed to fetch points")

        val initialPoints = viewModel.state.value.totalUserPoint
        viewModel
        advanceUntilIdle()
        assertThat(viewModel.state.value.totalUserPoint).isEqualTo(initialPoints)
    }

    @Test
    fun `onClickGameCard should update state with the selected game type`() = runTest {
        val selectedGameType = GameUiState.GameTypeUiState.GUESS_MOVIE_BY_POSTER

        viewModel.onClickGameCard(selectedGameType)
        advanceUntilIdle()

        assertThat(viewModel.state.value.selectedGameTypeUiState).isEqualTo(selectedGameType)
    }

    @Test
    fun `onSelectDifficultyLevel should update state with selected difficulty and enable start button`() = runTest {
        val selectedDifficulty = testDifficultiesUiState.first { it.difficultyLevel == GameDifficultyUiState.DifficultyLevelUiState.HARD }

        viewModel.onSelectDifficultyLevel(selectedDifficulty)
        advanceUntilIdle()

        assertThat(viewModel.state.value.selectedDifficultyLevel).isEqualTo(selectedDifficulty)
        assertThat(viewModel.state.value.isStartGameButtonEnable).isTrue()
    }

    @Test
    fun `onClickCloseDifficultyLevelDialog should reset selections and disable start button`() = runTest {
        viewModel.onClickGameCard(GameUiState.GameTypeUiState.GUESS_CHARACTER)
        viewModel.onSelectDifficultyLevel(easyDifficulty)
        advanceUntilIdle()

        viewModel.onClickCloseDifficultyLevelDialog()
        advanceUntilIdle()

        assertThat(viewModel.state.value.selectedGameTypeUiState).isNull()
        assertThat(viewModel.state.value.selectedDifficultyLevel).isNull()
        assertThat(viewModel.state.value.isStartGameButtonEnable).isFalse()
    }

    @Test
    fun `onClickStartGame should send NavigateToGuessCharacterScreen effect when selected`() = runTest {
        viewModel.onClickGameCard(GameUiState.GameTypeUiState.GUESS_CHARACTER)
        viewModel.onSelectDifficultyLevel(mediumDifficulty)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onClickStartGame()
            val expectedEffect = LetsPlayEffect.NavigateToGuessCharacterScreen(mediumDifficulty.difficultyLevel.name)
            assertThat(awaitItem()).isEqualTo(expectedEffect)
        }
    }

    @Test
    fun `onClickStartGame should send NavigateToGuessMovieByPosterScreen effect when selected`() = runTest {
        viewModel.onClickGameCard(GameUiState.GameTypeUiState.GUESS_MOVIE_BY_POSTER)
        viewModel.onSelectDifficultyLevel(easyDifficulty)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onClickStartGame()
            val expectedEffect = LetsPlayEffect.NavigateToGuessMovieByPosterScreen(easyDifficulty.difficultyLevel.name)
            assertThat(awaitItem()).isEqualTo(expectedEffect)
        }
    }

    @Test
    fun `onClickStartGame should do nothing if difficulty level is not selected`() = runTest {
        viewModel.onClickGameCard(GameUiState.GameTypeUiState.GUESS_CHARACTER)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onClickStartGame()
            expectNoEvents()
        }
    }

    @Test
    fun `onClickStartGame should reset state after sending navigation effect`() = runTest {
        viewModel.onClickGameCard(GameUiState.GameTypeUiState.GUESS_MOVIE_BY_GENRE)
        viewModel.onSelectDifficultyLevel(hardDifficulty)
        advanceUntilIdle()

        viewModel.onClickStartGame()
        advanceUntilIdle()

        assertThat(viewModel.state.value.selectedGameTypeUiState).isNull()
        assertThat(viewModel.state.value.selectedDifficultyLevel).isNull()
        assertThat(viewModel.state.value.isStartGameButtonEnable).isFalse()
    }

    private val easyDifficulty = GameDifficultyUiState(
        totalQuestions = 10,
        timeLimitSeconds = 30,
        pointsPerQuestion = 10,
        difficultyLevel = GameDifficultyUiState.DifficultyLevelUiState.EASY
    )
    private val mediumDifficulty = GameDifficultyUiState(
        totalQuestions = 15,
        timeLimitSeconds = 20,
        pointsPerQuestion = 20,
        difficultyLevel = GameDifficultyUiState.DifficultyLevelUiState.MEDIUM
    )
    private val hardDifficulty = GameDifficultyUiState(
        totalQuestions = 20,
        timeLimitSeconds = 15,
        pointsPerQuestion = 30,
        difficultyLevel = GameDifficultyUiState.DifficultyLevelUiState.HARD
    )

    private val testDifficultiesUiState = listOf(easyDifficulty, mediumDifficulty, hardDifficulty)

    private val testGamesUiState = listOf(
        GameUiState(GameUiState.GameTypeUiState.GUESS_CHARACTER, requiredPoints = 0),
        GameUiState(GameUiState.GameTypeUiState.GUESS_MOVIE_BY_POSTER, requiredPoints = 500),
        GameUiState(GameUiState.GameTypeUiState.GUESS_MOVIE_BY_RELEASE, requiredPoints = 1000),
        GameUiState(GameUiState.GameTypeUiState.GUESS_MOVIE_BY_GENRE, requiredPoints = 1500)
    )

    @Test
    fun `onClickStartGame should send NavigateToGuessMovieByReleaseScreen effect when selected`() = runTest {
        viewModel.onClickGameCard(GameUiState.GameTypeUiState.GUESS_MOVIE_BY_RELEASE)

        viewModel.onSelectDifficultyLevel(hardDifficulty)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onClickStartGame()
            val expectedEffect = LetsPlayEffect.NavigateToGuessMovieByReleaseScreen(hardDifficulty.difficultyLevel.name)
            assertThat(awaitItem()).isEqualTo(expectedEffect)
        }
    }
}