package com.amsterdam.domain.repository

import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase.TvShowDetails
import com.amsterdam.entity.Episode
import com.amsterdam.entity.Season
import com.amsterdam.entity.TvShow

interface TvShowRepository {
    suspend fun getTvShowByKeyword(keyword: String, page: Int, tvShowsPerPage: Int): List<TvShow>
    suspend fun getTvShowDetails(tvShowId: Long): TvShowDetails
    suspend fun getTvShowSeasons(tvShowId: Long): List<Season>
    suspend fun getEpisodesBySeasonNumber(tvShowId: Long, seasonNumber: Int): List<Episode>
}