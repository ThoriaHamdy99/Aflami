package com.example.imageviewer.classification.model

internal data class SafetyRule(
    val labelName: String,
    val labelIndex: Int,
    val threshold: Float
)
internal data class NsfwDetectorRule(
    val nudeIndex: Int,
    val nonNudeIndex: Int,
    val nsfwThreshold: Float,
    val sfwThreshold: Float
)
