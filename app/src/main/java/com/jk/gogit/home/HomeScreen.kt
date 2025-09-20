package com.jk.gogit.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hoppers.networkmodule.AuthManager
import com.jk.gogit.R
import com.jk.gogit.UiState
import com.jk.gogit.components.DropdownMenuItemContent
import com.jk.gogit.components.HyperLinkText
import com.jk.gogit.components.OfflineError
import com.jk.gogit.components.Page
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.home.model.Feed
import com.jk.gogit.navigation.AppScreens
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun HomeScreen() {
    val login = AuthManager.getLogin()
    val getAvatarUrl = AuthManager.getAvatarUrl()
    val viewModel = koinViewModel<FeedViewModel>(parameters = { parametersOf(login) })
    val navController = LocalNavController.current
    val onSearchClick: () -> Unit = { navController.navigate(AppScreens.SEARCH.route) }
    val onProfileClick: () -> Unit = {
        navController.currentBackStackEntry?.savedStateHandle
            ?.set(AppScreens.USERPROFILE.route, login)
        navController.navigate(AppScreens.USERPROFILE.route)
    }

    Page(
        title = { Text(stringResource(id = R.string.app_name)) },
        menuItems = listOf(
            DropdownMenuItemContent {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier
                            .clip(CircleShape),
                    )
                }
                IconButton(onClick = onProfileClick) {
                    val imagePainter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(getAvatarUrl)
                            .placeholder(R.drawable.face_24dp_fill0_wght400_grad0_opsz24)
                            .error(R.drawable.face_24dp_fill0_wght400_grad0_opsz24)
                            .crossfade(enable = true)
                            .build(),
                        contentScale = ContentScale.Inside
                    )
                    Image(
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(24.dp),
                        painter = imagePainter
                    )
                }
            }
        ),
        content = {
            when (val result = viewModel.feedStateFlow.collectAsState().value) {
                is UiState.Loading ->
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                    )

                is UiState.Content -> {
                    (result.data as List<*>).let { items ->
                        LazyColumn(
                            Modifier
                                .padding(8.dp)
                                .fillMaxSize()
                        ) {
                            items(items.size) {
                                if (it > 0) HorizontalDivider()
                                FeedItem(feed = items[it] as Feed,
                                    onActorClick = { feed ->
                                        navController.currentBackStackEntry
                                            ?.savedStateHandle
                                            ?.set(AppScreens.USERPROFILE.route, feed.actor.login)
                                        navController.navigate(AppScreens.USERPROFILE.route)
                                    },
                                    onRepoClick = { url ->
                                        val a = url.split("/")
                                        val owner = a[4]
                                        val repo = a[5]
                                        navController.currentBackStackEntry
                                            ?.savedStateHandle?.let { s ->
                                                s[AppScreens.USERPROFILE.route] = owner
                                                s[AppScreens.REPODETAIL.route] = repo
                                            }

                                        navController.navigate(AppScreens.REPODETAIL.route)
                                    })
                            }
                        }
                    }
                }


                is UiState.Error -> OfflineError(message = result.message)


                is UiState.Empty -> {}


            }
        })
}

@Composable
fun FeedItem(feed: Feed, onActorClick: (Feed) -> Unit = {}, onRepoClick: (String) -> Unit = {}) {
    val actorName = feed.actor.display_login
    val eventName = feed.getEventName()
    val time = feed.createdAt

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(feed.actor.avatar_url)
            .placeholder(R.drawable.ic_image_black_24dp)
            .error(R.drawable.ic_broken_image_black_24dp)
            .crossfade(enable = true)
            .build(),
        contentScale = ContentScale.Inside
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = "Image",
            modifier = Modifier
                .align(Alignment.Top)
                .size(48.dp)
                .clip(CircleShape)
                .clickable { onActorClick(feed) },
            contentScale = ContentScale.FillBounds
        )
        Column(modifier = Modifier.fillMaxWidth()) {

            HyperLinkText(
                hyperLink = actorName,
                localStringStyle = LocalTextStyle.current,
                modifier = Modifier,
                localString = " $eventName",
                startIndex = 0,
                endIndex = actorName.length,
                action = { onActorClick(feed) })

            Text(
                text = time,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.End
            )
            val data = feed.repo
            TextButton(
                contentPadding = PaddingValues(0.dp),
                onClick = { onRepoClick(data.url) },
                content = {
                    Text(text = data.name)
                }
            )

        }
    }


}


@Preview
@Composable
private fun FeedScreenPreview() {
    FeedItem(
        feed = Feed(
            "",
            "",
            Feed.Actor(0, "jie", "", "", "", ""),
            Feed.Repo(0, "", ""),
            null,
            true,
            ""
        )
    )
}