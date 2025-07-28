package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.serviceProvider.TvShowsServiceProvider
import com.amsterdam.repository.datasource.remote.TvShowsRemoteSource
import com.amsterdam.repository.dto.remote.EpisodeResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse

class TvRemoteDataSourceImpl(
    private val tvShowsServiceProvider: TvShowsServiceProvider
) : TvShowsRemoteSource {

    override suspend fun getTvShowsByKeyword(keyword: String, page: Int): RemoteTvShowResponse {
        return tvShowsServiceProvider.getTvShowsByKeyword(keyword, page)
    }

    override suspend fun getTvShowDetailsById(tvShowId: Long): TvShowDetailsRemoteResponse {
        return tvShowsServiceProvider.getTvShowDetailsById(tvShowId)
    }

    override suspend fun getEpisodesBySeasonNumber(
        tvShowId: Long,
        seasonNumber: Int
    ): EpisodeResponse {
        return tvShowsServiceProvider.getEpisodesBySeasonNumber(tvShowId, seasonNumber)
    }
}
