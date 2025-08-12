package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.entity.Movie
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.mapper.remote.toMovieEntityList
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    //   private val gameLocalDataSource: GameLocalDataSource,
    private val movieRemoteSource: MovieRemoteSource
) : GameRepository {

    override suspend fun getTotalUserPoints(): Int {
        return 100
    }

    override suspend fun getRandomMoviesWithNotNullDate(requiredMoviesNumber: Int): List<Movie> {
        val totalPages = getTotalPagesForPopularMovies()
        val collectedMovies = mutableListOf<Movie>()
        val usedPages = mutableSetOf<Int>()

        while (collectedMovies.size < requiredMoviesNumber && usedPages.size < totalPages) {
            val randomPage = (1..totalPages).random().also { usedPages.add(it) }
            val pageMovies = getPopularMoviesByPage(randomPage)
                .filter { it.releaseDate != null }

            for (movie in pageMovies) {
                if (!collectedMovies.contains(movie)) {
                    collectedMovies.add(movie)
                    if (collectedMovies.size == requiredMoviesNumber) break
                }
            }
        }

        return collectedMovies
    }

    private suspend fun getTotalPagesForPopularMovies(): Int {
        return movieRemoteSource.getPopularMovies(page = 1).totalPages
    }

    private suspend fun getPopularMoviesByPage(page: Int): List<Movie> {
        val random = (1..50).random()
        return movieRemoteSource.getPopularMovies(page = random).results.toMovieEntityList()
    }

}