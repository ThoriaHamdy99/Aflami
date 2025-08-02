package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.MovieCategoryCrossRefDto
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Dao
interface MovieDao {

    @Upsert
    suspend fun insertMovie(movies: LocalMovieDto)

    @Upsert
    suspend fun insertMovieCategoryCrossRefs(crossRefs: List<MovieCategoryCrossRefDto>)

    @Query(" SELECT * FROM ${DatabaseConstants.MOVIE_TABLE} WHERE movieId = :movieId and storedLanguage = :storedLanguage")
    suspend fun getMovieById(movieId: Long, storedLanguage: String): LocalMovieDto?
}
