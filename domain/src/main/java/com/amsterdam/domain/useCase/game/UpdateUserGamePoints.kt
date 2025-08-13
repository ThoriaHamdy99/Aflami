package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.repository.GameRepository

class UpdateUserGamePointsUseCase (private val gamePointsRepository : GameRepository) {
   suspend operator fun invoke(points : Int)  {
       gamePointsRepository.updatePoints(points)
    }
}