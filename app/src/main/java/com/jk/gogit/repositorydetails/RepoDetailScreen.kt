package com.jk.gogit.repositorydetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hoppers.GetRepoDetailsQuery
import com.hoppers.type.IssueState
import com.jk.gogit.R
import com.jk.gogit.UiState
import com.jk.gogit.components.Page
import com.jk.gogit.components.TitleText
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.AppScreens
import com.jk.gogit.navigation.NavigationArgs
import com.jk.gogit.overview.InfoRow
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RepoDetailScreen() {

    val localNavyController = LocalNavController.current
    val savedStateHandle = localNavyController.previousBackStackEntry?.savedStateHandle
    val login =
        savedStateHandle?.get<String>(AppScreens.USERPROFILE.route)!!
    val repoName =
        savedStateHandle.get<String>(AppScreens.REPODETAIL.route)!!
    val path =
        savedStateHandle.get<String>(AppScreens.REPOLIST.route)

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetail(repo: GetRepoDetailsQuery.Repository) {
    val localNavController = LocalNavController.current
    val modalSheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
    val selectedBranch = remember { mutableStateOf(repo.defaultBranchRef?.name.orEmpty()) }
    Card(
        border = BorderStroke(DividerDefaults.Thickness, DividerDefaults.color),
        modifier = Modifier.padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        InfoRow(
            iconId = R.drawable.issue_opened_16,
            label = "Issue",
            count = repo.issues.allIssues?.count { it?.state == IssueState.OPEN }
        ){
            repo.issues.allIssues?.joinToString(separator = ",") { it.toString() }
            localNavController.currentBackStackEntry
                ?.savedStateHandle
                ?.set(AppScreens.PULLREQUESTS.route, repo.issues.allIssues)
            localNavController.navigate(AppScreens.PULLREQUESTS.route)
        }

        InfoRow(
            iconId = R.drawable.git_pull_request_merge,
            label = "Pull Requests",
            count = repo.pullRequests.pr?.size
        ) {
            repo.pullRequests.pr?.joinToString(separator = ",") { it.toString() } // Convert to string

            localNavController.currentBackStackEntry
                ?.savedStateHandle
                ?.set(AppScreens.PULLREQUESTS.route, repo.pullRequests.pr)
            localNavController.navigate(AppScreens.PULLREQUESTS.route)
        }
        InfoRow(
            iconId = R.drawable.baseline_star_24,
            label = "Stars",
            count = repo.stargazerCount,
            tint = Color(android.graphics.Color.parseColor("#FFA500")),
        ){
            localNavController.currentBackStackEntry
                ?.savedStateHandle
                ?.set(AppScreens.USERPROFILE.route, "${repo.owner.login}/${repo.name}/stargazers")
            localNavController.navigate(AppScreens.USERLIST.route)

        }
        InfoRow(iconId = R.drawable.baseline_fork_left_24, label = "Forks", count = repo.forkCount)
        {

                val savedStateHandle =
                    localNavController.currentBackStackEntry?.savedStateHandle
                val keys = savedStateHandle?.keys()
                keys?.forEach { savedStateHandle.remove<Any>(it) }

                savedStateHandle?.let {
                    it[AppScreens.REPOLIST.route] = repo.owner.login
                    it[NavigationArgs.REPO_NAME] = repo.name
                    it[NavigationArgs.FILTER] = "Forks"
                }
                localNavController.navigate(AppScreens.REPOLIST.route)

        }
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
                    Text(text = selectedBranch.value,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1, modifier = Modifier.widthIn(max = 200.dp))
                }
                TextButton(onClick = { isSheetOpen = true }) {
                    Text(text = "Change branch")
                }
            }
            HorizontalDivider()
            InfoRow(iconId = R.drawable.code_2_svgrepo_com, label = "Code"){
                localNavController.currentBackStackEntry
                    ?.savedStateHandle?.let {
                        it[AppScreens.USERPROFILE.route] = repo.owner.login
                        it[AppScreens.REPOLIST.route] = selectedBranch.value
                        it[AppScreens.REPODETAIL.route] = repo.name
                    }
                localNavController.navigate(AppScreens.REPOTREESCREEN.route)
            }
            InfoRow(iconId = R.drawable.commit_svgrepo_com, label = "Commits", count = repo.refs?.commits?.find {
                it?.name == selectedBranch.value
            }?.target?.onCommit?.history?.totalCount){

                localNavController.currentBackStackEntry
                    ?.savedStateHandle?.let {
                                it[AppScreens.USERPROFILE.route] = repo.owner.login
                                it[AppScreens.REPOLIST.route] = selectedBranch.value
                                it[AppScreens.REPODETAIL.route] = repo.name
                            }

                localNavController.navigate(AppScreens.COMMITLIST.route)
            }
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
    if (isSheetOpen)
        repo.refs?.commits.orEmpty().let {
            BottomSheetLayout(
                modalSheetState = modalSheetState,
                selectedBranch = selectedBranch.value,
                defaultBranch = repo.defaultBranchRef?.name.orEmpty(),
                data = it,
                onDismiss = { isSheetOpen = false },
                onBranchSelected = { sb ->
                    selectedBranch.value = sb
                })
        }
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
        if (repo.isFork)
            Text(
                text = "Forked from ${repo.owner.login}/${repo.name}",
                style = MaterialTheme.typography.bodySmall.copy(MaterialTheme.colorScheme.primary)
            )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_star_24),
                contentDescription = null,
                tint = Color(android.graphics.Color.parseColor("#FFA500")),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetLayout(
    modalSheetState: SheetState,
    defaultBranch: String,
    selectedBranch: String,
    data: List<GetRepoDetailsQuery.Commit?>,
    onBranchSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val isSheetFullScreen by remember { mutableStateOf(false) }
    val modifier = if (isSheetFullScreen)
        Modifier
            .fillMaxSize()
    else
        Modifier.fillMaxWidth()

    BackHandler(modalSheetState.isVisible) {
        coroutineScope.launch { modalSheetState.hide() }
    }

    ModalBottomSheet(
        sheetState = modalSheetState,
        onDismissRequest = { onDismiss() },
        content = {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(4.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                ) {
                    items(data.size) { index ->
                        if (index > 0)
                        // Add a line as a separator
                            HorizontalDivider()
                        data[index]?.let { branch ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        onBranchSelected(branch.name)
                                    }
                                //  horizontalArrangement = if (index == 0) Arrangement.SpaceEvenly else Arrangement.Start
                            ) {
                                Row {
                                    Text(text = branch.name, modifier = Modifier.widthIn(max =250.dp))
                                }
                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp)) {
                                    Card {
                                        if (branch.name == defaultBranch) {
                                            Text(
                                                text = "default",
                                                modifier = Modifier.padding(horizontal = 8.dp)
                                            )
                                            Spacer(modifier = Modifier.width(24.dp))
                                        }
                                    }
                                    if (branch.name == selectedBranch)
                                        Icon(
                                            modifier = Modifier.align(Alignment.CenterEnd),
                                            imageVector = Icons.Outlined.CheckCircle,
                                            contentDescription = "Done Icon",
                                            tint = MaterialTheme.colorScheme.primary,
                                            //modifier = Modifier.sizeIn(48.dp)

                                        )

                                }
                            }
                        }
                    }
                }
            }


        })
}