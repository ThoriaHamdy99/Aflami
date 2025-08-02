package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.MovieCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.MovieDao
import com.amsterdam.repository.datasource.local.MovieLocalSource
import com.amsterdam.repository.dto.local.LocalMovieDto
import javax.inject.Inject


class MovieLocalDataSourceImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val interestDao: MovieCategoryInterestDao
) : MovieLocalSource {
    override suspend fun getMovieById(
        movieId: Long,
        storedLanguage: String
    ): LocalMovieDto? {
        return movieDao.getMovieById(movieId, storedLanguage)
    }

    override suspend fun incrementGenreInterest(categoryId: Long) {
        interestDao.incrementInterest(categoryId)
    }

    override suspend fun insertMovie(movie: LocalMovieDto) {
        movieDao.insertMovie(movie)
    }
}
