package com.amsterdam.imageviewer.classification

import android.graphics.Bitmap
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

internal class NsfwDetectorClassifier(
    private val imageClassifier: ImageClassifier
) : CustomImageClassifier {

    private val detectionRule = NsfwDetectorConfig.NSFW_DETECTION_RULE

    override fun isImageSafe(bitmap: Bitmap): Boolean? {
        return runInference(bitmap)?.let { scores ->
            isInappropriate(scores).not()
        }
    }

    private fun runInference(bitmap: Bitmap): FloatArray? {
        return synchronized(this) {
            runCatching {
                val tensorImage = TensorImage.fromBitmap(bitmap)

                val results = imageClassifier.classify(tensorImage)

                val scores = FloatArray(2)
                results.firstOrNull()?.categories?.forEach { category ->
                    when (category.label) {
                        "0" -> scores[detectionRule.nonNudeIndex] = category.score
                        "1" -> scores[detectionRule.nudeIndex] = category.score
                    }
                }
                scores
            }.getOrNull()
        }
    }

    private fun isInappropriate(scores: FloatArray): Boolean {
        val nonNudeScore = scores.getOrElse(detectionRule.nonNudeIndex) { 0f }
        val nudeScore = scores.getOrElse(detectionRule.nudeIndex) { 0f }
        return nonNudeScore < detectionRule.sfwThreshold || nudeScore >= detectionRule.nsfwThreshold
    }
}