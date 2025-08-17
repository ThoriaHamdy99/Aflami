package com.amsterdam.domain.utils.category

enum class TvShowGenre {
    ALL,
    SCIENCE_FICTION_FANTASY,
    CRIME,
    FAMILY,
    MYSTERY,
    WAR_POLITICS,
    ANIMATION,
    COMEDY,
    DRAMA,
    KIDS,
    NEWS,
    ACTION_ADVENTURE,
    REALITY,
    SOAP,
    TALK,
    WESTERN,
    DOCUMENTARY
}

fun String.toTvShowGenre() = TvShowGenre.valueOf(this)

fun List<String>.toTvShowGenres() = this.map { it.toTvShowGenre() }
