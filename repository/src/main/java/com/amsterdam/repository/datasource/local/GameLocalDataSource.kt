package com.amsterdam.repository.datasource.local

interface GameLocalDataSource {

    suspend fun upsertPoints(points: Int)
    suspend fun getUserPoints(): Int
}