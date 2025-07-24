package com.example.repository.mapper.remoteToLocal

import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.mapper.shared.RemoteToLocalMapper
import com.example.repository.mapper.shared.mapCategoryIdToMovieGenre

class MovieGenreIdsRemoteLocalMapper :
    RemoteToLocalMapper<Int, LocalMovieCategoryDto> {
    override fun toLocal(remote: Int, args: List<Any>): LocalMovieCategoryDto {
        return LocalMovieCategoryDto(
            categoryId = remote.toLong(),
            name = mapCategoryIdToMovieGenre(remote.toLong()).name,
            storedLanguage = args.first().toString(),
        )
    }
}