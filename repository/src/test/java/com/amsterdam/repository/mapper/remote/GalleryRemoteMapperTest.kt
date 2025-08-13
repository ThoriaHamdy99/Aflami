package com.amsterdam.repository.mapper.remote

import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryRemoteResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class GalleryRemoteMapperTest {


    @Test
    fun `given remote gallery response when mapped to entity list then return list of file paths`() {
        // Given
        val backdrops =
            RemoteGalleryRemoteResponse(
                id = null,
                backdrops = emptyList(),
                logos = emptyList(),
                posters = emptyList(),

                )


        // When
        val result = backdrops.toEntityList()

        // Then
        assertThat(result).isEmpty()
    }

}
