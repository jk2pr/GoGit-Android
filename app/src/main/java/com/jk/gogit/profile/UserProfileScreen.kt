package com.jk.gogit.profile

import android.util.Log
import androidx.compose.animation.Crossfade
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hoppers.networkmodule.network.AuthManager
import com.jk.gogit.UiState
import com.jk.gogit.components.ComposeLocalWrapper
import com.jk.gogit.components.Page
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.AppScreens
import com.jk.gogit.overview.OverViewTab
import com.jk.gogit.overview.model.OverViewTabData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun UserProfileScreen() {


    val localNavController = LocalNavController.current
    val localContext = LocalContext.current
    val login =
        (localNavController.previousBackStackEntry?.savedStateHandle?.get<String>(AppScreens.USERPROFILE.route)
            ?: AuthManager.getLogin())!!
    val viewModel = koinViewModel<UserProfileViewModel>(parameters = { parametersOf(login) })
    val scrollState = rememberScrollState()
    val title = remember { mutableStateOf("") }

    Page(
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
        title = {
            Crossfade(targetState = title.value, label = "") { currentTitle ->
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = currentTitle,
                )
            }

        }) {
        when (val result = viewModel.feedStateFlow.collectAsState().value) {
            is UiState.Loading ->
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                )

            is UiState.Content -> {

                if (result.data is OverViewTabData.OverViewScreenData) {
                    val overViewTabData = result.data

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.Top,
                    ) {
                        UserProfileHeader(data = overViewTabData.user!!, onFollow = { viewerIsFollowing ->
                            if (viewerIsFollowing) // if following
                            viewModel.setState(UserProfileViewModel.MainState.UnFollowEvent(overViewTabData.user.id))
                            else //Not following
                            viewModel.setState(UserProfileViewModel.MainState.FollowEvent(overViewTabData.user.id))
                        })

                        OverViewTab(overViewTabData = overViewTabData){ ownerName, repoName, defaultBranch ->
                            localNavController.currentBackStackEntry
                                ?.savedStateHandle?.let {
                                    it[AppScreens.USERPROFILE.route] = ownerName
                                    it[AppScreens.REPOLIST.route] = defaultBranch
                                    it[AppScreens.REPODETAIL.route] = repoName
                                }
                            localNavController.navigate(AppScreens.REPODETAIL.route)

                        }
                    }
                    title.value =
                        overViewTabData.user?.name ?: overViewTabData.user?.login.orEmpty()
                    /* LaunchedEffect(Unit) {
                    scrollState.scrollTo(0)
                    snapshotFlow { scrollState.value }
                        .collect { scrollOffset ->
                            // Change title text based on scroll offset
                            title.value = if (scrollOffset > 100) overViewTabData.user.name
                                ?: overViewTabData.user.login else localContext.getString(R.string.app_name)
                        }
                }*/
                } else {
                    Log.d("UserProfileScreen", "Follow ${result.data}")

                }
            }

            is UiState.Error -> Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = result.message,
                softWrap = true,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error)
            )

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

