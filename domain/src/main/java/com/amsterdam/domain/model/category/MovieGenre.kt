package com.amsterdam.domain.model.category

enum class MovieGenre {
    ALL,
    SCIENCE_FICTION,
    FAMILY,
    MYSTERY,
    HISTORY,
    WAR,
    ACTION,
    CRIME,
    COMEDY,
    HORROR,
    WESTERN,
    ROMANCE,
    ADVENTURE,
    TV_MOVIE,
    FANTASY,
    THRILLER,
    DRAMA,
    ANIMATION,
    MUSIC,
    DOCUMENTARY
}

fun String.toMovieGenre() = MovieGenre.valueOf(this)

fun List<String>.toMovieGenres() = this.map { it.toMovieGenre() }