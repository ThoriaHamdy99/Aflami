package com.amsterdam.domain.repository

import com.amsterdam.entity.Movie

interface GameRepository {

    suspend fun getRandomMoviesWithNotNullDate(requiredMoviesNumber: Int): List<Movie>
    suspend fun getRandomMoviesWithNotNullPoster(requiredMoviesNumber: Int): List<Movie>

    suspend fun updatePoints(points: Int)
    suspend fun getUserPoints(): Int
}