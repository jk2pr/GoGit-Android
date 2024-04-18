package com.jk.gogit.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.hoppers.fragment.Repos
import com.hoppers.fragment.UserFields
import com.hoppers.type.SearchType
import com.jk.gogit.UiState
import com.jk.gogit.components.DropdownMenuItemContent
import com.jk.gogit.components.Page
import com.jk.gogit.components.RepositoryItem
import com.jk.gogit.components.UserItem
import org.koin.androidx.compose.koinViewModel


@Composable
fun SearchScreen() {

    val viewModel = koinViewModel<SearchViewModel>()
    val isSearchActivated = remember { mutableStateOf(false) }
    Page(
        menuItems = mutableListOf(
            DropdownMenuItemContent(menu = {
                SearchComponent(isSearchActivated) { query ->
                    viewModel.performSearch(query)
                }
            })
        )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            AnimatedVisibility(visible = isSearchActivated.value) {
                Chip(modifier = Modifier.fillMaxWidth()) {
                    viewModel.updateType(it)
                }
            }
            when (val result = viewModel.searchStateFlow.collectAsState().value) {
                is UiState.Loading -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center),
                    )
                }

                is UiState.Error,
                is UiState.Empty -> {}


                is UiState.Content -> {
                    val nodes = result.data as List<*>
                    LazyColumn {
                        items(nodes.size) {
                            if (it > 0) HorizontalDivider()
                            when (val node = nodes[it]) {
                                is Repos -> RepositoryItem(node)
                                is UserFields -> UserItem(node)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Chip(modifier: Modifier = Modifier, onTypeSelected: (SearchType) -> Unit) {
    val types = listOf(SearchType.REPOSITORY, SearchType.USER, SearchType.ISSUE)
    val selectedType = remember { mutableStateOf(types.first()) }

    Row(
        modifier = modifier.padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        types.forEach { type ->
            val isSelected = selectedType.value == type
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
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


@Composable
fun SearchComponent(
    isSearchActivated: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    onSearchTextChanged: (String) -> Unit
) {
    // Implement your search component here
    val searchText = remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }

    TextField(
        modifier = modifier
            .wrapContentWidth()
            .padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.small,
        colors = OutlinedTextFieldDefaults.colors(),
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