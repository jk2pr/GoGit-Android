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
import com.jk.gogit.R
import com.jk.gogit.components.Page
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.extensions.toColor
import com.jk.gogit.navigation.AppScreens


val GREEN_500 = "#1B5E20".toColor()
val MAGENTA_500 = "#8250F4".toColor()

@Composable
fun PullRequestListScreen() {

    val localNavyController = LocalNavController.current
    val items =
        localNavyController.previousBackStackEntry?.savedStateHandle?.get<Any>(
            AppScreens.PULLREQUESTS.route
        )!!
    items as List<*>

    Page(title = {
        val title = if (items.firstOrNull() is GetRepoDetailsQuery.Pr) "Pull Request" else "Issues"
        Text(title)
    }
    ) {
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
                    val title: String
                    val body: String
                    val number: Int
                    val createdAt: String
                    val authorLogin: String
                    val state: String
                    if (it is GetRepoDetailsQuery.Pr) {
                        title = it.title
                        body = it.body
                        number = it.number
                        createdAt = it.createdAt.toString()
                        authorLogin = it.author?.login.orEmpty()
                        state = it.state.rawValue
                    } else {
                        it as GetRepoDetailsQuery.AllIssue
                        title = it.title
                        body = it.body
                        number = it.number
                        createdAt = it.createdAt.toString()
                        authorLogin = it.author?.login.orEmpty()
                        state = it.state.rawValue

                    }

                    val message = when (state) {
                        "OPEN" -> "#${number} opened on $createdAt by $authorLogin"
                        "CLOSED" -> "#${number} was closed by $authorLogin on $createdAt"
                        "MERGED" -> "#${number} by $authorLogin was merged on $createdAt"
                        else -> ""
                    }

                    val imageVector =
                        ImageVector.vectorResource(
                            if (it is GetRepoDetailsQuery.Pr)  // Handle PR
                                when (state) {
                                    "OPEN" -> R.drawable.git_pull_request_merge
                                    "CLOSED" -> R.drawable.git_pull_request_closed_svgrepo_com
                                    else -> R.drawable.git_pull_request_svgrepo_com
                                }
                            else
                                if (state == "OPEN") R.drawable.issue_opened_16 else R.drawable.issue_closed_svgrepo_com

                        )
                    val tint =
                        if (it is GetRepoDetailsQuery.Pr)
                            when (state) {
                                "OPEN" -> GREEN_500
                                "MERGED" -> MAGENTA_500
                                else -> Color.Red
                            }
                        else if (state == "OPEN") MAGENTA_500 else GREEN_500

                    PullRequestItem(
                        title = title,
                        body = body,
                        message = "$state $message",
                        tint = tint,
                        imageVector = imageVector
                    )
                }
            }
        }
    }

}

@Composable
fun PullRequestItem(
    title: String, body: String,
    message: String, tint: Color,
    imageVector: ImageVector
) {
    val navController = LocalNavController.current




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
            imageVector = imageVector,
            tint = tint,
            contentDescription = null,
            modifier = Modifier
                .size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title.trim(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = body,
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

