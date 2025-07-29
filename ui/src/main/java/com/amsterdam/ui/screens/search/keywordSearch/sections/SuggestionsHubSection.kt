package com.amsterdam.ui.screens.search.keywordSearch.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.components.globalSearchHub.GlobalSearchHub
import com.amsterdam.ui.components.globalSearchHub.GlobalSearchHubUI

internal fun LazyGridScope.suggestionsHubSection(
    keyword: String,
    onWorldSearchCardClicked: () -> Unit,
    onActorSearchCardClicked: () -> Unit,
) {

    item(span = { GridItemSpan(maxLineSpan) }) {
        AnimatedVisibility(keyword.isBlank()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                text = stringResource(R.string.serach_suggestions_hub),
                style = AppTheme.textStyle.title.medium,
                color = AppTheme.color.title,
                textAlign = TextAlign.Start,
            )
        }
    }

    item(span = { GridItemSpan(maxLineSpan) }) {
        AnimatedVisibility(
            modifier = Modifier.padding(bottom = 12.dp).background(Color.Blue),
            visible = keyword.isBlank()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp)
                    .height(intrinsicSize = IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GlobalSearchHub(
                    modifier = Modifier.weight(1f),
                    globalSearchHubUI = GlobalSearchHubUI.WORLD,
                    onItemClick = onWorldSearchCardClicked,
                )

                GlobalSearchHub(
                    modifier = Modifier.weight(1f),
                    globalSearchHubUI = GlobalSearchHubUI.ACTOR,
                    onItemClick = onActorSearchCardClicked,
                )
            }
//            GlobalSearchHub(
//                modifier = Modifier.fillMaxWidth().padding(start = if (it == 0) 16.dp else 0.dp, end = if (it == 1) 16.dp else 0.dp),
//                globalSearchHubUI = if(it == 0) GlobalSearchHubUI.WORLD else GlobalSearchHubUI.ACTOR,
//                onItemClick = if(it == 0) onWorldSearchCardClicked else onActorSearchCardClicked,
//            )
        }
    }
}

//@Composable
//internal fun SuggestionsHubSection(
//    keyword: String,
//    onWorldSearchCardClicked: () -> Unit,
//    onActorSearchCardClicked: () -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    AnimatedVisibility(keyword.isBlank()) {
//        Column(
//            modifier =
//                modifier.padding(
//                    bottom = 12.dp,
//                    top = 8.dp,
//                    start = 16.dp,
//                    end = 16.dp,
//                ),
//        ) {
//            Text(
//                modifier =
//                    Modifier
//                        .fillMaxWidth(),
//                text = stringResource(R.string.serach_suggestions_hub),
//                style = AppTheme.textStyle.title.medium,
//                color = AppTheme.color.title,
//                textAlign = TextAlign.Start,
//            )
//
//            Row(
//                modifier =
//                    Modifier
//                        .fillMaxWidth()
//                        .padding(top = 12.dp)
//                        .height(intrinsicSize = IntrinsicSize.Max),
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//            ) {
//                GlobalSearchHub(
//                    modifier =
//                        Modifier
//                            .weight(1f),
//                    globalSearchHubUI = GlobalSearchHubUI.WORLD,
//                    onItemClick = onWorldSearchCardClicked,
//                )
//
//                GlobalSearchHub(
//                    modifier =
//                        Modifier
//                            .weight(1f),
//                    globalSearchHubUI = GlobalSearchHubUI.ACTOR,
//                    onItemClick = onActorSearchCardClicked,
//                )
//            }
//        }
//    }
//}
