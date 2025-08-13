package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.repository.datasource.local.GameLocalDataSource
import javax.inject.Inject

class GameLocalDataSourceImpl @Inject constructor(    private val gamePointsDao: GamePointsDao
) : GamePointsLocalDataSource {
    override suspend fun upsertPoints(points: Int) {
        gamePointsDao.upsertPoints(GamePointsDto(points = points))
    }

    override fun getPoints(): Flow<Int?> {
        return gamePointsDao.getPoints()
    }
}