package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.repository.GameRepository


class GetTotalUserPointsUseCase (private val gameRepository : GameRepository) {
     suspend operator fun invoke()  = gameRepository.getUserPoints()
}