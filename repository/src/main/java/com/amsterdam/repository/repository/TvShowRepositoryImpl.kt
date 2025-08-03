package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Episode
import com.amsterdam.entity.Season
import com.amsterdam.entity.TvShow
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.TvShowLocalSource
import com.amsterdam.repository.datasource.remote.TvShowsRemoteSource
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.mapper.remote.CastRemoteMapper
import com.amsterdam.repository.mapper.remote.EpisodeRemoteMapper
import com.amsterdam.repository.mapper.remote.SeasonRemoteMapper
import com.amsterdam.repository.mapper.remote.TvShowDetailsRemoteMapper
import com.amsterdam.repository.mapper.remote.TvShowRemoteMapper
import com.amsterdam.repository.mapper.remoteToLocal.TvShowRemoteDetailsLocalMapper
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TvShowRepositoryImpl @Inject constructor(
    private val localTvDataSource: TvShowLocalSource,
    private val remoteTvDataSource: TvShowsRemoteSource,
    private val preferences: AppPreferences,
    private val tvRemoteMapper: TvShowRemoteMapper,
    private val seasonRemoteMapper: SeasonRemoteMapper,
    private val episodeRemoteMapper: EpisodeRemoteMapper,
    private val tvShowRemoteDetailsLocalMapper: TvShowRemoteDetailsLocalMapper,
    private val tvShowDetailsRemoteMapper: TvShowDetailsRemoteMapper,
    private val castRemoteMapper: CastRemoteMapper,
) : TvShowRepository {
    override suspend fun getPopularTvShows(): List<TvShow> {
        return tvRemoteMapper.toEntityList(remoteTvDataSource.getPopularTvShows().results)
    }

    override suspend fun getTvShowCast(tvShowId: Long): List<Actor> {
        return castRemoteMapper.toEntityList(remoteTvDataSource.getTvShowCast(tvShowId).cast)
    }

    override suspend fun getTvShowByKeyword(
        keyword: String,
        page: Int,
        tvShowsPerPage: Int
    ): List<TvShow> {
        return tvRemoteMapper.toEntityList(getTvShows(keyword, page).results)
    }

    override suspend fun getTvShowDetails(tvShowId: Long): GetTvShowDetailsUseCase.TvShowDetails {
        return tvShowDetailsRemoteMapper.toEntity(
            remoteTvDataSource.getTvShowDetailsById(tvShowId)
                .also {
                     incrementUserInterestByTvShow(it.genres)
                    cacheWatchedTvShow(it)
                }
        )
    }

    private suspend fun cacheWatchedTvShow(remoteTvShowItemDto: TvShowDetailsRemoteResponse) {
        localTvDataSource.insertTvShow(
            tvShowRemoteDetailsLocalMapper.toLocal(
                remote = remoteTvShowItemDto, args = listOf(preferences.getDeviceLanguage().first())
            )
        )
    }

    override suspend fun getTvShowSeasons(tvShowId: Long): List<Season> {
        return seasonRemoteMapper.toEntityList(remoteTvDataSource.getTvShowDetailsById(tvShowId).seasons)
    }

    override suspend fun getEpisodesBySeasonNumber(
        tvShowId: Long,
        seasonNumber: Int
    ): List<Episode> {
        return episodeRemoteMapper.toEntityList(
            remoteTvDataSource.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            ).episodes
        )
    }

    override suspend fun getTopRatedTvShows(page: Int): List<TvShow> {
        return tvRemoteMapper.toEntityList(remoteTvDataSource.getTopRatedTvShows(page).results)
    }

    private suspend fun getTvShows(keyword: String, page: Int): RemoteTvShowResponse {
        return remoteTvDataSource.getTvShowsByKeyword(keyword, page)
    }

    private suspend fun incrementUserInterestByTvShow(remoteCategories: List<RemoteCategoryDto>) {
        remoteCategories.map(RemoteCategoryDto::id)
            .map {
                localTvDataSource.incrementGenreInterest(it.toLong()) }
    }
}