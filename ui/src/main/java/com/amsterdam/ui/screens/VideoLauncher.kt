package com.amsterdam.ui.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun openYouTubeVideo(context: Context, videoUrl: String) {
    val appIntent = Intent(Intent.ACTION_VIEW, videoUrl.toUri())
        .apply { setPackage("com.google.android.youtube") }

    val webIntent = Intent(
        Intent.ACTION_VIEW,
        videoUrl.toUri()
    )

    try {
        context.startActivity(appIntent)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(webIntent)
    }
}