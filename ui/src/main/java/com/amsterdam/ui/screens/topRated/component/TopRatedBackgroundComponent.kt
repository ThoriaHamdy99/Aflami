package com.amsterdam.ui.screens.topRated.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R
import io.sifr.shaded.modifiers.blur

@Composable
fun TopRatedBackgroundComponent() {
    AppTheme.color.topRatedGradientBackground
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.593f)
            .background(
                brush = Brush.verticalGradient(
                    colors = AppTheme.color.topRatedGradientBackground
                )
            )
    ) {
        FireImage(
            size = 64.dp,
            alignment = Alignment.TopEnd,
            offsetX = 8.dp,
            offsetY = 59.dp
        )
        FireImage(
            size = 32.dp,
            alignment = Alignment.TopEnd,
            offsetX = (-34).dp,
            offsetY = 39.dp
        )
        FireImage(
            size = 24.dp,
            alignment = Alignment.TopEnd,
            offsetX = (-60).dp,
            offsetY = 20.dp
        )
        FireImage(
            size = 200.dp,
            alignment = Alignment.BottomCenter,
            offsetX = 0.dp,
            offsetY = (-76).dp
        )
    }
}

@Composable
private fun BoxScope.FireImage(
    size: Dp,
    alignment: Alignment,
    offsetX: Dp,
    offsetY: Dp
) {
    Image(
        painter = painterResource(id = R.drawable.fire),
        modifier = Modifier
            .size(size)
            .align(alignment)
            .offset(x = offsetX, y = offsetY)
            .blur(3f),
        contentDescription = null
    )
}


@ThemeAndLocalePreviews
@Composable
private fun TopRatedBackgroundComponentPreview() {
    AflamiTheme {
        TopRatedBackgroundComponent()
    }
}