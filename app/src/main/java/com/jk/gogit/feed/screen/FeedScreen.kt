package com.jk.gogit.feed.screen

import com.hoppers.networkmodule.network.AuthManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.jk.gogit.R
import com.jk.gogit.UiState
import com.jk.gogit.components.Page
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.feed.model.Feed
import com.jk.gogit.feed.viewmodel.FeedViewModel
import com.jk.gogit.navigation.AppScreens
import com.jk.gogit.utils.DateUtil
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen() {
    val hasInternetConnection =
        remember { mutableStateOf(true) } // Assuming internet connection by default
    val login = com.hoppers.networkmodule.network.AuthManager.getLogin()
    val viewModel = koinViewModel<FeedViewModel>(parameters = { parametersOf(login) })

    Page {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {

            if (!hasInternetConnection.value) {
                Text(
                    text = stringResource(id = R.string.no_internet),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    //  fontFamily = FontFamily(Font(R.font.bebasneue_regular)),
                    color = Color.Black
                )
            }


            val state = rememberPullToRefreshState()
            if (state.isRefreshing) {
                LaunchedEffect(true) {
                    // fetch something
                    delay(1500)
                    //Logic to refresh
                    state.endRefresh()
                }
            }
            when (val result = viewModel.feedStateFlow.collectAsState().value) {
                is UiState.Loading ->
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                    )

                is UiState.Content ->

                    Box(Modifier.nestedScroll(state.nestedScrollConnection)) {
                        val items = result.data as List<*>
                        LazyColumn(Modifier.fillMaxSize()) {
                            if (!state.isRefreshing) {
                                items(items.size) {
                                    FeedItem(items[it] as Feed)
                                }
                            }
                        }
                        PullToRefreshContainer(
                            modifier = Modifier.align(Alignment.TopCenter),
                            state = state,
                        )
                    }


                is UiState.Error ->
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

            if (!hasInternetConnection.value) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun FeedItem(feed: Feed) {
    val actorName = feed.actor.display_login
    val repoName = feed.repo.name
    val eventName = feed.getEventName()
    val longText = "$actorName $eventName $repoName"
    val time = DateUtil.getDateComparatively(feed.createdAt)

    val navController = LocalNavController.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val (imgActor, column) = createRefs()
            val painter =
                rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(feed.actor.avatar_url)
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .error(R.drawable.ic_broken_image_black_24dp)
                        .crossfade(enable = true)
                        .build(),
                    contentScale = ContentScale.Inside
                )
            Image(
                painter = painter,
                contentDescription = "Image",
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .clickable {
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(AppScreens.USERPROFILE.route, feed.actor.login)
                        navController.navigate(AppScreens.USERPROFILE.route)
                    }
                    .constrainAs(imgActor) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    },
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = Modifier
                    .constrainAs(column) {
                        start.linkTo(imgActor.end, margin = 16.dp)
                        centerVerticallyTo(parent)
                        end.linkTo(parent.end, margin = 16.dp)
                        width = Dimension.fillToConstraints
                    }) {


                Text(
                    softWrap = true,
                    text = longText,
                    color = Color.Black
                )

                Text(
                    text = time,
                    modifier = Modifier.align(Alignment.End),
                    color = Color.Black,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}


@Preview
@Composable
private fun FeedScreenPreview() {
    FeedItem(feed = Feed("", "", Feed.Actor(0, "", "", "", "", ""), Feed.Repo(0, "", ""), null, true,""))
}
