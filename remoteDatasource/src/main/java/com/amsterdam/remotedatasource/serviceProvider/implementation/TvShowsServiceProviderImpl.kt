package com.amsterdam.remotedatasource.serviceProvider.implementation

import com.amsterdam.remotedatasource.api.TvShowsApiService
import com.amsterdam.remotedatasource.serviceProvider.TvShowsServiceProvider
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.dto.remote.EpisodeResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse

class TvShowsServiceProviderImpl(
    private val tvShowsApiService: TvShowsApiService
) : TvShowsServiceProvider {

    override suspend fun getTvShowsByKeyword(keyword: String, page: Int): RemoteTvShowResponse {
        return responseCall {
            tvShowsApiService.getTvShowsByKeyword(keyword, page)
        }
    }

    override suspend fun getTvShowDetailsById(tvShowId: Long): TvShowDetailsRemoteResponse {
        return responseCall { tvShowsApiService.getTvShowDetailsById(tvShowId) }
    }

    override suspend fun getEpisodesBySeasonNumber(
        tvShowId: Long,
        seasonNumber: Int
    ): EpisodeResponse {
        return responseCall {
            tvShowsApiService.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        }
    }
}