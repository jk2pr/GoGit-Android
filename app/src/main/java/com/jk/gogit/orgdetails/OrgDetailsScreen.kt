package com.jk.gogit.orgdetails

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hoppers.networkmodule.network.AuthManager
import com.jk.gogit.R
import com.jk.gogit.UiState
import com.jk.gogit.components.ComposeLocalWrapper
import com.jk.gogit.components.Page
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.AppScreens
import com.jk.gogit.overview.GistItem
import com.jk.gogit.overview.InfoRow
import com.jk.gogit.overview.PinnedItems
import com.jk.gogit.overview.model.OverViewTabData
import com.jk.gogit.overview.model.OverViewTabData.OverViewScreenData.OverViewItem.PinnedGist
import com.jk.gogit.overview.model.OverViewTabData.OverViewScreenData.OverViewItem.PinnedRepository
import com.jk.gogit.overview.model.OverViewTabData.OverViewScreenData.OverViewItem.PopularRepository
import dev.jeziellago.compose.markdowntext.MarkdownText
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun OrgDetailsScreen() {


    val localNavController = LocalNavController.current
    val login =
        (localNavController.previousBackStackEntry?.savedStateHandle?.get<String>(AppScreens.ORGDETAIL.route)
            ?: AuthManager.getLogin())!!
    val viewModel = koinViewModel<OrgDetailsViewModel>(parameters = { parametersOf(login) })
    val scrollState = rememberScrollState()
    val title = remember { mutableStateOf("") }

    Page(
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


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Top,
                ) {
                    // Header(data = overViewTabData.user)
                    val overViewTabData = result.data as OverViewTabData.OverViewScreenData
                    if (overViewTabData.org == null) return@Page
                    OrgDetailsHeader(data = overViewTabData.org)

                    val file = overViewTabData.html
                    val items = overViewTabData.list
                    if (items.isEmpty() and file.contains("null")) {
                        Text(text = "Nothing to show")
                        return@Column
                    }
                    val hasPinned = items.any { it is PinnedRepository || it is PinnedGist }
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                ImageVector.vectorResource(id = if (hasPinned) R.drawable.pin_26 else R.drawable.baseline_star_24),
                                contentDescription = "",
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (hasPinned) "Pinned" else "Popular",
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        val itemModifier = Modifier
                            .width(200.dp)
                            .height(150.dp)
                            .padding(end = 8.dp)
                        LazyRow(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .wrapContentHeight()
                        ) {
                            items(items.size) { index ->
                                when (val item = items[index]) {
                                    is PinnedRepository -> PinnedItems(
                                        repo = item.repo, modifier = itemModifier
                                    ) { _, _, _ -> }

                                    is PopularRepository -> PinnedItems(
                                        repo = item.repo, modifier = itemModifier
                                    ) { _, _, _ -> }

                                    is PinnedGist -> GistItem(
                                        gist = item.gist, modifier = itemModifier
                                    )


                                }
                            }
                        }
                        Card(
                            border = BorderStroke(DividerDefaults.Thickness, DividerDefaults.color),
                            modifier = Modifier.padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),

                            ) {
                            InfoRow(
                                iconId = R.drawable.git_repository_line_1,
                                label = "Repositories",
                                count = overViewTabData.org.repositories.totalCount,
                            ) {
                                val savedStateHandle =
                                    localNavController.currentBackStackEntry?.savedStateHandle
                                val keys = savedStateHandle?.keys()
                                keys?.forEach { savedStateHandle.remove<Any>(it) }

                                savedStateHandle?.let {
                                    it[AppScreens.REPOLIST.route] = overViewTabData.org.login
                                    //Is Org true
                                    it[AppScreens.REPODETAIL.route] = true
                                }
                                localNavController.navigate(AppScreens.REPOLIST.route)
                            }
                        }
                        if (file == "null") return@Page
                        MarkdownText(
                            markdown = file,
                            modifier = Modifier.padding(vertical = 8.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = LocalContentColor.current
                            )
                        )

                        LaunchedEffect(Unit) {
                            scrollState.scrollTo(0)
                            snapshotFlow { scrollState.value }
                                .collect { scrollOffset ->
                                    // Change title text based on scroll offset
                                    title.value = if (scrollOffset > 100) overViewTabData.org.name
                                        ?: overViewTabData.org.login
                                    else ""
                                }
                        }
                    }
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
        OrgDetailsScreen()
    }
}

