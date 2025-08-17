package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.utils.category.TvShowGenre
import com.amsterdam.entity.TvShow

class GetTvShowsByGenreUseCase(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(
        selectedGenre: TvShowGenre,
        page: Int
    ): List<TvShow> = tvShowRepository.getTvShowsByGenre(selectedGenre, page)
}
