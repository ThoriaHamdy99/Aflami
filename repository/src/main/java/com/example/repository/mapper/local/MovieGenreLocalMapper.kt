package com.example.repository.mapper.local

import com.example.entity.category.MovieGenre
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.mapper.shared.DtoMapper
import com.example.repository.mapper.shared.EntityMapper
import com.example.repository.mapper.shared.toMovieCategory

class MovieGenreLocalMapper : EntityMapper<LocalMovieCategoryDto, MovieGenre>,
    DtoMapper<MovieGenre, Long> {
    override fun toEntity(dto: LocalMovieCategoryDto): MovieGenre {
        return dto.categoryId.toMovieCategory()
    }

    override fun toDto(entity: MovieGenre): Long {
        return when (entity) {
            MovieGenre.ACTION -> 28L
            MovieGenre.ADVENTURE -> 12L
            MovieGenre.ANIMATION -> 16L
            MovieGenre.COMEDY -> 35L
            MovieGenre.CRIME -> 80L
            MovieGenre.DOCUMENTARY -> 99L
            MovieGenre.DRAMA -> 18L
            MovieGenre.FAMILY -> 10751L
            MovieGenre.FANTASY -> 14L
            MovieGenre.HISTORY -> 36L
            MovieGenre.HORROR -> 27L
            MovieGenre.MUSIC -> 10402L
            MovieGenre.MYSTERY -> 9648L
            MovieGenre.ROMANCE -> 10749L
            MovieGenre.SCIENCE_FICTION -> 878L
            MovieGenre.TV_MOVIE -> 10770L
            MovieGenre.THRILLER -> 53L
            MovieGenre.WAR -> 10752L
            MovieGenre.WESTERN -> 37L
            else -> 35L
        }
    }
}