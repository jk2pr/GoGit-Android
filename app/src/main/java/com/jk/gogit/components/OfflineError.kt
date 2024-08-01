package com.jk.gogit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jk.gogit.R

@Composable
@Preview
fun OfflineError(
    modifier: Modifier = Modifier,
    retryHandler: () -> Unit = {},
    title: String = "Oops!",
    message: String = "Seems you are offline. Please check your internet connection."
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painterResource(id = R.drawable.wifi_off_48dp), contentDescription = "Error")
        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        Text(
            text = message, style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(horizontal = 8.dp),
        )

        OutlinedButton(onClick = retryHandler) {
            Text(text = "Try again")
        }
    }

}