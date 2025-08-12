@file:OptIn(ExperimentalUuidApi::class)

package com.amsterdam.domain.useCase.game.whichGenre

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.entity.category.MovieGenre
import kotlin.uuid.ExperimentalUuidApi

class GetGenreQuestionsUseCase(
    private val gameRepository: GameRepository,
) {
    suspend operator fun invoke(
        questionCount: Int,
        wrongAnswersCount: Int = 3,
    ): List<MovieGenreQuestion> {
        val movies = gameRepository.getRandomMoviesWithNotNullDate(questionCount)

        return movies.map { movie ->
            val correctGenre = movie.categories.random()
            val wrongGenres =
                MovieGenre
                    .entries
                    .drop(1)
                    .filter { !(movie.categories.contains(it)) }
                    .shuffled()
                    .take(wrongAnswersCount)
            val choices = (listOf(correctGenre) + wrongGenres).shuffled()

            MovieGenreQuestion(
                id = movie.id,
                question = movie.name,
                genreChoices = choices,
                correctChoice = correctGenre
            )
        }
    }

    data class MovieGenreQuestion(
        val id: Long,
        val question: String,
        val genreChoices: List<MovieGenre>,
        val correctChoice: MovieGenre
    )
}