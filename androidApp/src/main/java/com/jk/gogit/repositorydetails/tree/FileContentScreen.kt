package com.jk.gogit.repositorydetails.tree

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jk.gogit.components.Page
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.NavigationArgs
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun FileContentScreen() {

    val localNavyController = LocalNavController.current
    val savedStateHandle = localNavyController.previousBackStackEntry?.savedStateHandle
    val filePath = savedStateHandle?.get<String>(NavigationArgs.FILE_CONTENT)!!
    val fileName = savedStateHandle.get<String>(NavigationArgs.FILE_NAME)!!

    Page(title = {Text(text = fileName)},
        contentAlignment = Alignment.TopStart) {
        MarkdownText(
            markdown = filePath,
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = LocalContentColor.current
            )
        )
    }
}