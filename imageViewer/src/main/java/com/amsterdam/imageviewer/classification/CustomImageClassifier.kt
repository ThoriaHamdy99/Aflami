package com.amsterdam.imageviewer.classification

import android.graphics.Bitmap

internal interface CustomImageClassifier {

    fun isImageSafe(bitmap: Bitmap): Boolean?
}