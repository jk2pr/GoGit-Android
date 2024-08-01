package com.jk.gogit.organisation


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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hoppers.fragment.Org
import com.hoppers.networkmodule.AuthManager
import com.jk.gogit.UiState
import com.jk.gogit.components.OfflineError
import com.jk.gogit.components.OrgItem
import com.jk.gogit.components.Page
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.AppScreens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun OrgListScreen() {

    val localNavyController = LocalNavController.current
    val savedStateHandle = (localNavyController.previousBackStackEntry?.savedStateHandle)
    val login =
        savedStateHandle?.get<String>(AppScreens.ORGLIST.route)
            ?: AuthManager.getLogin()!!

    val viewModel = koinViewModel<OrgListViewModel>(parameters = { parametersOf(login) })
    Page(title = { Text(text = "Organizations") }) {
        when (val result = viewModel.orgStateFlow.collectAsState().value) {
            is UiState.Loading ->
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                )


            is UiState.Error -> OfflineError(message = result.message,)
            is UiState.Empty -> {}
            is UiState.Content -> {

                val nodes = result.data as List<*>
                if (nodes.isEmpty())
                    Text(
                        text = "No organizations found",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                else
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        LazyColumn {
                            items(nodes.size) {
                                if (it > 0) HorizontalDivider()
                                nodes[it]?.let { nodes ->
                                    OrgItem(nodes as Org)
                                }
                            }
                        }
                    }

            }
        }
    }
}
