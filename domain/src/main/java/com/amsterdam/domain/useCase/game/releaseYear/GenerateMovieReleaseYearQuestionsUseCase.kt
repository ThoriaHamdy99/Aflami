package com.amsterdam.domain.useCase.game.releaseYear

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.entity.GameDifficulty
import com.amsterdam.entity.GameDifficulty.DifficultyType

class GenerateMovieReleaseYearQuestionsUseCase(
    private val gameRepository: GameRepository,
    private val getGameDifficultyByDifficultyTypeUseCase: GetGameDifficultyByDifficultyTypeUseCase
) {
    suspend operator fun invoke(difficultyType: DifficultyType): List<MovieReleasedDateQuestion> {
      val questionsCounts =  getGameDifficultyByDifficultyTypeUseCase(difficultyType).totalQuestions
        val movies = gameRepository.getRandomMoviesWithNotNullDate(questionsCounts)

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