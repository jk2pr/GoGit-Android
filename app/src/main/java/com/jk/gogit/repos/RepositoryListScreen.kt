package com.jk.gogit.repos


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import com.hoppers.networkmodule.network.AuthManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hoppers.fragment.Repos
import com.jk.gogit.R
import com.jk.gogit.UiState
import com.jk.gogit.components.ColoredBullet
import com.jk.gogit.components.DropdownFilter
import com.jk.gogit.components.Page
import com.jk.gogit.components.RepositoryItem
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.AppScreens
import com.jk.gogit.navigation.NavigationArgs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun RepositoryListScreen() {

    val localNavyController = LocalNavController.current
    val savedStateHandle = (localNavyController.previousBackStackEntry?.savedStateHandle)
    val login =
        savedStateHandle?.get<String>(AppScreens.REPOLIST.route)
            ?: AuthManager.getLogin()!!
    val isStarred = savedStateHandle?.get<Boolean>(AppScreens.USERPROFILE.route) ?: false
    val isOrg = savedStateHandle?.get<Boolean>(AppScreens.REPODETAIL.route) ?: false
    val filter = savedStateHandle?.get<String>(NavigationArgs.FILTER) ?: ""
    val repoName = savedStateHandle?.get<String>(NavigationArgs.REPO_NAME) ?: ""

    val viewModel = koinViewModel<RepoListViewModel>(parameters = {
        parametersOf(
            login,
            isStarred,
            isOrg,
            filter,
            repoName
        )
    })
    Page(title = {
        Text(
            text = if (isStarred) "Starred Repositories"
            else if (filter.isNotEmpty()) filter
            else "Repositories"
        )
    }) {
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
                        if (filter.isEmpty()) {
                            val languageMap = viewModel.languageMapStateFlow.collectAsState().value
                            DropdownFilter(options = languageMap.keys.toList()) { language ->
                                viewModel.setState(RepoListViewModel.MainState.FilterEvent(language))
                            }
                        }
                        LazyColumn {
                            items(nodes.size) {
                                if (it > 0) HorizontalDivider()
                                nodes[it]?.let { nodes ->
                                    if (filter == "Forks")
                                        ForksItem(nodes as Repos)
                                    else
                                        RepositoryItem(nodes as Repos)
                                }
                            }
                        }
                    }
            }
        }
    }

}

@Composable
fun ForksItem(repo: Repos) {
    val localNavController = LocalNavController.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                localNavController.currentBackStackEntry
                    ?.savedStateHandle?.let {
                        it[AppScreens.USERPROFILE.route] = repo.owner.login
                        it[AppScreens.REPOLIST.route] = repo.defaultBranchRef?.name.orEmpty()
                        it[AppScreens.REPODETAIL.route] = repo.repoName
                    }
                localNavController.navigate(AppScreens.REPODETAIL.route)
            }
    ) {

        Text(
            text = "${repo.owner.login}/${repo.repoName}",
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_star_24),
                    contentDescription = null,
                    tint = Color(android.graphics.Color.parseColor("#FFA500")),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = repo.stargazerCount.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    //  fontFamily = FontFamily(Font(R.font.bebasneue_light)),
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_fork_left_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = repo.forkCount.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    //  fontFamily = FontFamily(Font(R.font.bebasneue_light)),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
        Text(
            text = "Updated on " + repo.updatedAt.toString(),
            style = MaterialTheme.typography.bodySmall,
            //   fontFamily = FontFamily(Font(R.font.bebasneue_light)),
            textAlign = TextAlign.End
        )
    }

}
