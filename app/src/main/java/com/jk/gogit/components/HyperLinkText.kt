package com.jk.gogit.components

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text // Changed from ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener // Ensure this is imported
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun HyperLinkText(
    hyperLink: String,      // Data for the link action, also part of displayed text
    modifier: Modifier,
    localString: String,    // Text following the hyperLink part
    startIndex: Int,        // Start index of the clickable link segment
    endIndex: Int,          // End index of the clickable link segment
    hyperLinkStyle: SpanStyle = SpanStyle(), // Style for the link segment
    localStringStyle: TextStyle = LocalTextStyle.current, // Base style for the entire text
    action: (String) -> Unit // Lambda to execute on link click
) {

    val annotatedString = buildAnnotatedString {
        // 1. Append text content
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(hyperLink)
        }
        append(localString)

        // 2. Apply visual style to the link segment
        addStyle(
            style = hyperLinkStyle,
            start = startIndex,
            end = endIndex
        )

        // 3. Create and add the LinkAnnotation
        val clickableAnnotation = LinkAnnotation.Clickable(
            tag = "URL", // Semantic tag for the link
            linkInteractionListener = object : LinkInteractionListener {
                override fun onClick(link: LinkAnnotation) { // Renamed parameter here
                    // When the link is clicked, this listener is invoked.
                    // We use the 'hyperLink' value captured from the HyperLinkText function's parameters.
                    action(hyperLink)
                }
            }
        )

        // Apply the clickable annotation to the specified range.
        // Ensure addLink is called correctly (no named argument for clickableAnnotation itself)
        addLink(clickableAnnotation, start = startIndex, end = endIndex)
    }

    Text(
        modifier = modifier,
        style = localStringStyle.copy(color = LocalContentColor.current),
        text = annotatedString
        // Click handling is managed by the LinkAnnotation.Clickable's LinkInteractionListener
    )
}
