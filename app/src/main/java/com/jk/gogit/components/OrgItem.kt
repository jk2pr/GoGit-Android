package com.jk.gogit.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hoppers.fragment.Org
import com.jk.gogit.R
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.navigation.AppScreens

@Composable
fun OrgItem(org: Org) {
    val localNavController = LocalNavController.current
    val navController = LocalNavController.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(AppScreens.ORGDETAIL.route, org.login)
                navController.navigate(AppScreens.ORGDETAIL.route)
            }

    ) {
        Image(
            painter = rememberAsyncImagePainter(org.avatarUrl),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = org.name.orEmpty().trim(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.widthIn(max = 200.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = org.login,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
            if (!org.description.isNullOrBlank())
                Text(
                    text = org.description,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3,
                    style = MaterialTheme.typography.bodySmall,
                )

        }
    }
}

