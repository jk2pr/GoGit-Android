package com.jk.gogit.profile

import com.hoppers.networkmodule.network.AuthManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jk.gogit.R
import com.jk.gogit.UiState
import com.jk.gogit.components.ComposeLocalWrapper
import com.jk.gogit.components.DropdownMenuItemContent
import com.jk.gogit.components.Page
import com.jk.gogit.components.TitleText
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.AppScreens
import com.jk.gogit.overview.OverViewTab
import com.jk.gogit.overview.model.OverViewTabData
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun UserProfileScreen() {

    val menuItems: List<DropdownMenuItemContent> = remember {
        listOf(DropdownMenuItemContent {

            Icon(
                painter = painterResource(id = R.drawable.baseline_edit_24),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        //Handle click event
                    }
            )

        })
    }


    val localNavController = LocalNavController.current
    val login =
        (localNavController.previousBackStackEntry?.savedStateHandle?.get<String>(AppScreens.USERPROFILE.route)
            ?: com.hoppers.networkmodule.network.AuthManager.getLogin())!!
    val viewModel = koinViewModel<UserProfileViewModel>(parameters = { parametersOf(login) })
    val scrollState = rememberScrollState()
    val title = remember { mutableStateOf("") }

    Page(menuItems = menuItems,
        floatingActionButton = {
            FloatingActionButton(
                shape = MaterialTheme.shapes.extraLarge,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp),
                onClick = { localNavController.navigate(AppScreens.SEARCH.route) }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(24.dp)
                )

            }
        },
        title = { TitleText(title.value) }) {
        when (val result = viewModel.feedStateFlow.collectAsState().value) {
            is UiState.Loading ->
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                )

            is UiState.Content -> {
                val overViewTabData = result.data as OverViewTabData.OverViewScreenData
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Top,
                ) {
                    Header(data = overViewTabData.user)

                    OverViewTab(overViewTabData = overViewTabData)
                }
                LaunchedEffect(Unit) {
                    scrollState.scrollTo(0)
                    snapshotFlow { scrollState.value }
                        .collect { scrollOffset ->
                            // Change title text based on scroll offset
                            title.value = if (scrollOffset > 100) overViewTabData.user.name
                                ?: overViewTabData.user.login else ""
                        }
                }
            }

            is UiState.Error -> Text(text = result.message)
            is UiState.Empty -> {}
        }

    }
}

@Preview
@Composable
fun UserProfileScreenPreview() {
    ComposeLocalWrapper {
        UserProfileScreen()
    }
}

