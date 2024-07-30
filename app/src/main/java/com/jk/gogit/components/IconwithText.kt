package com.jk.gogit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun IconWithText(text: String, res: Int, tint: Color = Color.Black) {
    Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {

        Icon(
            painter = painterResource(id = res),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            //  fontFamily = FontFamily(Font(R.font.bebasneue_light)),
        )
    }
}