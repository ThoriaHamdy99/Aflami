package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.entity.Episode
import javax.inject.Inject

class GetEpisodesBySeasonNumberUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvShowId: Long, seasonNumber: Int): List<Episode> {
        return tvShowRepository.getEpisodesBySeasonNumber(tvShowId, seasonNumber)
    }
}