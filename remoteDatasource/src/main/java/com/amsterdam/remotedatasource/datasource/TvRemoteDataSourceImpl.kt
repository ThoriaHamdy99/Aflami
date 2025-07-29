package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.serviceProvider.TvShowsServiceProvider
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.TvShowsRemoteSource
import com.amsterdam.repository.dto.remote.EpisodeResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse
import javax.inject.Inject

class TvRemoteDataSourceImpl @Inject constructor(
    private val tvShowsServiceProvider: TvShowsServiceProvider
) : TvShowsRemoteSource {
    override suspend fun getPopularTvShows(): RemoteTvShowResponse {
        return tvShowsServiceProvider.getPopularTvShows()
    }

    override suspend fun getTopRatedTvShows(): RemoteTvShowResponse {
        return tvShowsServiceProvider.getTopRatedTvShows()
    }

    override suspend fun getTvShowCast(tvShowId: Long): RemoteCastAndCrewResponse {
        return responseCall { tvShowsServiceProvider.getTvShowCast(tvShowId) }
    }

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
