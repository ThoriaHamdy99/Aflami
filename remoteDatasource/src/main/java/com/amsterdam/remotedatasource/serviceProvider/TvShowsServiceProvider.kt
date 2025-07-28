package com.amsterdam.remotedatasource.serviceProvider

import com.amsterdam.repository.dto.remote.EpisodeResponse
import com.amsterdam.repository.dto.remote.ProductionCompanyResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse

interface TvShowsServiceProvider {
    suspend fun getTvShowsByKeyword(keyword: String, page: Int): RemoteTvShowResponse
    suspend fun getTvShowDetailsById(tvShowId: Long): TvShowDetailsRemoteResponse
    suspend fun getEpisodesBySeasonNumber(tvShowId: Long, seasonNumber: Int): EpisodeResponse
}