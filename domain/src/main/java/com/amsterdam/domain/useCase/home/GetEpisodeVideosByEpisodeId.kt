package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.repository.TvShowRepository

class GetEpisodeVideosByEpisodeId (
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Long
    ) = tvShowRepository.getEpisodeVideoUrl(tvShowId, seasonNumber, episodeNumber.toInt())

}