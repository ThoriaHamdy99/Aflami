package com.amsterdam.domain.models.gameModels


data class Question(
    val id: String,
    val promptText: String? = null,
    val imageUrl: String? = null,
    val options: List<AnswerOption>,
    val correctOptionId: String
)