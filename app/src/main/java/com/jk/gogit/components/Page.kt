package com.jk.gogit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.components.localproviders.LocalSnackBarHostState
import com.jk.gogit.navigation.AppScreens
import kotlinx.coroutines.flow.collect

@Composable
fun Page(
    title: @Composable () -> Unit = {},
    menuItems: List<DropdownMenuItemContent> = emptyList(),
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = { AppBar(menuItems = menuItems, title = title) },
        floatingActionButton = floatingActionButton,
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = LocalSnackBarHostState.current,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.Bottom)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(menuItems: List<DropdownMenuItemContent>, title: @Composable () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        title = title,
        navigationIcon = {
            val navController = LocalNavController.current
            val isRootScreen = navController.previousBackStackEntry == null
            if (!isRootScreen) NavigationIcon(navController = navController)
        },

        actions = {
            Row(horizontalArrangement = Arrangement.Start) {
                menuItems.forEach { it.menu() }
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "OverFlow",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically)
                )

            }
        }
    )
}

@Composable
private fun NavigationIcon(navController: NavController) {

    Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = "",
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable {
                navController.popBackStack()
            }
    )
}

data class DropdownMenuItemContent(var menu: @Composable () -> Unit)