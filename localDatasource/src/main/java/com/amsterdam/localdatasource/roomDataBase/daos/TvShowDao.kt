package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Dao
interface TvShowDao {
    @Upsert
    suspend fun insertTvShow(tvShow: LocalTvShowDto)

    @Query(" SELECT * FROM ${DatabaseConstants.TV_SHOW_TABLE} WHERE tvShowId = :tvShowId and storedLanguage = :storedLanguage")
    suspend fun getTvShowById(tvShowId: Long, storedLanguage: String): LocalTvShowDto?
}