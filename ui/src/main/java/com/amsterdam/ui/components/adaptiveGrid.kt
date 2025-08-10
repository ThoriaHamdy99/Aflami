package com.amsterdam.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun <T> LazyListScope.adaptiveGrid(
    availableWidth: Int,
    items: List<T>,
    itemMinWidth: Int,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemsHorizontalPadding: Dp = 8.dp,
    itemsVerticalPadding: Dp = 8.dp,
    cardContent: @Composable RowScope.(T) -> Unit
) {
    val chunkSize = (availableWidth / itemMinWidth).coerceAtLeast(1)
    val chunkedItems = items.chunked(chunkSize)

    item {
        Spacer(modifier = Modifier.padding(top = contentPadding.calculateTopPadding()))
    }

    itemsIndexed(chunkedItems) { index, rowItems ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = contentPadding.calculateEndPadding(LocalLayoutDirection.current),
                    bottom = if (index != chunkedItems.size - 1) itemsVerticalPadding else 0.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(itemsHorizontalPadding)
        ) {
            rowItems.forEach { item ->
                key("$index$item") {
                    cardContent(item)
                }
            }
            repeat(chunkSize - rowItems.size) {
                Box(modifier = Modifier.weight(1f))
            }
        }
    }

    item {
        Spacer(modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding()))
    }
}