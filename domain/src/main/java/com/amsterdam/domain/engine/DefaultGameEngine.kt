package com.amsterdam.domain.engine

import com.amsterdam.domain.strategies.QuestionProvider
import com.amsterdam.domain.strategies.HintStrategy
import com.amsterdam.domain.strategies.ScoringPolicy
import com.amsterdam.entity.Game.GameType
import com.amsterdam.domain.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

class DefaultGameEngine(
    private val questionProvider: QuestionProvider,
    private val hintStrategy: HintStrategy,
    private val scoringPolicy: ScoringPolicy
) : GameEngine {

    // Immutable state container
    private data class EngineState(
        val session: GameSession? = null,
        val timeLeftSeconds: Int = 0,
        val totalTimeSpent: Int = 0,
        val sessionStartTime: Long = 0,
        val correctAnswersCount: Int = 0
    )

    private val _state = MutableStateFlow(createInitialState())
    override val state: StateFlow<GameSessionState> = _state.asStateFlow()
    
    private var engineState = EngineState()

    override suspend fun start(config: GameConfig) {
        try {
            updateState { copy(status = GameStatus.Loading) }
            
            val questions = questionProvider.generateQuestions(config)
            require(questions.isNotEmpty()) { "No questions generated for game type: ${config.type}" }
            
            val timePerQuestion = scoringPolicy.getTimePerQuestion(config.difficulty)
            val session = createGameSession(config, questions)
            
            engineState = EngineState(
                session = session,
                timeLeftSeconds = timePerQuestion,
                totalTimeSpent = 0,
                sessionStartTime = System.currentTimeMillis(),
                correctAnswersCount = 0
            )
            
            val currentQuestion = questions.first()
            val options = getCurrentOptions(currentQuestion, emptySet())
            
            updateState {
                copy(
                    status = GameStatus.Active,
                    session = session,
                    timeLeftSeconds = timePerQuestion,
                    currentQuestion = currentQuestion,
                    options = options,
                    hintState = HintState.None
                )
            }
            
        } catch (e: Exception) {
            updateState { copy(status = GameStatus.Idle, session = null) }
            throw e
        }
    }

    override fun submitAnswer(optionId: String) {
        val session = engineState.session ?: return
        val currentQuestion = session.getCurrentQuestion() ?: return
        
        if (!isValidOption(optionId, currentQuestion)) return
        
        val isCorrect = optionId == currentQuestion.correctOptionId
        val pointsEarned = if (isCorrect) {
            scoringPolicy.getPointsPerCorrect(session.config.difficulty)
        } else 0
        
        val updatedSession = updateSessionScore(session, pointsEarned)
        val updatedEngineState = if (isCorrect) {
            engineState.copy(
                session = updatedSession,
                correctAnswersCount = engineState.correctAnswersCount + 1
            )
        } else {
            engineState.copy(session = updatedSession)
        }
        
        engineState = updatedEngineState
        
        updateState {
            copy(
                status = GameStatus.QuestionAnswered,
                session = updatedSession,
                timeLeftSeconds = engineState.timeLeftSeconds,
                currentQuestion = currentQuestion,
                options = getCurrentOptions(currentQuestion, getRemovedOptions(currentQuestion.id)),
                hintState = getCurrentHintState(currentQuestion.id),
                selectedOptionId = optionId,
                isCorrect = isCorrect,
                pointsEarned = pointsEarned
            )
        }
    }

    override fun useHint() {
        val session = engineState.session ?: return
        val currentQuestion = session.getCurrentQuestion() ?: return
        
        if (session.hintUsedForQuestionIds.contains(currentQuestion.id)) return
        
        val updatedSession = hintStrategy.applyHint(session)
        engineState = engineState.copy(session = updatedSession)
        
        val hintState = createHintState(session.config.type, currentQuestion.id)
        
        updateState {
            copy(
                status = GameStatus.Active,
                session = updatedSession,
                timeLeftSeconds = engineState.timeLeftSeconds,
                currentQuestion = currentQuestion,
                options = getCurrentOptions(currentQuestion, getRemovedOptions(currentQuestion.id)),
                hintState = hintState
            )
        }
    }

    override fun skip() = next()

    override fun next() {
        val session = engineState.session ?: return
        
        if (session.isFinished()) {
            finish()
        } else {
            val nextIndex = session.currentIndex + 1
            val timePerQuestion = scoringPolicy.getTimePerQuestion(session.config.difficulty)
            
            val updatedSession = session.copy(currentIndex = nextIndex)
            engineState = engineState.copy(
                session = updatedSession,
                timeLeftSeconds = timePerQuestion
            )
            
            val nextQuestion = updatedSession.getCurrentQuestion() ?: return
            val options = getCurrentOptions(nextQuestion, getRemovedOptions(nextQuestion.id))
            
            updateState {
                copy(
                    status = GameStatus.Active,
                    session = updatedSession,
                    timeLeftSeconds = timePerQuestion,
                    currentQuestion = nextQuestion,
                    options = options,
                    hintState = HintState.None
                )
            }
        }
    }

    override fun tick(deltaMillis: Long) {
        if (engineState.session == null || !_state.value.isGameActive()) return
        
        val deltaSeconds = (deltaMillis / 1000).toInt()
        val newTimeLeft = (engineState.timeLeftSeconds - deltaSeconds).coerceAtLeast(0)
        val newTotalTime = engineState.totalTimeSpent + deltaSeconds
        
        engineState = engineState.copy(
            timeLeftSeconds = newTimeLeft,
            totalTimeSpent = newTotalTime
        )
        
        if (newTimeLeft <= 0) {
            next()
        } else {
            updateState { copy(timeLeftSeconds = newTimeLeft) }
        }
    }

    override fun finish() {
        val session = engineState.session ?: return
        
        val result = createGameResult(session)
        
        updateState {
            copy(
                status = GameStatus.Finished,
                session = session,
                timeLeftSeconds = 0,
                currentQuestion = null,
                options = emptyList(),
                hintState = HintState.None
            )
        }
        
        resetEngineState()
    }

    override fun reset() {
        resetEngineState()
        updateState { createInitialState() }
    }

    // Private helper methods - pure functions
    private fun createInitialState() = GameSessionState(
        status = GameStatus.Idle,
        session = null,
        timeLeftSeconds = 0,
        currentQuestion = null,
        options = emptyList(),
        hintState = HintState.None
    )

    private fun createGameSession(config: GameConfig, questions: List<Question>): GameSession {
        return GameSession(
            id = UUID.randomUUID().toString(),
            config = config,
            questions = questions,
            currentIndex = 0,
            score = 0,
            hintUsedForQuestionIds = emptySet(),
            removedOptionIdsByQuestion = emptyMap()
        )
    }

    private fun updateSessionScore(session: GameSession, pointsEarned: Int): GameSession {
        return session.copy(score = session.score + pointsEarned)
    }

    private fun isValidOption(optionId: String, question: Question): Boolean {
        return question.options.any { it.id == optionId }
    }

    private fun getCurrentOptions(question: Question, removedOptionIds: Set<String>): List<AnswerOption> {
        return question.options.filter { it.id !in removedOptionIds }
    }

    private fun getRemovedOptions(questionId: String): Set<String> {
        return engineState.session?.removedOptionIdsByQuestion?.get(questionId) ?: emptySet()
    }

    private fun getCurrentHintState(questionId: String): HintState {
        val session = engineState.session ?: return HintState.None
        
        return if (session.hintUsedForQuestionIds.contains(questionId)) {
            createHintState(session.config.type, questionId)
        } else {
            HintState.None
        }
    }

    private fun createHintState(gameType: GameType, questionId: String): HintState {
        return when (gameType) {
            GameType.GUESS_CHARACTER, GameType.GUESS_MOVIE_BY_POSTER -> HintState.BlurReduced
            else -> {
                val removedOptions = getRemovedOptions(questionId)
                HintState.OptionRemoved(removedOptions)
            }
        }
    }

    private fun createGameResult(session: GameSession): GameResult {
        return GameResult(
            sessionId = session.id,
            score = session.score,
            totalTimeSpent = engineState.totalTimeSpent,
            questionsAnswered = session.currentIndex + 1,
            correctAnswers = engineState.correctAnswersCount
        )
    }

    private fun resetEngineState() {
        engineState = EngineState()
    }

    private fun updateState(update: GameSessionState.() -> GameSessionState) {
        _state.value = _state.value.update()
    }
}
