package com.jk.gogit.repositorydetails

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hoppers.GetRepoDetailsQuery
import com.jk.gogit.R
import com.jk.gogit.UiState
import com.jk.gogit.components.Page
import com.jk.gogit.components.TitleText
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.AppScreens
import com.jk.gogit.overview.InfoRow
import dev.jeziellago.compose.markdowntext.MarkdownText
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RepoDetailScreen() {

    val localNavyController = LocalNavController.current
    val login =
        localNavyController.previousBackStackEntry?.savedStateHandle?.get<String>(AppScreens.USERPROFILE.route)!!
    val repoName =
        localNavyController.previousBackStackEntry?.savedStateHandle?.get<String>(AppScreens.REPODETAIL.route)!!
    val path =
        localNavyController.previousBackStackEntry?.savedStateHandle?.get<String>(AppScreens.REPOLIST.route)

    val viewModel =
        koinViewModel<RepoDetailViewModel>(parameters = {
            parametersOf(
                login,
                repoName,
                "$path:README.md"
            )
        })
    val scrollState = rememberScrollState()
    val titleKey = remember { mutableStateOf("") }
    val titleValue = remember { mutableStateOf("") }

    Page(title = {
        Column {
            Text(text = titleKey.value)
            TitleText(title = titleValue.value)
        }
    }
    ) {

        when (val result = viewModel.userListStateFlow.collectAsState().value) {
            is UiState.Loading ->
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp)
                )

            is UiState.Content -> {
                val repo = result.data as GetRepoDetailsQuery.Repository
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Top,
                ) {
                    RepoDetailHeader(repo = repo)
                    RepoDetail(repo = repo)
                }
                LaunchedEffect(Unit) {
                    Log.d("RepoDetailScreen", "LaunchedEffect triggered")
                    scrollState.scrollTo(0)
                    snapshotFlow { scrollState.value }
                        .collect { scrollOffset ->
                            val (newTitleKey, newTitleValue) = if (scrollOffset > 100)
                                repo.owner.login to repo.name
                            else
                                "" to ""

                            titleKey.value = newTitleKey
                            titleValue.value = newTitleValue
                        }
                }
            }

            is UiState.Error -> Text(text = result.message)
            is UiState.Empty -> {}
        }

    }
}

@Composable
fun RepoDetail(repo: GetRepoDetailsQuery.Repository) {
    val localNavController = LocalNavController.current
    Card(
        border = BorderStroke(1.dp, Color.Gray),
        modifier = Modifier.padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        InfoRow(
            iconId = R.drawable.organization_65,
            label = "Issue",
            count = repo.issues.totalCount
        )

        InfoRow(
            iconId = R.drawable.git_pull_request_svgrepo_com,
            label = "Pull Requests",
            count = repo.pullRequests.nodes?.size
        ) {
            repo.pullRequests.nodes?.joinToString(separator = ",") { it.toString() } // Convert to string

            localNavController.currentBackStackEntry
                ?.savedStateHandle
                ?.set(AppScreens.PULLREQUESTS.route, repo.pullRequests.nodes)
            localNavController.navigate(AppScreens.PULLREQUESTS.route)
        }
        InfoRow(iconId = R.drawable.baseline_star_24, label = "Stars", count = repo.stargazerCount)
        InfoRow(iconId = R.drawable.baseline_fork_left_24, label = "Forks", count = repo.forkCount)
        InfoRow(
            iconId = R.drawable.organization_65,
            label = "Contributors",
            count = repo.collaborators?.totalCount ?: 0
        )
        InfoRow(
            iconId = R.drawable.baseline_remove_red_eye_24,
            label = "Watchers",
            count = repo.watchers.totalCount
        )

        HorizontalDivider()
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.branch_svgrepo_com),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = repo.defaultBranchRef?.name.orEmpty())
                }
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "View all")
                }
            }
            HorizontalDivider()
            InfoRow(iconId = R.drawable.code_2_svgrepo_com, label = "Code")
            InfoRow(iconId = R.drawable.commit_svgrepo_com, label = "Commits")


        }
    }

    val readmeText = repo.readme?.onBlob?.text
    //  if (readmeText.isNullOrEmpty()) {
    //    Text(text = "No Description")
    //  return
    //}
    MarkdownText(
        markdown = readmeText.orEmpty(),
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        style = LocalTextStyle.current.copy(color = LocalContentColor.current)
    )
}


@Composable
fun RepoDetailHeader(repo: GetRepoDetailsQuery.Repository) {

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter =
                rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(repo.owner.avatarUrl)
                        .build(),
                    contentScale = ContentScale.Inside,
                )

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = repo.owner.login)
        }
        TitleText(title = repo.name)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_star_24),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Text(text = "${repo.stargazerCount} Stars")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.baseline_fork_left_24),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Text(text = "${repo.forkCount} Forks")
        }

    }
}