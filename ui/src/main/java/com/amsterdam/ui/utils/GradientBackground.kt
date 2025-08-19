package com.amsterdam.ui.utils

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.topGradient(): Modifier {
    return this.background(
        Brush.verticalGradient(
            colors = listOf(
                Color(0xCC000000),
                Color.Transparent
            ),
            startY = 0f,
            endY = Float.POSITIVE_INFINITY
        )
    )
}