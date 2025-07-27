package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.serviceProvider.MovieServiceProvider
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.dto.remote.ProductionCompanyResponse
import com.amsterdam.repository.dto.remote.RemoteActorSearchResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
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

    override suspend fun getMovieReviews(movieId: Long): ReviewsResponse {
        return movieServiceProvider.getMovieReviews(movieId)
    }

    override suspend fun getSimilarMovies(movieId: Long): RemoteMovieResponse {
        return movieServiceProvider.getSimilarMovies(movieId)
    }

    override suspend fun getMovieGallery(movieId: Long): RemoteGalleryResponse {
        return movieServiceProvider.getMovieGallery(movieId)
    }

    override suspend fun getProductionCompany(movieId: Long): ProductionCompanyResponse {
        return movieServiceProvider.getProductionCompany(movieId)
    }

    override suspend fun getMovieDetailsById(movieId: Long): RemoteMovieItemDto {
        return movieServiceProvider.getMovieDetailsById(movieId)
    }

    override suspend fun getMoviePosters(movieId: Long): RemoteGalleryResponse {
        return movieServiceProvider.getMoviePosters(movieId)
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