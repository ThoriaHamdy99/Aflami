package com.amsterdam.repository.mapper

import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryRemoteResponse

fun RemoteGalleryRemoteResponse.toEntityList(): List<String> {
    return this.backdrops.map { it.fullFilePath.orEmpty() }
}