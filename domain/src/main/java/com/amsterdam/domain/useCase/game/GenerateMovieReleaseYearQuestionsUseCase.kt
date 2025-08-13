package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.repository.GameRepository

class GenerateMovieReleaseYearQuestionsUseCase(
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(questionCount: Int): List<MovieReleasedDateQuestion> {
        val movies = gameRepository.getRandomMoviesWithNotNullDate(questionCount)

        return movies.map { movie ->
            val correctYear = movie.releaseDate!!.year
            val choices = generateYearChoices(correctYear)
            MovieReleasedDateQuestion(
                question = movie.name,
                releaseYearChoices = choices,
                correctChoice = correctYear
            )
        }
    }

    private fun generateYearChoices(correctYear: Int, numberOfChoices: Int = 4): List<Int> {
        val choices = mutableSetOf(correctYear)

        while (choices.size < numberOfChoices) {
            val randomYear = (correctYear - 10..correctYear + 10).random()
            choices.add(randomYear)
        }

        return choices.shuffled()
    }

    data class MovieReleasedDateQuestion(
        val question: String,
        val releaseYearChoices: List<Int>,
        val correctChoice: Int
    )
}