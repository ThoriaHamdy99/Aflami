package com.amsterdam.designsystem.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.theme.AppTheme

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    minimizedMaxLines: Int = 5,
    style: TextStyle = AppTheme.textStyle.body.small,
    textColor: Color = AppTheme.color.hint,
    showMoreText: String = stringResource(R.string.read_more),
    showLessText: String = stringResource(R.string.read_less),
    showMoreStyle: TextStyle = AppTheme.textStyle.label.medium,
    showLessStyle: TextStyle = AppTheme.textStyle.label.medium,
    showMoreColor: Color = AppTheme.color.primary,
    showLessColor: Color = AppTheme.color.primary
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isClickable by remember { mutableStateOf(false) }
    var lastCharacterIndex by remember { mutableIntStateOf(0) }

    val textSpanStyle = SpanStyle(
        color = textColor,
        fontSize = style.fontSize,
        fontWeight = style.fontWeight,
        fontStyle = style.fontStyle,
        letterSpacing = style.letterSpacing
    )
    val showMoreSpanStyle = SpanStyle(
        color = showMoreColor,
        fontSize = showMoreStyle.fontSize,
        fontWeight = showMoreStyle.fontWeight,
        fontStyle = showMoreStyle.fontStyle,
        letterSpacing = showMoreStyle.letterSpacing
    )

    val showLessSpanStyle = SpanStyle(
        color = showLessColor,
        fontSize = showLessStyle.fontSize,
        fontWeight = showLessStyle.fontWeight,
        fontStyle = showLessStyle.fontStyle,
        letterSpacing = showLessStyle.letterSpacing
    )

    val annotatedText = buildAnnotatedString {
        if (isClickable && !isExpanded) {
            val adjustedText = text.substring(0, lastCharacterIndex - 4)
                .dropLast(showMoreText.length)
                .dropLastWhile { it.isWhitespace() || it == '.' }
            withStyle(textSpanStyle) {
                append(adjustedText)
                append(" ")
            }

            withLink(
                link = LinkAnnotation.Clickable(
                    tag = showMoreText,
                    linkInteractionListener = { isExpanded = true }
                )
            ) {
                withStyle(showMoreSpanStyle) {
                    append(showMoreText)
                }
            }
        } else if (isExpanded) {
            withStyle(textSpanStyle) {
                append(text)
                append(" ")
            }

            withLink(
                link = LinkAnnotation.Clickable(
                    tag = showLessText,
                    linkInteractionListener = { isExpanded = false }
                )
            ) {
                withStyle(showLessSpanStyle) {
                    append(showLessText)
                }
            }
        } else {
            withStyle(textSpanStyle) {
                append(text)
            }
        }
    }

    Text(
        text = annotatedText,
        style = style,
        modifier =
            modifier
                .fillMaxWidth()
                .animateContentSize(),
        maxLines = if (isExpanded) Int.MAX_VALUE else minimizedMaxLines,
        onTextLayout = { textLayoutResult ->
            if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                isClickable = true
                lastCharacterIndex = textLayoutResult.getLineEnd(minimizedMaxLines - 1)
            }
        }
    )
}