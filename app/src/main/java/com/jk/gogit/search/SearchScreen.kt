package com.jk.gogit.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hoppers.fragment.Repos
import com.hoppers.fragment.UserFields
import com.hoppers.type.SearchType
import com.jk.gogit.UiState
import com.jk.gogit.components.Chip
import com.jk.gogit.components.DropdownMenuItemContent
import com.jk.gogit.components.Page
import com.jk.gogit.components.RepositoryItem
import com.jk.gogit.components.SearchComponent
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
            HorizontalDivider()
            AnimatedVisibility(visible = isSearchActivated.value) {

                Chip(
                    modifier = Modifier.fillMaxWidth(),
                    types = listOf(SearchType.REPOSITORY, SearchType.USER, SearchType.ISSUE)
                ) {
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
                is UiState.Empty -> {
                }


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


