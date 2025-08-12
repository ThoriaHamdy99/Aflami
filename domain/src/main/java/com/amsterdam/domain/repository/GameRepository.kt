package com.amsterdam.domain.repository

import com.amsterdam.entity.Movie

interface GameRepository {
    suspend fun getTotalUserPoints(): Int
    suspend fun getRandomMoviesWithNotNullDate(requiredMoviesNumber: Int): List<Movie>
}