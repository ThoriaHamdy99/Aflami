package com.amsterdam.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCategoryResponse(
    @SerialName("genres") val genres: List<RemoteCategoryDto>
)