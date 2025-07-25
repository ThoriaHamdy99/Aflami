package com.example.ui.screens.home.sections.placeholder

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.zIndex
import com.example.designsystem.R
import com.example.designsystem.components.Icon
import com.example.designsystem.components.RoundedShimmerPlaceholder
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import kotlin.math.absoluteValue

@SuppressLint("RestrictedApi", "ConfigurationScreenWidthHeight", "UnusedBoxWithConstraintsScope")
fun LazyListScope.popularSectionPlaceholder(
) {
    item {
        Row(
            modifier = Modifier
                .zIndex(1f)
                .padding(bottom = 12.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RoundedShimmerPlaceholder(height = 30.dp, width = 77.dp, cornerRadius = 8.dp)
            Icon(
                painter = painterResource(R.drawable.ic_fire),
                tint = AppTheme.color.secondary,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
        }
    }

    item {
        BoxWithConstraints {
            val pagerState = rememberPagerState(
                initialPage = 1,
                pageCount = { 3 }
            )
            val screenWidth = maxWidth
            val itemWidth = 207.dp
            val horizontalPadding = (screenWidth - itemWidth) / 2
            HorizontalPager(
                state = pagerState,
                pageSpacing = 16.dp,
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                userScrollEnabled = false,
                modifier = Modifier.align(Alignment.TopCenter)
            ) { page ->
                val currentPageOffset =
                    ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

                val width by animateDpAsState(
                    targetValue = lerp(207.dp, 244.dp, 1f - currentPageOffset.coerceIn(0f, 1f)),
                    label = "width"
                )

                val height by animateDpAsState(
                    targetValue = lerp(276.dp, 300.dp, 1f - currentPageOffset.coerceIn(0f, 1f)),
                    label = "height"
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RoundedShimmerPlaceholder(height = height, width = width) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(color = AppTheme.color.onPrimary, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_play),
                                contentDescription = null
                            )
                        }
                    }

                    RoundedShimmerPlaceholder(height = 24.dp, width = 166.dp, cornerRadius = 8.dp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(4) {
                            RoundedShimmerPlaceholder(
                                modifier = Modifier.weight(1f),
                                height = 24.dp,
                                width = 57.dp,
                                cornerRadius = 8.dp
                            )
                        }
                    }
                }
            }
        }
    }
//        LazyRow(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(16.dp),
//            verticalAlignment = Alignment.Bottom
//        ) {
//            item { RoundedShimmerPlaceholder(height = 276.dp, width = 207.dp) }
//            item {
//                RoundedShimmerPlaceholder(height = 300.dp, width = 244.dp){
//                    Box(
//                        modifier = Modifier
//                            .size(64.dp)
//                            .background(color = AppTheme.color.onPrimary, shape = CircleShape),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Icon(painter = painterResource(R.drawable.ic_play), contentDescription = null)
//                    }
//                }
//            }
//            item { RoundedShimmerPlaceholder(height = 276.dp, width = 207.dp) }
//        }
//    }
}

@ThemeAndLocalePreviews
@Composable
private fun PopularSectionPlaceholderPreview() {

    AflamiTheme {
        LazyColumn {
            popularSectionPlaceholder()
        }
    }
}