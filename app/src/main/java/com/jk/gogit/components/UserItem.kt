package com.jk.gogit.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hoppers.fragment.UserFields
import com.jk.gogit.R
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.AppScreens

@Composable
fun UserItem(node: UserFields) {
    val navController = LocalNavController.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(AppScreens.USERPROFILE.route, node.login)
                navController.navigate(AppScreens.USERPROFILE.route)
            }

    ) {
        Image(
            painter = rememberAsyncImagePainter(node.avatarUrl),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = node.name.orEmpty().trim(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.widthIn(max = 200.dp)
                )
                Text(
                    text = node.login,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            if (!node.bio.isNullOrBlank())
                Text(
                    text = node.bio,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (!node.company.isNullOrBlank()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.outline_home_work_24),
                            contentDescription = "",
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            text = node.company,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .widthIn(max = 150.dp)
                        )
                    }
                }

                if (!node.location.isNullOrBlank()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.outline_location_on_24),
                            contentDescription = "",
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            text = node.location,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}