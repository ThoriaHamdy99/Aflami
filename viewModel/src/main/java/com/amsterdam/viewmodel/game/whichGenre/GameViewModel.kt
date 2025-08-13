package com.amsterdam.viewmodel.game.whichGenre

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.timer.TimerHandler
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.useCase.game.whichGenre.GuessMovieGenreUseCase
import com.amsterdam.entity.Game
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.sharedGame.TimerUiState
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel
@Inject
constructor(
    private val getGameDifficultyByDifficultyTypeUseCase: GetGameDifficultyByDifficultyTypeUseCase,
    private val guessMovieGenreGameEngin: GuessMovieGenreUseCase,
    private val timerHandler: TimerHandler,
    private val args: GameArgs,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<GameUiState, GameEffect>(
    GameUiState(),
    dispatcherProvider,
),
    GameInteractionListener {
    init {
        startWhichGenreGame()
    }

    private fun startWhichGenreGame() {
        viewModelScope.launch {
            val difficulty = getGameDifficultyByDifficultyTypeUseCase(args.difficulty)
            val questions = guessMovieGenreGameEngin.startGame(
                difficulty.difficultyType,
                { timeLeft ->
                    val timerColor = when (timeLeft) {
                        in 0..(difficulty.timeLimitSeconds / 3) -> TimerUiState.TimerColor.RED
                        else -> TimerUiState.TimerColor.GREEN
                    }
                    updateState {
                        it.copy(
                            timerUiState = state.value.timerUiState.copy(
                                currentTimerCount = timeLeft,
                                progress = (timeLeft.toFloat() / difficulty.timeLimitSeconds),
                                currentTimerColor = timerColor
                            ),
                        )
                    }
                },
                {
                    sendNewEffect(GameEffect.GameOver)
                }
            )

            val questionUiStates = questions.map {
                GameQuestionUiState(
                    id = it.id,
                    questionData = it.question,
                    answers = it.genreChoices.map { it.name },
                )
            }
            updateState {
                it.copy(
                    questions = questionUiStates,
                )
            }
        }
    }

    override fun onCancelGameClick() {
        sendNewEffect(GameEffect.CancelGame)
    }

    override fun onChooseAnswerClick(answerIndex: Int, answer: String, questionId: Long) {
        val isCorrect = guessMovieGenreGameEngin.checkUserAnswer(answer = answer, questionId = questionId)
        updateState {
            it.copy(
                selectedAnswerIndex = answerIndex,
                isAnswerCorrect = isCorrect,
                isNextEnabled = true,
            )
        }
    }

    override fun onUseHint() {
        when (state.value.gameType) {
            Game.GameType.GUESS_CHARACTER, Game.GameType.GUESS_MOVIE_BY_POSTER -> {
                updateState { it.copy(isHintEnabled = false) }
            }

            Game.GameType.GUESS_MOVIE_BY_RELEASE, Game.GameType.GUESS_MOVIE_BY_GENRE -> {
                val currentQuestion = state.value.questions[state.value.currentQuestionIndex]
                val updatedQuestion = guessMovieGenreGameEngin.useHint(currentQuestion.id)
                val updatedQuestionUiState = GameQuestionUiState(
                    id = updatedQuestion.id,
                    questionData = updatedQuestion.question,
                    answers = updatedQuestion.genreChoices.map { it.name },
                )
                updateState {
                    it.copy(
                        questions =
                            it.questions
                                .toMutableList()
                                .apply { set(state.value.currentQuestionIndex, updatedQuestionUiState) },
                        isHintEnabled = false,
                    )
                }
            }
        }
    }

    override fun onMoveToNextQuestion() {
        val currentQuestionIndex = state.value.currentQuestionIndex
        val nextQuestionIndex = currentQuestionIndex + 1
        if (nextQuestionIndex < state.value.questions.size) {
            updateState {
                it.copy(
                    currentQuestionIndex = nextQuestionIndex,
                    selectedAnswerIndex = null,
                    isAnswerCorrect = null,
                    isNextEnabled = false,
                )
            }
        } else {
            sendNewEffect(GameEffect.GameOver)
        }
    }
}
