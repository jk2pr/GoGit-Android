package com.jk.gogit.overview


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hoppers.GetUserQuery
import com.hoppers.fragment.GistFields
import com.hoppers.fragment.Repos
import com.jk.gogit.R
import com.jk.gogit.components.ColoredBullet
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.AppScreens
import com.jk.gogit.overview.model.OverViewTabData
import com.jk.gogit.overview.model.OverViewTabData.OverViewScreenData.OverViewItem.PinnedGist
import com.jk.gogit.overview.model.OverViewTabData.OverViewScreenData.OverViewItem.PinnedRepository
import com.jk.gogit.overview.model.OverViewTabData.OverViewScreenData.OverViewItem.PopularRepository
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun OverViewTab(
    overViewTabData: OverViewTabData.OverViewScreenData,
    onClick: (String, String, String) -> Unit
) {

    //val viewModel = koinViewModel<OverViewModel>(parameters = { parametersOf(login) })


    val file = overViewTabData.html
    val items = overViewTabData.list
    if (items.isEmpty() and file.contains("null")) {
        Text(text = "Nothing to show")
        return
    }
    val hasPinned = items.any { it is PinnedRepository || it is PinnedGist }
    Column(
        modifier = Modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                ImageVector.vectorResource(id = if (hasPinned) R.drawable.pin_26 else R.drawable.baseline_star_24),
                contentDescription = "",
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = if (hasPinned) "Pinned" else "Popular",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        val itemModifier = Modifier
            .width(200.dp)
            .height(150.dp)
        // .padding(end = 8.dp)
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items.size) { index ->
                if (index > 0) VerticalDivider(thickness = 8.dp)
                when (val item = items[index]) {
                    is PinnedRepository -> PinnedItems(
                        repo = item.repo, modifier = itemModifier
                    ) { ownerName, repoName, defaultBranch ->
                        onClick(ownerName, repoName, defaultBranch)

                    }

                    is PopularRepository -> PinnedItems(
                        repo = item.repo, modifier = itemModifier
                    ) { ownerName, repoName, defaultBranch ->
                        onClick(ownerName, repoName, defaultBranch)

                    }


                    is PinnedGist -> GistItem(
                        gist = item.gist, modifier = itemModifier
                    )


                }
            }
        }
        InfoCard(user = overViewTabData.user!!)
        if (file.isEmpty() and (file == "null")) return
        MarkdownText(
            markdown = file,
            modifier = Modifier.padding(vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = LocalContentColor.current
            )
        )
    }


}


@Composable
fun PinnedItems(repo: Repos, modifier: Modifier, onClick: (String, String, String) -> Unit) {
    ElevatedCard(
        modifier = modifier.clickable {
            onClick(repo.owner.login, repo.repoName, repo.defaultBranchRef?.name.orEmpty())
        },
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val painter =
                    rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(repo.owner.avatarUrl)
                            .build(),
                        contentScale = ContentScale.Inside,

                        )
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = repo.owner.login)
            }
            Text(
                text = repo.repoName,
                style = MaterialTheme.typography.labelLarge.merge(MaterialTheme.colorScheme.primary),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = repo.description.orEmpty(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColoredBullet(repo.primaryLanguage?.color)
                Spacer(modifier = Modifier.width(4.dp))
                repo.primaryLanguage?.name?.let {
                    Text(
                        text = repo.primaryLanguage.name,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_star_24),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = repo.stargazerCount.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        //  fontFamily = FontFamily(Font(R.font.bebasneue_light)),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_fork_left_24),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = repo.forkCount.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        //  fontFamily = FontFamily(Font(R.font.bebasneue_light)),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            Text(
                text = repo.updatedAt.toString(),
                style = MaterialTheme.typography.labelSmall,
                //   fontFamily = FontFamily(Font(R.font.bebasneue_light)),
                fontSize = 12.sp,
                textAlign = TextAlign.End
            )
        }
    }

}

@Composable
fun GistItem(gist: GistFields, modifier: Modifier) {
    ElevatedCard(
        modifier = modifier.clickable {
            //onClick()
        },
    )
    {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = gist.files?.first()?.name.orEmpty(),
                style = MaterialTheme.typography.labelLarge.merge(MaterialTheme.colorScheme.primary),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = gist.files?.first()?.text.orEmpty(),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun InfoCard(user: GetUserQuery.User) {
    val localNavController = LocalNavController.current
    OutlinedCard {
        InfoRow(
            iconId = R.drawable.git_repository_line_1,
            label = "Repositories",
            count = user.repositories.totalCount,
        ) {
            val savedStateHandle = localNavController.currentBackStackEntry?.savedStateHandle
            val keys = savedStateHandle?.keys()
            keys?.forEach { savedStateHandle.remove<Any>(it) }
            savedStateHandle?.set(
                AppScreens.REPOLIST.route,
                user.login
            )
            localNavController.navigate(AppScreens.REPOLIST.route)
        }
        InfoRow(
            iconId = R.drawable.organization_65,
            label = "Organizations",
            count = user.organizations.totalCount,
            tint = Color.Red
        ) {
            val savedStateHandle = localNavController.currentBackStackEntry?.savedStateHandle
            val keys = savedStateHandle?.keys()
            keys?.forEach { savedStateHandle.remove<Any>(it) }
            savedStateHandle?.set(
                AppScreens.ORGLIST.route,
                user.login
            )
            localNavController.navigate(AppScreens.ORGLIST.route)

        }
        InfoRow(
            iconId = R.drawable.baseline_star_24,
            label = "Starred",
            count = user.starredRepositories.totalCount,
            tint = Color(android.graphics.Color.parseColor("#FFA500"))
        ) {
            val savedStateHandle = localNavController.currentBackStackEntry?.savedStateHandle
            val keys = savedStateHandle?.keys()
            keys?.forEach { savedStateHandle.remove<Any>(it) }
            savedStateHandle?.let {
                it[AppScreens.REPOLIST.route] = user.login
                //Is Starred true
                it[AppScreens.USERPROFILE.route] = true
            }


            localNavController.navigate(AppScreens.REPOLIST.route)
        }

    }
}

@Composable
fun InfoRow(
    iconId: Int,
    label: String,
    count: Int? = null,
    tint: Color = LocalContentColor.current,
    onClick: (() -> Unit) = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                ImageVector.vectorResource(id = iconId),
                contentDescription = "",
                tint = tint,
                modifier = Modifier.size(16.dp),
            )
            Text(text = label, style = MaterialTheme.typography.labelLarge)
        }
        count?.let {
            Text(text = count.toString(), style = MaterialTheme.typography.labelLarge)
        }
    }
}


