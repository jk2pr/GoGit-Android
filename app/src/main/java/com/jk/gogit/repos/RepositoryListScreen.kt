package com.jk.gogit.repos


import com.hoppers.networkmodule.network.AuthManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hoppers.fragment.Repos
import com.jk.gogit.UiState
import com.jk.gogit.components.DropdownFilter
import com.jk.gogit.components.Page
import com.jk.gogit.components.RepositoryItem
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.AppScreens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun RepositoryListScreen() {

    val localNavyController = LocalNavController.current
    val login =
        (localNavyController.previousBackStackEntry?.savedStateHandle?.get<String>(AppScreens.REPOLIST.route)
            ?: com.hoppers.networkmodule.network.AuthManager.getLogin())!!

    val viewModel = koinViewModel<RepoListViewModel>(parameters = { parametersOf(login) })
    Page(title = { Text(text = "Repositories") }) {
        when (val result = viewModel.repoStateFlow.collectAsState().value) {
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

            is UiState.Error -> {}
            is UiState.Empty -> {}
            is UiState.Content -> {

                val nodes = result.data as List<*>
                if (nodes.isEmpty())
                    Text(
                        text = "No repositories found",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center
                    )
                else
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        val languageMap = viewModel.languageMapStateFlow.collectAsState().value
                        DropdownFilter(options = languageMap.keys.toList()) { language ->
                            viewModel.setState(RepoListViewModel.MainState.FilterEvent(language))
                        }
                        LazyColumn {
                            items(nodes.size) {
                                if (it > 0) HorizontalDivider()
                                nodes[it]?.let { nodes ->
                                    RepositoryItem(nodes as Repos)
                                }
                            }
                        }
                    }
            }
        }
    }
}
