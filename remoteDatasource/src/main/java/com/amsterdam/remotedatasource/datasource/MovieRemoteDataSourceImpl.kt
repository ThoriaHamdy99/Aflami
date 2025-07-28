package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.serviceProvider.MovieServiceProvider
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.dto.remote.ProductionCompanyResponse
import com.amsterdam.repository.dto.remote.RemoteActorSearchResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse

class MovieRemoteDataSourceImpl(
    private val movieServiceProvider: MovieServiceProvider
) : MovieRemoteSource {

    override suspend fun getMoviesByKeyword(keyword: String, page: Int): RemoteMovieResponse {
        return movieServiceProvider.getMoviesByKeyword(keyword, page)
    }

    override suspend fun getMoviesByActorName(name: String, page: Int): RemoteMovieResponse {
        val actorsByName = getActorIdByName(name, page)
            .actors
            .joinToString(separator = "|") { it.id.toString() }

        return movieServiceProvider.getMoviesByActorId(actorsByName)
    }

    private suspend fun getActorIdByName(name: String, page: Int): RemoteActorSearchResponse {
        return movieServiceProvider.getActorIdByName(name, page)
    }

    override suspend fun getMoviesByCountryIsoCode(
        countryIsoCode: String,
        page: Int
    ): RemoteMovieResponse {
        return movieServiceProvider.getMoviesByCountryIsoCode(countryIsoCode, page)
    }

    override suspend fun getCastByMovieId(movieId: Long): RemoteCastAndCrewResponse {
        return movieServiceProvider.getCastByMovieId(movieId)
    }

    override suspend fun getMovieDetailsById(movieId: Long): RemoteMovieDetailsResponse {
        return movieServiceProvider.getMovieDetailsById(movieId)
    }

    override suspend fun getPopularMovies(): RemoteMovieResponse {
        return movieServiceProvider.getPopularMovies()
    }

    override suspend fun getUpcomingMovies(): RemoteMovieResponse {
        return movieServiceProvider.getUpcomingMovies()
    }
  override suspend fun getTopRatedMovies(): RemoteMovieResponse {
     return movieServiceProvider.getTopRatedMovies()
  }
    override suspend fun getMoviesByGenreIds(genresIds: List<Long>): RemoteMovieResponse {
        return movieServiceProvider.getMoviesByGenreIds(genresIds)
    }
}