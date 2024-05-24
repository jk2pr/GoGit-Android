package com.jk.gogit.repositorydetails.tree

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
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
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.AppScreens
import com.jk.gogit.navigation.NavigationArgs
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
    val branch =
        savedStateHandle.get<String>(AppScreens.REPOLIST.route)
    val data = remember { mutableStateListOf<GetRepositoryTreeQuery.Entry>() }
    val filePath = remember { mutableStateListOf<PathToFile>() }
    var currentPath = "$branch:"
    val viewModel =
        koinViewModel<RepoTreeViewModel>(parameters = { parametersOf(login, repoName, "$branch:") })

    val onPathSelected: (PathToFile) -> Unit = { p ->
        //filePath.clear()
        val index = filePath.indexOf(p)
        filePath.removeRange(index + 1, filePath.size)
        currentPath = p.path
        data.clear()
        data.addAll(p.file)
    }
    val onFolderSelected: (GetRepositoryTreeQuery.Entry) -> Unit = { tree ->

        if (tree.type == "tree") {
            val delimiters = if (currentPath.last() == ':') "" else "/"
            currentPath = (currentPath + delimiters + tree.name)
            viewModel.setState(
                RepoTreeViewModel.MainState.RefreshEvent(currentPath)
            )
        } else {

            localNavyController.currentBackStackEntry?.savedStateHandle?.let {
                it[NavigationArgs.FILE_NAME] = tree.name
                it[NavigationArgs.FILE_CONTENT] =  tree.`object`?.onBlob?.text
            }
            localNavyController.navigate(AppScreens.FILECONTENT.route)
        }
    }

    Page(title = { Text(text = "Files") }) {

        Form(
            filePath = filePath,
            data = data,
            onPathSelected = onPathSelected,
            onFolderSelected = onFolderSelected
        )
        when (val result = viewModel.userListStateFlow.collectAsState().value) {
            is UiState.Loading -> {}
            is UiState.Empty -> {}
            is UiState.Error -> Text(text = result.message)
            is UiState.Content -> {
                val d = result.data as PathToFile
                data.apply {
                    clear()
                    addAll(d.file)
                }
                filePath.add(d)
            }
        }
    }

}

@Composable
private fun Form(
    filePath: MutableList<PathToFile>,
    data: List<GetRepositoryTreeQuery.Entry>,
    onPathSelected: (PathToFile) -> Unit,
    onFolderSelected: (GetRepositoryTreeQuery.Entry) -> Unit
) {
    if (data.isEmpty()) return
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        LazyRow {
            items(filePath.size) {
                val path = filePath[it]
                Row(modifier = Modifier.clickable {
                    onPathSelected(path)
                }) {
                    val branch = path.path.split(":").first()
                    val t = path.path.replace("$branch:", "")
                    Text(text = t.split("/").lastOrNull() ?: t)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chevron_right_white_24dp),
                        contentDescription = "separator"
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
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
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                onFolderSelected(tree)
                            }) {
                            Icon(
                                painter = painterResource(
                                    if (tree.type == "tree")
                                        R.drawable.folder_svgrepo_com else
                                        R.drawable.ic_file_black_24dp
                                ),
                                tint = Color(android.graphics.Color.parseColor("#FFC36E")),
                                contentDescription = "Icon",
                                modifier = Modifier.size(24.dp)

                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(text = tree.name)
                        }
                    }

                }
            }
        }
    }
}
