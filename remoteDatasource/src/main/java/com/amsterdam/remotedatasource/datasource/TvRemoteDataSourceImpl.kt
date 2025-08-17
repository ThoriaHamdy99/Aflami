package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.logger.Loggable
import com.amsterdam.remotedatasource.api.TvShowsApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.TvShowsRemoteDataSource
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.EpisodeRemoteResponse
import com.amsterdam.repository.dto.remote.RatingRemoteResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.TvShowRemoteResponse
import com.amsterdam.repository.dto.remote.VideoRemoteResponse
import javax.inject.Inject

class TvRemoteDataSourceImpl @Inject constructor(
    private val tvShowsApiService: TvShowsApiService
) : TvShowsRemoteDataSource, Loggable {
    override suspend fun getPopularTvShows(): TvShowRemoteResponse {
        return responseCall(logger = logger, execute = { tvShowsApiService.getPopularTvShows() })
    }

    override suspend fun getTopRatedTvShows(
        page: Int
    ): TvShowRemoteResponse {
        return responseCall(
            logger = logger,
            execute = { tvShowsApiService.getTopRatedTvShows(page) })
    }

    override suspend fun getTvShowsByKeyword(keyword: String, page: Int): TvShowRemoteResponse {
        return responseCall(
            logger = logger,
            execute = { tvShowsApiService.getTvShowsByKeyword(keyword, page) })
    }

    override suspend fun getTvShowDetailsById(tvShowId: Long): TvShowDetailsRemoteResponse {
        return responseCall(
            logger = logger,
            execute = { tvShowsApiService.getTvShowDetailsById(tvShowId) })
    }

    override suspend fun getTvShowCast(tvShowId: Long): CastAndCrewRemoteResponse {
        return responseCall(
            logger = logger,
            execute = { tvShowsApiService.getTvShowCast(tvShowId) })
    }

    override suspend fun getEpisodesBySeasonNumber(
        tvShowId: Long,
        seasonNumber: Int
    ): EpisodeRemoteResponse {
        return responseCall(logger = logger, execute = {
            tvShowsApiService.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        })
    }

    override suspend fun getRatedTvShows(): TvShowRemoteResponse {
        return responseCall(logger = logger, execute = { tvShowsApiService.getRatedTvShows() })
    }

    override suspend fun setTvShowRate(
        rate: Int,
        tvShowId: Long,
    ): RatingRemoteResponse {
        return responseCall(logger = logger, execute = {
            tvShowsApiService.postTvRating(
                tvId = tvShowId,
                rate = rate.toFloat(),
            )
        })
    }

    override suspend fun deleteTvShowRate(tvShowId: Long) {
        responseCall(
            logger = logger,
            execute = { tvShowsApiService.deleteTvRating(tvId = tvShowId) })
    }

    override suspend fun getEpisodeVideos(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): VideoRemoteResponse {
        val actualSeasonNumber = if (seasonNumber <= 0) 1 else seasonNumber
        return responseCall(logger = logger, execute = {
            tvShowsApiService.getEpisodeVideos(
                tvShowId,
                actualSeasonNumber,
                episodeNumber
            )
        }
        )
    }

    override suspend fun getTvShowsByGenreId(
        genreId: Long,
        page: Int
    ): TvShowRemoteResponse {
        return responseCall(logger = logger, execute = {
            tvShowsApiService.getTvShowsByGenreIds(
                listOf(genreId),
                page
            )
        })
    }
}