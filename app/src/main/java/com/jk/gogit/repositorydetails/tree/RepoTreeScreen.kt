package com.jk.gogit.repositorydetails.tree

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hoppers.GetRepositoryTreeQuery
import com.jk.gogit.R
import com.jk.gogit.UiState
import com.jk.gogit.components.Page
import com.jk.gogit.components.TitleText
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.AppScreens
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RepoTreeScreen() {

    val localNavyController = LocalNavController.current
    val savedStateHandle = localNavyController.previousBackStackEntry?.savedStateHandle
    val login =
        savedStateHandle?.get<String>(AppScreens.USERPROFILE.route)!!
    val repoName =
        savedStateHandle.get<String>(AppScreens.REPODETAIL.route)!!
    val path =
        savedStateHandle.get<String>(AppScreens.REPOLIST.route)

    val viewModel =
        koinViewModel<RepoTreeViewModel>(parameters = {
            parametersOf(
                login,
                repoName,
                "$path:"
            )
        })

    Page(title = {
        Column {
            Text(text = "Files")
        }
    }
    ) {

        when (val result = viewModel.userListStateFlow.collectAsState().value) {
            is UiState.Loading ->
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp)
                )

            is UiState.Content -> {
                val data = (result.data as List<*>)
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        border = BorderStroke(DividerDefaults.Thickness, DividerDefaults.color),
                        modifier = Modifier.padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        LazyColumn(
                            contentPadding = PaddingValues(4.dp),
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                        ) {

                            items(data.size) { index ->
                                if (index > 0)
                                // Add a line as a separator
                                    HorizontalDivider()
                                data[index].let { tree ->
                                    tree as GetRepositoryTreeQuery.Entry
                                    Row(modifier = Modifier.padding(8.dp)) {
                                        Icon(
                                            painter = if (tree.type == "tree")
                                                painterResource(id = R.drawable.folder_svgrepo_com) else painterResource(
                                                id = R.drawable.ic_file_black_24dp
                                            ),
                                            tint = Color(android.graphics.Color.parseColor("#FFC36E")),
                                            contentDescription = "Icon",
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                        Text(text = "${tree.name} ${tree.type}")
                                    }

                                }

                            }
                        }
                    }
                }
            }

            is UiState.Error -> Text(text = result.message)
            is UiState.Empty -> {}
        }

    }
}
