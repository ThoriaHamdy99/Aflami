package com.amsterdam.repository.dto.remote

import androidx.room.Embedded
import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteMovieDetailsResponse(
    @Embedded val movie: RemoteMovieItemDto,
    @SerialName("reviews") val reviews: ReviewsResponse,
    @SerialName("credits") val credits: RemoteCastAndCrewResponse,
    @SerialName("similar") val similar: RemoteMovieResponse,
    @SerialName("images") val images: RemoteGalleryResponse,
    @SerialName("videos") val videos: RemoteGalleryResponse,
)