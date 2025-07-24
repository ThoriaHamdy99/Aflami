package com.example.imageviewer.coil


import android.content.Context
import coil.ImageLoader
import com.example.imageviewer.classification.CustomImageClassifier
import com.example.imageviewer.classification.NsfwDetectorClassifier
import com.example.imageviewer.classification.policy.SafetyPolicy

internal object ImageLoaderFactory {

    fun build(
        context: Context,
        classifier: NsfwDetectorClassifier,
        policy: SafetyPolicy
    ): ImageLoader {
        val classifier: CustomImageClassifier = when (policy) {
            is SafetyPolicy.SFWPolicy -> classifier
        }

        return ImageLoader.Builder(context).components {
            add(SafetyInterceptor(classifier))
        }.build()
    }
}