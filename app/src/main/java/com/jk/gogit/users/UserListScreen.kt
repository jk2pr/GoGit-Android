package com.jk.gogit.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hoppers.fragment.UserFields
import com.jk.gogit.R
import com.jk.gogit.UiState
import com.jk.gogit.components.Page
import com.jk.gogit.components.UserItem
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.NavigationArgs
import com.jk.gogit.users.viewmodel.UserListViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun UserListScreen() {

    val savedStateHandle =
        LocalNavController.current.previousBackStackEntry?.savedStateHandle ?: return
    val login = savedStateHandle.get<String>(NavigationArgs.USER_NAME)!!

    val filter = savedStateHandle.get<String>(NavigationArgs.FILTER).orEmpty()
    val repoName = savedStateHandle.get<String>(NavigationArgs.REPO_NAME).orEmpty()

    val viewModel =
        koinViewModel<UserListViewModel>(parameters = { parametersOf(login, filter, repoName) })

    Page(title = { Text(text = filter.replaceFirstChar { it.uppercase() }) }) {

        when (val result = viewModel.userListStateFlow.collectAsState().value) {
            is UiState.Loading -> CircularProgressIndicator()
            is UiState.Content -> {

                val items = result.data as List<*>
                LazyColumn(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier.padding(8.dp).fillMaxSize()
                   ) {
                    items(items.size) { index ->
                        if (index > 0)
                        // Add a line as a separator
                            HorizontalDivider()

                        UserItem(items[index] as UserFields)
                    }
                }
            }

            is UiState.Error -> {
                Text(
                    text = result.message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    //   fontFamily = FontFamily(Font(R.font.bebasneue_regular)),
                    color = Color.Black
                )
            }

            is UiState.Empty -> Text(
                text = stringResource(id = R.string.no_data_available),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                //   fontFamily = FontFamily(Font(R.font.bebasneue_regular)),
                color = Color.Black
            )


        }

    }
}


