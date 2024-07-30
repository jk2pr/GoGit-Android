package com.jk.gogit.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun SearchComponent(
    isSearchActivated: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    onSearchTextChanged: (String) -> Unit
) {
    // Implement your search component here
    val searchText = remember { mutableStateOf("") }
    remember { MutableInteractionSource() }

    TextField(
        modifier = modifier
            .fillMaxWidth(0.9f),
        shape = MaterialTheme.shapes.small,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        maxLines = 1,
        value = searchText.value,
        onValueChange = {
            isSearchActivated.value = true
            searchText.value = it
            if (it.isEmpty()) isSearchActivated.value = false
            onSearchTextChanged(it)

        },

        placeholder = { Text("GitHub Search") },
        trailingIcon = {
            IconButton(
                onClick = {
                    isSearchActivated.value = !isSearchActivated.value
                    searchText.value = ""
                    onSearchTextChanged("")
                }
            ) {
                Icon(
                    imageVector = if (isSearchActivated.value) Icons.Outlined.Close else Icons.Outlined.Search,
                    contentDescription = "Search"
                )
            }
        },
    )
}