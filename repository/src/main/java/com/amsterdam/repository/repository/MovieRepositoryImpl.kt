package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Country
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.MovieLocalSource
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.dto.local.utils.SearchType
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import com.amsterdam.repository.mapper.local.MovieGenreLocalMapper
import com.amsterdam.repository.mapper.remote.CastRemoteMapper
import com.amsterdam.repository.mapper.remote.MovieDetailRemoteMapper
import com.amsterdam.repository.mapper.remote.MovieRemoteMapper
import com.amsterdam.repository.mapper.remoteToLocal.MovieGenreIdsRemoteLocalMapper
import com.amsterdam.repository.mapper.remoteToLocal.MovieRemoteLocalMapper
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieLocalSource: MovieLocalSource,
    private val movieRemoteDataSource: MovieRemoteSource,
    private val preferences: AppPreferences,
    private val movieGenreIdsRemoteLocalMapper: MovieGenreIdsRemoteLocalMapper,
    private val movieRemoteMapper: MovieRemoteMapper,
    private val castRemoteMapper: CastRemoteMapper,
    private val movieDetailRemoteMapper: MovieDetailRemoteMapper,
    private val movieRemoteLocalMapper: MovieRemoteLocalMapper,
    private val movieGenreLocalMapper: MovieGenreLocalMapper,
) : MovieRepository {
    override suspend fun getMoviesByKeyword(
        keyword: String,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        return getMoviesByKeywordFromRemote(
            keyword,
            page
        )
    }

    override suspend fun getMoviesByActor(
        actorName: String,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        return getMoviesByActorNameFromRemote(
            actorName,
            page
        )

    }

    override suspend fun getMoviesByCountry(
        country: Country,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        return getMoviesByCountryIsoCodeFromRemote(
            country.countryIsoCode,
            page
        )
    }

    override suspend fun getActorsByMovieId(movieId: Long): List<Actor> {
        return castRemoteMapper.toEntityList(movieRemoteDataSource.getCastByMovieId(movieId).cast)
    }

    override suspend fun getMovieDetailsById(movieId: Long): GetMovieDetailsUseCase.MovieDetails {
        return movieDetailRemoteMapper.toEntity(
            movieRemoteDataSource.getMovieDetailsById(movieId)
                .also {
                    incrementUserInterestByMovie(it.genres)
                    cacheWatchedMovie(movieDetailRemoteMapper.mapMovieDetailsToMovieItemDto(it))
                }
        )
    }

    private suspend fun cacheWatchedMovie(remoteMovieItemDto: RemoteMovieItemDto) {
        movieLocalSource.insertMovie(
            movieRemoteLocalMapper.toLocal(
                remote = remoteMovieItemDto, args = listOf(preferences.getDeviceLanguage().first())
            )
        )
    }

    override suspend fun getUpcomingMovies(): List<Movie> {
        return movieRemoteMapper.toEntityList(
            movieRemoteDataSource.getUpcomingMovies().results,
            isPoster = false
        )
    }

    override suspend fun getPopularMovies(): List<Movie> =
        movieRemoteMapper.toEntityList(movieRemoteDataSource.getPopularMovies().results)

    override suspend fun getTopRatedMovies(
        page: Int,
    ): List<Movie> =
        movieRemoteMapper.toEntityList(
            movieRemoteDataSource.getTopRatedMovies(
                page = page,
            ).results
        )


    private suspend fun getMoviesByKeywordFromRemote(
        keyword: String, page: Int
    ): List<Movie> {
        return onSuccessGetRemoteMovies(
            movieRemoteDataSource.getMoviesByKeyword(keyword, page)
        )
    }

    private suspend fun getMoviesByActorNameFromRemote(
        actorName: String, page: Int
    ): List<Movie> {
        return movieRemoteDataSource.getActorIdsByName(actorName, page).takeIf { actorIds ->
            actorIds.isNotEmpty()
        }?.let { actorIds ->
            onSuccessGetRemoteMovies(
                movieRemoteDataSource.getMoviesByActorIds(actorIds, page)
            )
        } ?: emptyList()
    }

    private suspend fun getMoviesByCountryIsoCodeFromRemote(
        countryIsoCode: String, page: Int
    ): List<Movie> {
        return onSuccessGetRemoteMovies(
            movieRemoteDataSource.getMoviesByCountryIsoCode(countryIsoCode, page)
        )
    }

    private fun onSuccessGetRemoteMovies(
        remoteMovies: RemoteMovieResponse
    ): List<Movie> {
        return movieRemoteMapper.toEntityList(remoteMovies.results)
    }


    private suspend fun saveMoviesWithSearch(
        remoteMovies: RemoteMovieResponse, keyword: String, searchType: SearchType
    ) {
        movieLocalSource.addMoviesBySearchData(
            movies = movieRemoteLocalMapper.toLocalList(
                remoteMovies.results,
                listOf(preferences.getDeviceLanguage().first())
            ),
            searchKeyword = keyword,
            searchType = searchType
        )
    }

    private suspend fun saveMovieWithCategories(remoteMovies: RemoteMovieResponse) {
        remoteMovies.results.forEach { onSaveMovieWithCategories(it) }
    }

    private suspend fun onSaveMovieWithCategories(remoteMovie: RemoteMovieItemDto) {
        movieLocalSource.addMovieWithCategories(
            movie = movieRemoteLocalMapper.toLocal(
                remoteMovie,
                listOf(preferences.getDeviceLanguage().first())
            ),
            categories = movieGenreIdsRemoteLocalMapper.toLocalList(
                remoteMovie.genreIds,
                listOf(preferences.getDeviceLanguage().first())
            ),
            storedLanguage = preferences.getDeviceLanguage().first()
        )
    }

    override suspend fun getMoviesByGenres(movieGenres: List<MovieGenre>): List<Movie> {
        return movieGenreLocalMapper.toDtoList(movieGenres).let { genresIds ->
            movieRemoteMapper.toEntityList(
                movieRemoteDataSource.getMoviesByGenreIds(
                    genresIds
                ).results
            )
        }
    }

    private suspend fun incrementUserInterestByMovie(remoteCategories: List<RemoteCategoryDto>) {
        remoteCategories.map(RemoteCategoryDto::id)
            .map { movieLocalSource.incrementGenreInterest(it.toLong()) }
    }


}