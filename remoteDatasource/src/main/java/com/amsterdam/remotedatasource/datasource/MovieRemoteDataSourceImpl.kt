package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.dto.remote.RatingRemoteResponse
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.MovieDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.MovieRemoteResponse
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val movieApiService: MovieApiService,
) : MovieRemoteSource {

    override suspend fun getMoviesByKeyword(keyword: String, page: Int): MovieRemoteResponse {
        return responseCall { movieApiService.getMoviesByKeyword(keyword, page) }
    }

    override suspend fun getMoviesByActorIds(actorIds: List<Int>, page: Int): MovieRemoteResponse {
        val actorIdsAsString = actorIds.joinToString(separator = "|")
        return responseCall { movieApiService.getMoviesByActorId(actorIdsAsString) }
    }

    override suspend fun getActorIdsByName(name: String, page: Int): List<Int> {
        return responseCall { movieApiService.getActorIdByName(name, page) }
            .actors
            .map { it.id }
    }

    override suspend fun getMoviesByCountryIsoCode(
        countryIsoCode: String,
        page: Int
    ): MovieRemoteResponse {
        return responseCall { movieApiService.getMoviesByCountryIsoCode(countryIsoCode, page) }
    }

    override suspend fun getCastByMovieId(movieId: Long): CastAndCrewRemoteResponse {
        return responseCall { movieApiService.getCastByMovieId(movieId) }
    }

    override suspend fun getMovieDetailsById(movieId: Long): MovieDetailsRemoteResponse {
        return responseCall { movieApiService.getMovieDetailsById(movieId) }
    }

    override suspend fun getPopularMovies(): MovieRemoteResponse {
        return responseCall { movieApiService.getPopularMovies() }
    }

    override suspend fun getUpcomingMovies(): MovieRemoteResponse {
        return responseCall { movieApiService.getUpcomingMovies() }
    }

    override suspend fun getTopRatedMovies(page: Int): MovieRemoteResponse {
        return responseCall { movieApiService.getTopRatedMovies(page) }
    }

    override suspend fun getMoviesByGenreIds(
        genresIds: List<Long>,
        page: Int
    ): MovieRemoteResponse {
        return responseCall { movieApiService.getMoviesByGenreIds(genresIds, page) }
    }

    override suspend fun getMoviesByGenreId(
        genreId: Long,
        page: Int
    ): MovieRemoteResponse {
        return responseCall { movieApiService.getMoviesByGenreIds(listOf(genreId), page) }
    }

    override suspend fun setMovieRate(rate: Float, movieId: Long): RatingRemoteResponse? {
        return responseCall { movieApiService.postMovieRating(movieId = movieId, rate = rate) }
    }

    override suspend fun getRatedMovies(): MovieRemoteResponse {
        return responseCall { movieApiService.getRatedMovies() }
    }

    override suspend fun deleteMovieRate(movieId: Long) {
        responseCall { movieApiService.deleteMovieRate(movieId = movieId) }
    }
}