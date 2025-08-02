package com.amsterdam.repository.dto.remote

import com.amsterdam.repository.utils.VideoBaseUrl.YOUTUBE_BASE_URL
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class VideoDto(
    @SerialName("iso_639_1")
    val iso6391: String,

    @SerialName("iso_3166_1")
    val iso31661: String,

    @SerialName("name")
    val name: String,

    @SerialName("key")
    val key: String,

    @SerialName("site")
    val site: String,

    @SerialName("size")
    val size: Int,

    @SerialName("type")
    val type: String,

    @SerialName("official")
    val official: Boolean,

    @SerialName("published_at")
    val publishedAt: String,

    @SerialName("id")
    val id: String
){
    val fullVideoUrl: String?
        get() = YOUTUBE_BASE_URL + key
}