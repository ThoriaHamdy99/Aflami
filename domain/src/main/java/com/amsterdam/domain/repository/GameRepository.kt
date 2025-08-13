package com.amsterdam.domain.repository

import com.amsterdam.entity.Movie
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    suspend fun getRandomMoviesWithNotNullDate(requiredMoviesNumber: Int): List<Movie>

    suspend fun updatePoints(points: Int)
    fun getUserPoints(): Flow<Int>
}