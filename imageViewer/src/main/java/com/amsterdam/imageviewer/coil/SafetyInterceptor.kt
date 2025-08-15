package com.amsterdam.imageviewer.coil


import coil.intercept.Interceptor
import coil.request.ImageResult
import com.amsterdam.imageviewer.classification.CustomImageClassifier
import com.amsterdam.imageviewer.classification.NsfwDetectorConfig
import com.amsterdam.imageviewer.classification.model.NsfwDetectorRule

internal class SafetyInterceptor(
    private val classifier: CustomImageClassifier,
    private val nsfwDetectorRule: NsfwDetectorRule
) : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val isAdult = chain.request.parameters.value("is_adult") as? Boolean ?: false
        val detectorRule = adjustDetectRule(isAdult)

        val safetyTransformation =
            SafetyBlurTransformation(classifier, detectorRule = detectorRule)

        val newRequest = chain.request.newBuilder()
            .transformations(listOf(safetyTransformation) + chain.request.transformations)
            .build()

        return chain.proceed(newRequest)
    }

    private fun adjustDetectRule(isAdult: Boolean): NsfwDetectorRule {
        return if (isAdult) {
            when (nsfwDetectorRule) {
                NsfwDetectorConfig.NSFW_DETECTION_RULE_STRICT -> NsfwDetectorConfig.NSFW_DETECTION_RULE_STRICT_ADULT
                NsfwDetectorConfig.NSFW_DETECTION_RULE_MODERATE -> NsfwDetectorConfig.NSFW_DETECTION_RULE_MODERATE_ADULT
                else -> nsfwDetectorRule
            }
        } else {
            nsfwDetectorRule
        }
    }
}