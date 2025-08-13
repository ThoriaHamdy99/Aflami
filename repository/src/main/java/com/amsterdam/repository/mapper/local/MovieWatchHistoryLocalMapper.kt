package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.repository.dto.local.MovieLocalDto
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto

fun MovieWatchHistoryDto.toWatchHistoryEntity(movieLocalDto: MovieLocalDto): MovieWatchHistory =
    MovieWatchHistory(
        movie = movieLocalDto.toEntity(),
        lastWatchedTime = watchedDate
    )
