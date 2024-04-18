package com.jk.gogit.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
 fun TitleText(title: String, modifier: Modifier = Modifier) {
    Text(
        softWrap = true,
        maxLines = 1,
        text = title,
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold,
        )
    )
}