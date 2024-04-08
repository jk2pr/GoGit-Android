package com.jk.gogit.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun ColoredBullet(color: String?) {
    val col: Color = color?.let {
        Color(android.graphics.Color.parseColor(it))
    } ?: MaterialTheme.colorScheme.surface
    Canvas(
        modifier = Modifier
            .size(8.dp)
    ) {
        drawCircle(
            color = col,
            radius = 4f,
            style = Stroke(width = 4.dp.toPx())
        )
    }
}
