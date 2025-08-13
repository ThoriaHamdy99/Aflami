package com.amsterdam.domain.useCase.profile

import com.amsterdam.domain.repository.GameRepository // Ensure this is the correct GameRepository

class GetUserPointsUseCase(
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(): Int {
        return gameRepository.getUserPoints()
    }
}