package com.amsterdam.domain.useCase.game.guessByPoster

import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.useCase.game.GetTotalUserPointsUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import kotlinx.coroutines.flow.first

class DoGuessMovieByPosterHintUseCase(
    private val getTotalUserPointsUseCase: GetTotalUserPointsUseCase,
    private val updatePoints: UpdateUserGamePointsUseCase
) {
    suspend operator fun invoke(
        moviePosterQuestion: MoviePosterQuestion
    ): MoviePosterQuestion {

        val currentPoints = getTotalUserPointsUseCase().first()

        if (currentPoints < REQUIRED_HINT_POINTS) {
            throw NotEnoughPointsException()
        }

        val choices = moviePosterQuestion.movieNameChoices.toMutableList()

        val wrongChoiceToRemove = choices
            .filter { it != moviePosterQuestion.correctMovieName }
            .randomOrNull()
        wrongChoiceToRemove?.let {
            choices.remove(it)
            updatePoints(-REQUIRED_HINT_POINTS)
        }

        return moviePosterQuestion.copy(movieNameChoices = choices)
    }

    companion object {
        private const val REQUIRED_HINT_POINTS = 10
    }
}