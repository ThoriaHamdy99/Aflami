package com.amsterdam.ui.screens.movieDetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.ExpandableText
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme

@Composable
fun DescriptionSection(modifier: Modifier = Modifier, description: String) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.description),
            style = AppTheme.textStyle.headline.small,
            color = AppTheme.color.title,
        )
        ExpandableText(text = description)
    }
}
