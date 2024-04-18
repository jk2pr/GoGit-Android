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
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            items(items.size) { index ->
                if (index > 0)
                // Add a line as a separator
                    HorizontalDivider()
                items[index]?.let {
                    PullRequestItem(it)
                }
            }
        }
    }

}

@Composable
fun PullRequestItem(node: GetRepoDetailsQuery.Node) {
    val navController = LocalNavController.current

    val message = when(node.state){
        PullRequestState.OPEN -> "#${node.number} opened on ${node.createdAt} by ${node.author?.login}"
        PullRequestState.CLOSED -> "#${node.number} was closed by ${node.author?.login} on ${node.createdAt}"
        PullRequestState.MERGED ->"#${node.number} by ${node.author?.login} was merged on ${node.createdAt}"
        PullRequestState.UNKNOWN__ -> ""
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                //   navController.currentBackStackEntry
                //    ?.savedStateHandle
                //    ?.set(AppScreens.USERPROFILE.route, node.author)
                // navController.navigate(AppScreens.USERPROFILE.route)
            }

    ) {
        Icon(
            imageVector = ImageVector.vectorResource(
                id = if (node.state == PullRequestState.OPEN)
                    R.drawable.git_pull_request_merge
                else
                    R.drawable.git_pull_request_svgrepo_com

            ),
            tint = if(node.state == PullRequestState.MERGED)  Color.Magenta else Color.Green,
            contentDescription = null,
            modifier = Modifier
                .size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = node.title.trim(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = node.body,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium.merge(Color.Gray),
                )
            }

            Text(
                text = message,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall.merge(Color.Gray),
            )
        }
    }
}

