package com.amsterdam.repository.mapper.remote

import com.amsterdam.domain.models.MovieDetails
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.mapper.shared.EntityMapper

class MovieDetailRemoteMapper(
    private val movieRemoteMapper: MovieRemoteMapper,
    private val reviewRemoteMapper: ReviewRemoteMapper,
    private val castRemoteMapper: CastRemoteMapper,
    private val galleryRemoteMapper: GalleryRemoteMapper,
    private val posterRemoteMapper: PostersRemoteMapper,
): EntityMapper<RemoteMovieDetailsResponse, MovieDetails> {
    override fun toEntity(response: RemoteMovieDetailsResponse): MovieDetails {
        return MovieDetails(
            movie = movieRemoteMapper.toEntity(response.movie),
            reviews = reviewRemoteMapper.toEntityList(response.reviews.results),
            actors = castRemoteMapper.toEntityList(response.credits.cast),
            similarMovies = movieRemoteMapper.toEntityList(response.similar.results),
            movieGallery = galleryRemoteMapper.toEntity(response.images),
            moviePosters = posterRemoteMapper.toEntity(response.images),
        )
    }

}