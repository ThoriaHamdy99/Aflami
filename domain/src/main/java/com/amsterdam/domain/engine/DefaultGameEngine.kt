package com.amsterdam.domain.engine

import com.amsterdam.domain.strategies.QuestionProvider
import com.amsterdam.domain.strategies.HintStrategy
import com.amsterdam.domain.strategies.ScoringPolicy
import com.amsterdam.entity.Game.GameType
import com.amsterdam.domain.models.gameModels.AnswerOption
import com.amsterdam.domain.models.gameModels.GameConfig
import com.amsterdam.domain.models.gameModels.GameResult
import com.amsterdam.domain.models.gameModels.GameSession
import com.amsterdam.domain.models.gameModels.GameSessionState
import com.amsterdam.domain.models.gameModels.GameStatus
import com.amsterdam.domain.models.gameModels.HintState
import com.amsterdam.domain.models.gameModels.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

class DefaultGameEngine(
    private val questionProvider: QuestionProvider,
    private val hintStrategy: HintStrategy,
    private val scoringPolicy: ScoringPolicy
) : GameEngine {

    private val _state = MutableStateFlow(
        GameSessionState(
            status = GameStatus.Idle,
            session = null,
            timeLeftSeconds = 0,
            currentQuestion = null,
            options = emptyList(),
            hintState = HintState.None
        )
    )
    override val state: StateFlow<GameSessionState> = _state.asStateFlow()

    private var currentSession: GameSession? = null
    private var timeLeftSeconds: Int = 0
    private var totalTimeSpent: Int = 0
    private var sessionStartTime: Long = 0
    private var correctAnswersCount: Int = 0

    override suspend fun start(config: GameConfig) {
        try {
            _state.value = _state.value.copy(status = GameStatus.Loading)

            val questions = questionProvider.generateQuestions(config)
            if (questions.isEmpty()) {
                throw IllegalStateException("No questions generated for game type: ${config.type}")
            }

            val timePerQuestion = scoringPolicy.getTimePerQuestion(config.difficulty)

            currentSession = GameSession(
                id = UUID.randomUUID().toString(),
                config = config,
                questions = questions,
                currentIndex = 0,
                score = 0,
                hintUsedForQuestionIds = emptySet(),
                removedOptionIdsByQuestion = emptyMap()
            )

            timeLeftSeconds = timePerQuestion
            totalTimeSpent = 0
            sessionStartTime = System.currentTimeMillis()
            correctAnswersCount = 0

            val currentQuestion = questions[0]
            val options = getCurrentOptions(currentQuestion, emptySet())

            _state.value = GameSessionState(
                status = GameStatus.Active,
                session = currentSession!!,
                timeLeftSeconds = timeLeftSeconds,
                currentQuestion = currentQuestion,
                options = options,
                hintState = HintState.None
            )

        } catch (e: Exception) {
            _state.value = _state.value.copy(
                status = GameStatus.Idle,
                session = null
            )
            throw e
        }
    }

    override fun submitAnswer(optionId: String) {
        val session = currentSession ?: return
        val currentQuestion = session.questions[session.currentIndex]

        if (optionId !in currentQuestion.options.map { it.id }) {
            return // Invalid option
        }

        val isCorrect = optionId == currentQuestion.correctOptionId
        if (isCorrect) {
            correctAnswersCount++
        }

        val pointsEarned = if (isCorrect) {
            scoringPolicy.getPointsPerCorrect(session.config.difficulty)
        } else 0

        val updatedSession = session.copy(score = session.score + pointsEarned)
        currentSession = updatedSession

        _state.value = GameSessionState(
            status = GameStatus.QuestionAnswered,
            session = updatedSession,
            timeLeftSeconds = timeLeftSeconds,
            currentQuestion = currentQuestion,
            options = getCurrentOptions(currentQuestion, getRemovedOptions(currentQuestion.id)),
            hintState = getCurrentHintState(currentQuestion.id),
            selectedOptionId = optionId,
            isCorrect = isCorrect,
            pointsEarned = pointsEarned
        )
    }

    override fun useHint() {
        val session = currentSession ?: return
        val currentQuestion = session.questions[session.currentIndex]

        // Check if hint already used for this question
        if (session.hintUsedForQuestionIds.contains(currentQuestion.id)) {
            return // Hint already used
        }

        val updatedSession = hintStrategy.applyHint(session)
        currentSession = updatedSession

        val hintState = when (session.config.type) {
            GameType.GUESS_CHARACTER, GameType.GUESS_MOVIE_BY_POSTER -> {
                HintState.BlurReduced
            }
            else -> {
                val removedOptions = getRemovedOptions(currentQuestion.id)
                HintState.OptionRemoved(removedOptions)
            }
        }

        _state.value = GameSessionState(
            status = GameStatus.Active,
            session = updatedSession,
            timeLeftSeconds = timeLeftSeconds,
            currentQuestion = currentQuestion,
            options = getCurrentOptions(currentQuestion, getRemovedOptions(currentQuestion.id)),
            hintState = hintState
        )
    }

    override fun skip() {
        next()
    }

    override fun next() {
        val session = currentSession ?: return

        if (session.currentIndex >= session.questions.size - 1) {
            finish()
        } else {
            val nextIndex = session.currentIndex + 1
            val timePerQuestion = scoringPolicy.getTimePerQuestion(session.config.difficulty)

            currentSession = session.copy(currentIndex = nextIndex)
            timeLeftSeconds = timePerQuestion

            val nextQuestion = session.questions[nextIndex]
            val options = getCurrentOptions(nextQuestion, getRemovedOptions(nextQuestion.id))

            _state.value = GameSessionState(
                status = GameStatus.Active,
                session = currentSession!!,
                timeLeftSeconds = timeLeftSeconds,
                currentQuestion = nextQuestion,
                options = options,
                hintState = HintState.None
            )
        }
    }

    override fun tick(deltaMillis: Long) {
        if (currentSession == null || _state.value.status != GameStatus.Active) {
            return
        }

        val deltaSeconds = (deltaMillis / 1000).toInt()
        timeLeftSeconds -= deltaSeconds
        totalTimeSpent += deltaSeconds

        if (timeLeftSeconds <= 0) {
            // Auto-advance to next question on timeout
            next()
        } else {
            _state.value = _state.value.copy(timeLeftSeconds = timeLeftSeconds)
        }
    }

    override fun finish() {
        val session = currentSession ?: return

        val result = GameResult(
            sessionId = session.id,
            score = session.score,
            totalTimeSpent = totalTimeSpent,
            questionsAnswered = session.currentIndex + 1,
            correctAnswers = correctAnswersCount
        )

        _state.value = GameSessionState(
            status = GameStatus.Finished,
            session = session,
            timeLeftSeconds = 0,
            currentQuestion = null,
            options = emptyList(),
            hintState = HintState.None
        )

        // Reset for next game
        currentSession = null
        timeLeftSeconds = 0
        totalTimeSpent = 0
        sessionStartTime = 0
        correctAnswersCount = 0
    }

    override fun reset() {
        currentSession = null
        timeLeftSeconds = 0
        totalTimeSpent = 0
        sessionStartTime = 0
        correctAnswersCount = 0

        _state.value = GameSessionState(
            status = GameStatus.Idle,
            session = null,
            timeLeftSeconds = 0,
            currentQuestion = null,
            options = emptyList(),
            hintState = HintState.None
        )
    }

    // Private helper methods
    private fun getCurrentOptions(question: Question, removedOptionIds: Set<String>): List<AnswerOption> {
        return question.options.filter { it.id !in removedOptionIds }
    }

    private fun getRemovedOptions(questionId: String): Set<String> {
        return currentSession?.removedOptionIdsByQuestion?.get(questionId) ?: emptySet()
    }

    private fun getCurrentHintState(questionId: String): HintState {
        val session = currentSession ?: return HintState.None

        return if (session.hintUsedForQuestionIds.contains(questionId)) {
            when (session.config.type) {
                GameType.GUESS_CHARACTER, GameType.GUESS_MOVIE_BY_POSTER -> HintState.BlurReduced
                else -> {
                    val removedOptions = getRemovedOptions(questionId)
                    HintState.OptionRemoved(removedOptions)
                }
            }
        } else {
            HintState.None
        }
    }
}
