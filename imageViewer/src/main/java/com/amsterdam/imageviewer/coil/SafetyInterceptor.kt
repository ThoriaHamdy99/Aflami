package com.amsterdam.imageviewer.coil


import coil.intercept.Interceptor
import coil.request.ImageResult
import com.amsterdam.imageviewer.classification.CustomImageClassifier

internal class SafetyInterceptor(
    private val classifier: CustomImageClassifier
) : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val safetyTransformation = SafetyBlurTransformation(classifier)

        val newRequest = chain.request.newBuilder()
            .transformations(listOf(safetyTransformation) + chain.request.transformations)
            .build()

        return chain.proceed(newRequest)
    }
}