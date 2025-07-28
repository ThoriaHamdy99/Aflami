package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.entity.Actor
import com.amsterdam.entity.ProductionCompany
import com.amsterdam.entity.Review
import com.amsterdam.entity.Season
import com.amsterdam.entity.TvShow

class GetTvShowDetailsUseCase(
    private val tvShowRepository: TvShowRepository,
) {

    suspend operator fun invoke(tvShowId: Long): TvShowDetails {
        val tvShow = tvShowRepository.getTvShowDetails(tvShowId)
        val seasons = tvShowRepository.getTvShowSeasons(tvShowId)
        return with(tvShow) {
            TvShowDetails(
                tvShow = tvShow.tvShow,
                actors = actors,
                seasons = seasons,
                reviews = reviews,
                similarTvShows = similarTvShows,
                gallery = gallery,
                productionsCompanies = productionsCompanies
            )
        }
    }

    data class TvShowDetails(
        val tvShow: TvShow,
        val actors: List<Actor>,
        val seasons: List<Season>,
        val reviews: List<Review>,
        val similarTvShows: List<TvShow>,
        val gallery: List<String>,
        val productionsCompanies: List<ProductionCompany>
    )
}