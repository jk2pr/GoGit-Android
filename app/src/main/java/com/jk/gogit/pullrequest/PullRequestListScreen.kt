package com.jk.gogit.pullrequest

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hoppers.GetRepoDetailsQuery
import com.hoppers.type.PullRequestState
import com.jk.gogit.R
import com.jk.gogit.components.Page
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.AppScreens

@Composable
fun PullRequestListScreen() {

    val localNavyController = LocalNavController.current
    val items =
        localNavyController.previousBackStackEntry?.savedStateHandle?.get<List<GetRepoDetailsQuery.Node?>?>(
            AppScreens.PULLREQUESTS.route
        )!!

    Page {
        if (items.isEmpty()) {
            Text("No Pull Requests")
            return@Page
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(items.size) { index ->
                if (index > 0)
                // Add a line as a separator
                    HorizontalDivider()
                val it = items[index]
                if (it != null) {
                    PullRequestItem(it)
                }
            }
        }
    }

}

@Composable
fun PullRequestItem(node: GetRepoDetailsQuery.Node) {
    val navController = LocalNavController.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(AppScreens.USERPROFILE.route, node.author)
                navController.navigate(AppScreens.USERPROFILE.route)
            }

    ) {
        val modifier = Modifier.rotate(90f)
        Icon(
            imageVector = ImageVector.vectorResource(
                id = if (node.state == PullRequestState.OPEN)
                    R.drawable.baseline_fork_left_24
                else
                    R.drawable.git_pull_request_svgrepo_com
            ),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp).then(modifier)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = node.title.trim(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.widthIn(max = 200.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = node.body,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium.merge(Color.Gray),
                )
            }
            Text(
                text = node.createdAt.toString(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall.merge(Color.Gray),
            )
        }
    }
}

