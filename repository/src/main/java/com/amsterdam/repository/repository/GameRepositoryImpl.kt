package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.entity.Movie
import com.amsterdam.repository.datasource.local.GameLocalDataSource
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.mapper.remote.toMovieEntityList
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val gamePointsLocalDataSource: GameLocalDataSource,
    private val movieRemoteSource: MovieRemoteSource
) : GameRepository {

  override suspend fun getRandomMoviesWithNotNullDate(requiredMoviesNumber: Int): List<Movie> {
   return movieRemoteSource.getRandomMoviesWithNotNullDate(requiredMoviesNumber).toMovieEntityList()
  }

    override suspend fun updatePoints(points: Int) {
        gamePointsLocalDataSource.upsertPoints(points)
    }

    override suspend fun getUserPoints(): Int {
        return gamePointsLocalDataSource.getPoints()
    }
}