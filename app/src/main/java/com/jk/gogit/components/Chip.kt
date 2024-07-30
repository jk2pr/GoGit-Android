package com.jk.gogit.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hoppers.type.SearchType

@Composable
fun Chip(
    modifier: Modifier = Modifier,
    types: List<SearchType>,
    onTypeSelected: (SearchType) -> Unit = {},
) {
    val selectedType = remember { mutableStateOf(types.first()) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        types.forEach { type ->
            val isSelected = selectedType.value == type
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                ),
                modifier = Modifier
                    .clickable {
                        selectedType.value = type
                        onTypeSelected(type)
                    }
            ) {
                Text(
                    text = type.name,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}