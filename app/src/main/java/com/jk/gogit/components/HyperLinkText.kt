package com.jk.gogit.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun HyperLinkText(
    hyperLink: String,
    modifier: Modifier,
    localString: String,
    startIndex: Int,
    endIndex: Int,
    hyperLinkStyle: SpanStyle = SpanStyle(),
    localStringStyle: TextStyle = LocalTextStyle.current,
    action: (String) -> Unit
) {

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(hyperLink)
        }
        append(localString)
        addStyle(
            end = endIndex,
            start = startIndex,
            style = hyperLinkStyle
        )
        addStringAnnotation(
            annotation = hyperLink,
            end = endIndex,
            start = startIndex,
            tag = "URL"
        )
    }

    ClickableText(
        modifier = modifier,
        style = localStringStyle.copy(color = LocalContentColor.current),
        text = annotatedString,
        onClick = {
            annotatedString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    action(stringAnnotation.item)
                }
        },

        )
}
