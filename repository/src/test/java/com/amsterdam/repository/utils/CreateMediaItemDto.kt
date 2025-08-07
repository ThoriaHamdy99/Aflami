package com.amsterdam.repository.utils

import com.amsterdam.repository.dto.remote.EpisodeDto
import com.amsterdam.repository.dto.remote.EpisodeResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteCastDto
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.dto.remote.RemoteTvShowItemDto
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.SeasonDto
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.VideoDto
import com.amsterdam.repository.dto.remote.VideoResponse
import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse

val remoteMovieItemDto = RemoteMovieItemDto(
    id = 1,
    title = "Breaking Bad",
    overview = "A high school chemistry teacher diagnosed with cancer turns to making meth.",
    posterPath = "/somePath.jpg",
    voteAverage = 9.5,
    voteCount = 10000,
    genreIds = listOf(1, 2, 3),
    releaseDate = "2008-01-20",
    originalLanguage = "English",
    popularity = 100.0,
    backdropPath = "/someBackdropPath.jpg",
    adult = false,
    originalTitle = "Breaking Bad",
    video = false
)

val remoteTvShowItemDto = RemoteTvShowItemDto(
    id = 1,
    title = "Breaking Bad",
    overview = "A high school chemistry teacher diagnosed with cancer turns to making meth.",
    posterPath = "/somePath.jpg",
    voteAverage = 9.5,
    voteCount = 10000,
    genreIds = listOf(1, 2, 3),
    releaseDate = "2008-01-20",
    originalLanguage = "English",
    popularity = 100.0,
    backdropPath = "/someBackdropPath.jpg",
    originCountry = listOf("US"),
    adult = false,
    originalTitle = "Breaking Bad",
    seasons = emptyList(),
    seasonCount = 2,
    productionCompanies = emptyList(),
)
val videoDto = VideoDto(
    id = "1",
    countryCode = "US",
    languageCode = "en",
    key = "someKey",
    name = "",
    official = true,
    publishedAt = "",
    site = "",
    size = 1080,
    type = "",
)
val remoteCastDto = RemoteCastDto(
    id = 1,
    name = "Actor One",
    character = "Char One",
    profilePath = "/img1.jpg",
    order = 1,
    adult = false,
    gender = 1,
    knownForDepartment = "Acting",
    popularity = 100.0,
    castId = 1,
    originalName = "Actor One",
    creditId = "creditId",
)
val tvShowDetailsRemoteResponse =
    TvShowDetailsRemoteResponse(
        id = 1,
        title = "Breaking Bad",
        overview = "A high school chemistry teacher diagnosed with cancer turns to making meth.",
        posterPath = "/somePath.jpg",
        voteAverage = 9.5,
        releaseDate = "2008-01-20",
        originalLanguage = "English",
        popularity = 100.0,
        backdropPath = "/someBackdropPath.jpg",
        adult = false,
        originalTitle = "Breaking Bad",
        seasons =listOf(
            SeasonDto  (
                id = 1,
                title = "Season 1",
                episodeCount = 10,
                seasonNumber = 1,
                airDate = null
            ),
           SeasonDto(
                id = 2,
                title = "Season 2",
                episodeCount = 12,
                seasonNumber = 2,
                airDate = null
            )
        ),
        seasonCount = 2,
        productionCompanies = emptyList(),
        genres = emptyList(),
        originCountry = listOf("US"),
        reviews = ReviewsResponse(
            id = 1,
            page = 1,
            results = emptyList(),
            totalPages = 1,
            totalResults = 1,
        ),
        credits = RemoteCastAndCrewResponse(
            id = 1,
            cast = listOf(remoteCastDto),
            crew = emptyList(),
        ),
        similar = RemoteTvShowResponse(
            page = 1,
            results = listOf(remoteTvShowItemDto),
            totalPages = 1,
            totalResults = 1,
        ),
        images = RemoteGalleryResponse(
            id = 1,
            backdrops = emptyList(),
            posters = emptyList(),
            logos = emptyList()
        ),
        videos = VideoResponse(
            results = listOf(videoDto),
        ),
        accountStates = null,
    )
val remoteEpisodeDto= EpisodeDto(
    id = 1,
    title = "Breaking Bad",
    overview = "A high school chemistry teacher diagnosed with cancer turns to making meth.",
    voteAverage = 9.5,
    seasonNumber = 1,
    episodeNumber = 1,
    stillPath = "/somePath.jpg",
    runtime = "50",
    airDate = "2008-01-20",
)
val episodeResponse= EpisodeResponse(
    id = 1,
    overview = "A high school chemistry teacher diagnosed with cancer turns to making meth.",
    voteAverage = 9.5,
    seasonNumber = 1,
    airDate = "2008-01-20",
    episodes = listOf(remoteEpisodeDto),
    name = "Breaking Bad",
    posterPath = "/somePath.jpg",
)