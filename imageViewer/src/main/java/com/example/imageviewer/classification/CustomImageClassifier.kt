package com.example.imageviewer.classification

import android.graphics.Bitmap

internal interface CustomImageClassifier {

    fun isImageSafe(bitmap: Bitmap): Boolean?
}