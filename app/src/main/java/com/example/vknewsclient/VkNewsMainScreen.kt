package com.example.vknewsclient

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vknewsclient.navigation.AppNavGraph
import com.example.vknewsclient.navigation.rememberNavigationState
import com.example.vknewsclient.ui.HomeScreen
import com.example.vknewsclient.ui.NavigationItem
import com.example.vknewsclient.ui.theme.VkNewsClientTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navigationState = rememberNavigationState()

    Scaffold(modifier = Modifier.padding(8.dp), topBar = {
        TopAppBar(title = { Text(text = "Top App Bar title") }, navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = null)
            }
        })
    }, bottomBar = {
        NavigationBar {
            val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val items = listOf(
                NavigationItem.Home,
                NavigationItem.Favorite,
                NavigationItem.Profile
            )

            items.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.screen.route,
                    onClick = {
                        navigationState.navigateTo(item.screen.route)
                    },
                    icon = {
                        Icon(item.icon, contentDescription = null)
                    },
                    label = {
                        Text(text = stringResource(id = item.titleResId))
                    }
                )
            }
        }
    }) { paddingValues ->
        AppNavGraph(
            navHostController = navigationState.navHostController,
            homeScreenContent = {
                HomeScreen(
                    viewModel = viewModel,
                    paddingValues = paddingValues
                )
            },
            favoriteScreenContent = {
                Text(
                    text = "Favorite",
                    color = Color.White,
                    modifier = Modifier.padding(paddingValues)
                )
            },
            profileScreenContent = {
                Text(
                    text = "Profile",
                    color = Color.White,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        )
    }
}

@Preview
@Composable
fun MainScreenPreviewLight() {
    VkNewsClientTheme(darkTheme = false) {
        MainScreen(viewModel())
    }
}

@Preview
@Composable
fun MainScreenPreviewDark() {
    VkNewsClientTheme(darkTheme = true) {
        MainScreen(viewModel())
    }
}
