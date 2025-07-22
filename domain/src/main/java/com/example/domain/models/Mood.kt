package com.example.domain.models

import com.example.entity.category.MovieGenre

enum class Mood(val movieGenres: List<MovieGenre>) {
    SAD(listOf(MovieGenre.DRAMA, MovieGenre.ROMANCE)),
    THINKING(listOf(MovieGenre.SCIENCE_FICTION, MovieGenre.MYSTERY, MovieGenre.DOCUMENTARY)),
    IN_LOVE(listOf(MovieGenre.ROMANCE, MovieGenre.COMEDY)),
    ANGRY(listOf(MovieGenre.ACTION, MovieGenre.THRILLER)),
    UN_HAPPY(listOf(MovieGenre.DRAMA, MovieGenre.WAR)),
    CONFUSED(listOf(MovieGenre.DRAMA, MovieGenre.THRILLER));
}