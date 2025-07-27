package com.amsterdam.domain.models

import com.amsterdam.entity.Actor
import com.amsterdam.entity.Movie
import com.amsterdam.entity.Review

data class MovieDetails(
    val movie: Movie,
    val reviews: List<Review>,
    val actors: List<Actor>,
    val similarMovies: List<Movie>,
    val movieGallery: List<String>,
    val moviePosters: List<String>,
)