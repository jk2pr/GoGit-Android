package com.jk.gogit.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun DropdownFilter(
    options: List<String>,
    onFilterSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var filterLabel by remember { mutableStateOf("Language") }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Card(
            //border = BorderStroke(2.dp,MaterialTheme.colorScheme.primary),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))

        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = filterLabel,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .wrapContentWidth()
                        .clickable(onClick = { expanded = true }),
                )
                Icon(
                    imageVector = Icons.Outlined.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                )
            }
        }
        DropdownMenu(
            modifier = Modifier.wrapContentWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = option.takeIf { it != selectedOption }
                        filterLabel =
                            if (selectedOption == null) "Language" else selectedOption.orEmpty()
                        onFilterSelected(selectedOption)
                        expanded = false
                    },
                    text = {
                        Row {
                            Text(option)
                            if (option == selectedOption) {
                                Icon(
                                    imageVector = Icons.Outlined.Done,
                                    contentDescription = "Done Icon",
                                    modifier = Modifier.sizeIn(48.dp)
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}
