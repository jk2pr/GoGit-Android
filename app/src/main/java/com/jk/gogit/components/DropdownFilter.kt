package com.jk.gogit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun DropdownFilter(
    options: List<String>,
    onFilterSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var filterLabel by remember { mutableStateOf("Language") }

    Column{
        SuggestionChip(onClick = { expanded = true }, label = {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = filterLabel,
                    style = MaterialTheme.typography.labelMedium,
                )
                Icon(
                    imageVector = Icons.Outlined.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                )
            }
        })
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
                    trailingIcon = {
                        if (option == selectedOption)
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Done Icon",
                            )
                    },
                    text = {
                        Text(option)

                    }
                )
            }
        }
    }
}
