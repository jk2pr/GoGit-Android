package com.jk.gogit.repositorydetails.commits

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hoppers.networkmodule.AuthManager
import com.jk.gogit.UiState
import com.jk.gogit.components.Page
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.AppScreens
import com.jk.gogit.repositorydetails.commits.CommitListExecutor.CommitData
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CommitListScreen() {

    val localNavyController = LocalNavController.current
    val savedStateHandle = (localNavyController.previousBackStackEntry?.savedStateHandle)


    val login =
        savedStateHandle?.get<String>(AppScreens.USERPROFILE.route) ?: AuthManager.getLogin()!!
    val branchName = savedStateHandle?.get<String>(AppScreens.REPOLIST.route) ?: false
    val repoName = savedStateHandle?.get<String>(AppScreens.REPODETAIL.route) ?: false
    val viewModel = koinViewModel<CommitListViewModel>(
        parameters = { parametersOf(login, branchName, repoName) }
    )


    Page(title = {
        Text(text = "Commits")
    }) {
        when (val result = viewModel.commListStateFlow.collectAsState().value) {
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
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    val commits = result.data as List<*>
                    if (commits.isEmpty())
                        Text(
                            text = "No repositories found",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxSize(),
                            textAlign = TextAlign.Center
                        )
                    else
                        LazyColumn(
                            Modifier.fillMaxSize(),
                            // contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            commits.forEach { data ->
                                val commitData = data as CommitData


                                val com = commitData.commits
                                items(com.size) { index ->
                                    if (index > 0)
                                    // Add a line as a separator
                                        HorizontalDivider()
                                    CommitsItem(commitData)
                                }
                            }

                        }

                }


            }

        }
    }
}

@Composable
private fun CommitsItem(commit: CommitData) {
    Card(
        border = BorderStroke(DividerDefaults.Thickness, DividerDefaults.color),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    ) {
        Column {


            Text(
                text = commit.date,
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Column {


                val modifier = Modifier
                    //.fillMaxWidth()
                    .padding(8.dp)
                commit.commits.forEach { commit ->
                    Text(
                        text = commit?.description.orEmpty(),
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = modifier
                    )
                    HorizontalDivider()
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
                        Text(
                            text = commit?.message.orEmpty(),
                            modifier = modifier.widthIn(max = 220.dp)
                        )

                        Text(
                            modifier = modifier
                                .align(Alignment.TopEnd),
                            style = MaterialTheme.typography.labelMedium,
                            text = commit?.oid.toString().trim().substring(0, 7),

                            )

                    }
                }
            }
        }
    }

}

