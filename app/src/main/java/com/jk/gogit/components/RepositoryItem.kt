package com.jk.gogit.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hoppers.fragment.Repos
import com.jk.gogit.R
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.AppScreens

@Composable
fun RepositoryItem(repo: Repos) {
    val localNavController = LocalNavController.current
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable {
                localNavController.currentBackStackEntry
                    ?.savedStateHandle?.let {
                        it[AppScreens.USERPROFILE.route] = repo.owner.login
                        it[AppScreens.REPOLIST.route] = repo.defaultBranchRef?.name.orEmpty()
                        it[AppScreens.REPODETAIL.route] = repo.repoName
                    }
                localNavController.navigate(AppScreens.REPODETAIL.route)
            }
    ) {
        Text(
            text = repo.repoName,
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        if (repo.isFork)
            Text(
                text = "Forked from ${repo.owner.login}/${repo.repoName}",
                style = MaterialTheme.typography.bodySmall.copy(MaterialTheme.colorScheme.primary)
            )
        if (!repo.description.isNullOrBlank()) {
            Text(
                text = repo.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 4.dp)
            )

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ColoredBullet(repo.primaryLanguage?.color)
            Text(
                text = repo.primaryLanguage?.name ?: "",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                IconWithText(
                    res = R.drawable.baseline_star_24,
                    tint = Color(android.graphics.Color.parseColor("#FFA500")),
                    text = repo.stargazerCount.toString(),
                )

                IconWithText(
                    res = R.drawable.baseline_fork_left_24,
                    text = repo.forkCount.toString(),
                )
            }
        }
        Text(
            text = repo.updatedAt.toString(),
            style = MaterialTheme.typography.bodySmall,
            //   fontFamily = FontFamily(Font(R.font.bebasneue_light)),
            textAlign = TextAlign.End
        )
    }

}

