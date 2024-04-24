package com.jk.gogit.repositorydetails.tree

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hoppers.GetRepoDetailsQuery
import com.hoppers.GetRepositoryTreeQuery
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
                val repo = (result.data as GetRepositoryTreeQuery.Data).repository
                LazyColumn(
                    contentPadding = PaddingValues(4.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                ) {

                    val data = repo?.`object`?.onTree?.entries.orEmpty()
                    items(data.size) { index ->
                        if (index > 0)
                        // Add a line as a separator
                            HorizontalDivider()
                        data[index].let { tree ->

                            Row {
                                Text(text = tree.name)
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
