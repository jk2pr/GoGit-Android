package com.jk.gogit.orgdetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hoppers.GetOrganizationDetailQuery
import com.jk.gogit.R
import com.jk.gogit.components.localproviders.LocalNavController

@Composable
fun OrgDetailsHeader(
    data: GetOrganizationDetailQuery.Organization,
    modifier: Modifier = Modifier
) {
    val localNavController = LocalNavController.current

    Column(modifier = modifier) {
        val painter =
            rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data.avatarUrl)
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .crossfade(enable = true)
                    .build(),
                contentScale = ContentScale.Inside
            )


        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {

                /* if (!data.name.isNullOrBlank()) {
                    Text(text = data.name)
                }*/
                Text(
                    softWrap = true,
                    text = data.name.orEmpty(),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    softWrap = true,
                    text = data.login,
                    style = MaterialTheme.typography.bodyMedium
                )

                BulletList(
                    style = MaterialTheme.typography.bodyMedium,
                    items = mapOf(
                        Icons.Outlined.MailOutline to data.email,
                        Icons.Outlined.LocationOn to data.location,
                    )
                )


            }
        }

        Column {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)

            ) {
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .weight(0.5f)
                ) {
                    /* BulletList(
                        style = MaterialTheme.typography.bodyMedium,
                        items = mapOf(ImageVector.vectorResource(id = R.drawable.outline_home_work_24) to data.company)
                    )*/
                    BulletList(
                        style = MaterialTheme.typography.bodyMedium,
                        maxLine = 1,
                        items = mapOf(ImageVector.vectorResource(id = R.drawable.baseline_link_24) to data.websiteUrl?.toString())
                    )

                }


            }
            BulletList(
                style = MaterialTheme.typography.bodyMedium,
                items = mapOf(Icons.Outlined.Info to data.description)

            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            OutlinedButton(
                contentPadding = PaddingValues(horizontal = 24.dp),
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                //colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.outline, contentColor = MaterialTheme.colorScheme.onPrimary),
                onClick = { /* Follow Button Clicked */ },
            ) {
                Text(
                    text = "Follow",
                    // style = TextStyle(color = Color.Blue),
                    //   modifier = Modifier.wrapContentWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
    HorizontalDivider()
}


@Composable
fun BulletList(
    modifier: Modifier = Modifier,
    maxLine: Int = Int.MAX_VALUE,
    tint: Color = LocalContentColor.current,
    style: TextStyle,
    lineSpacing: Dp = 8.dp,
    items: Map<ImageVector, String?>,
) {
    Column(modifier = modifier) {
        items.forEach {
            if (it.value.isNullOrBlank()) return@forEach
            Row(
                verticalAlignment = if (maxLine == 1) Alignment.CenterVertically else Alignment.Top
            ) {
                Icon(
                    imageVector = it.key,
                    contentDescription = "",
                    modifier = modifier.size(16.dp),
                    tint = tint
                )
                Spacer(modifier = Modifier.size(lineSpacing))
                Text(
                    text = it.value!!,
                    style = style,
                    maxLines = maxLine,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = true),
                )
            }

        }
    }
}