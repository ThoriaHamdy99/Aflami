package com.amsterdam.viewmodel.seriesDetails

import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase.TvShowDetails
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Episode
import com.amsterdam.entity.ProductionCompany
import com.amsterdam.entity.Review
import com.amsterdam.entity.Season
import com.amsterdam.entity.TvShow
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState.EpisodeUiState
import com.amsterdam.viewmodel.shared.mappers.toUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.SimilarMovieUiState
import com.amsterdam.viewmodel.utils.dateToString
import com.amsterdam.viewmodel.utils.formatDuration
import com.amsterdam.viewmodel.shared.mappers.ratingToRatingString
import kotlin.collections.map


fun TvShowDetails.toUiState(): SeriesDetailsUiState {
    return SeriesDetailsUiState(
        videoUrl = tvShow.videoUrl,
        tvShowId = tvShow.id,
        rating = ratingToRatingString(tvShow.rating),
        posterUrl = tvShow.posterUrl,
        title = tvShow.name,
        airDate = dateToString(tvShow.airDate),
        categories = tvShow.categories,
        seasonCount = formatSeasonCount(seasons.size),
        originCountry = tvShow.originCountry,
        description = tvShow.description,
        cast = actors.map(Actor::toUiState),
        isRateDialogVisible = false,
        isAddToListDialogVisible = false,
        extraItem = SeriesDetailsUiState.defaultSeriesExtrasItems,
        seasons = mapToSeasonUiState(seasons),
        similarSeries = similarTvShows.map(TvShow::toSimilarTvShowUiState),
        reviews = reviews.map(Review::toUiState),
        gallery = gallery,
        postersUrls = posters,
        productionCompanies = productionsCompanies.map(ProductionCompany::toUiState),
    )
}

private fun TvShow.toSimilarTvShowUiState(): SimilarMovieUiState{
    return SimilarMovieUiState(
        movieId = id,
        rate = ratingToRatingString(rating),
        name = name,
        productionYear = airDate.year.toString(),
        posterUrl = posterUrl
    )
}
//mapToSeasonUiState(seasons)
private fun mapToSeasonUiState(
    seasons: List<Season>,
    episodesBySeason: Map<Int, List<Episode>> = emptyMap()
): List<SeasonUiState> {
    return seasons.map { season ->
        val episodes = episodesBySeason[season.seasonNumber] ?: emptyList()
        season.toUiState(episodes)
    }
}

private fun Season.toUiState(episodes: List<Episode>): SeasonUiState {
    return SeasonUiState(
        id = id,
        seasonNumber = seasonNumber,
        title = title,
        episodeCount = episodeCount,
        episodes = episodes.map(Episode::toUiState)
    )
}

private fun Episode.toUiState(): EpisodeUiState{
    return EpisodeUiState(
        id = id,
        number = episodeNumber,
        title = title,
        rating = ratingToRatingString(rating),
        imageUrl = episodeImageUrl,
        imageNumber = episodeNumber,
        description = description,
        duration = formatDuration(runTimeInMinutes),
        airDate = dateToString(airDate)
    )
}

private fun formatSeasonCount(count: Int) = "$count Season"
