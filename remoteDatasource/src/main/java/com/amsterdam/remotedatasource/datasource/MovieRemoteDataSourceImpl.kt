package com.amsterdam.remotedatasource.datasource

import android.util.Log
import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.dto.remote.RatingResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val movieApiService: MovieApiService,
) : MovieRemoteSource {

    override suspend fun getMoviesByKeyword(keyword: String, page: Int): RemoteMovieResponse {
        return responseCall { movieApiService.getMoviesByKeyword(keyword, page) }
    }

    override suspend fun getMoviesByActorIds(actorIds: List<Int>, page: Int): RemoteMovieResponse {
        val actorIdsAsString = actorIds.joinToString(separator = "|")
        return responseCall { movieApiService.getMoviesByActorId(actorIdsAsString) }
    }

    override suspend fun getActorIdsByName(name: String, page: Int): List<Int> {
        return responseCall { movieApiService.getActorIdByName(name, page) }.actors
            .map{ it.id }
    }

    override suspend fun getMoviesByCountryIsoCode(
        countryIsoCode: String,
        page: Int
    ): RemoteMovieResponse {
        return responseCall { movieApiService.getMoviesByCountryIsoCode(countryIsoCode, page) }
    }

    override suspend fun getCastByMovieId(movieId: Long): RemoteCastAndCrewResponse {
        return responseCall { movieApiService.getCastByMovieId(movieId) }
    }

    override suspend fun getMovieDetailsById(movieId: Long, sessionId: String): RemoteMovieDetailsResponse {
        return responseCall { movieApiService.getMovieDetailsById(movieId, sessionId) }
    }

    override suspend fun getPopularMovies(page: Int): RemoteMovieResponse {
        return responseCall { movieApiService.getPopularMovies(page) }
    }

    override suspend fun getUpcomingMovies(): RemoteMovieResponse {
        return responseCall { movieApiService.getUpcomingMovies() }
    }
    override suspend fun getTopRatedMovies(
        page: Int
    ): RemoteMovieResponse {
        return responseCall { movieApiService.getTopRatedMovies(page) }
    }

    override suspend fun getMoviesByGenreIds(genresIds: List<Long>, page: Int): RemoteMovieResponse {
        return responseCall { movieApiService.getMoviesByGenreIds(genresIds, page) }
    }

    override suspend fun setMovieRate(rate: Float, movieId: Long, sessionId: String): RatingResponse? {
        return responseCall { movieApiService.postMovieRating(
            movieId = movieId,
            rate = rate,
            sessionId = sessionId
        ) }
    }

    override suspend fun getRatedMovies(sessionId: String): RemoteMovieResponse {
        return responseCall { movieApiService.getRatedMovies(sessionId = sessionId) }
    }

    override suspend fun deleteMovieRate(movieId: Long, sessionId: String) {
        responseCall { movieApiService.deleteMovieRate(movieId = movieId, sessionId = sessionId) }
    }

    override suspend fun getRandomMoviesWithNotNullDate(requiredMoviesNumber: Int): List<RemoteMovieItemDto> {
        val totalPages = 500
        val collectedMovies = mutableListOf<RemoteMovieItemDto>()
        val usedPages = mutableSetOf<Int>()

        while (collectedMovies.size < requiredMoviesNumber && usedPages.size < totalPages) {
            val randomPage = (1..totalPages).random().also { usedPages.add(it) }
            val pageMovies = getPopularMoviesByPage(randomPage)
                .filter { it.releaseDate != null }

            Log.e("getRandomMoviesWithNotNullDate", pageMovies.toString())
            Log.e("getRandomMoviesWithNotNullDate", pageMovies.toString())
            for (movie in pageMovies) {
                if (!collectedMovies.contains(movie)) {
                    collectedMovies.add(movie)
                    if (collectedMovies.size == requiredMoviesNumber) break
                }
            }
        }

        return collectedMovies
    }


    private suspend fun getPopularMoviesByPage(page: Int): List<RemoteMovieItemDto> {
        return getPopularMovies(page = page).results
    }

}