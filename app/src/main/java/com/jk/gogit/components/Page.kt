package com.jk.gogit.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.components.localproviders.LocalSnackBarHostState
import com.tusharhow.connext.helper.CheckConnectivityStatus

@Composable
fun Page(
    title: @Composable () -> Unit = {},
    menuItems: List<DropdownMenuItemContent> = emptyList(),
    floatingActionButton: @Composable () -> Unit = {},
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = { AppBar(menuItems = menuItems, title = title) },
        floatingActionButton = floatingActionButton,
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()),
                contentAlignment = contentAlignment
            ) {
                CheckConnectivityStatus(
                    connectedContent = { content() },
                    disconnectedContent = { OfflineError() }
                )
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
        title = title,
        actions = { menuItems.forEach { it.menu() } },
        navigationIcon = {
            val navController = LocalNavController.current
            val isRootScreen = navController.previousBackStackEntry == null
            if (!isRootScreen) NavigationIcon(navController = navController)
        },

        )
}

@Composable
private fun NavigationIcon(navController: NavController) {

    IconButton(
        onClick = { navController.popBackStack() },
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "",
        )
    }
}

data class DropdownMenuItemContent(var menu: @Composable () -> Unit)
