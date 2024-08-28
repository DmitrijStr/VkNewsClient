package com.example.vknewsclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.ui.NavigationItem
import com.example.vknewsclient.ui.PostCard
import com.example.vknewsclient.ui.theme.VkNewsClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VkNewsClientTheme {
                MainScreen()
            }
        }
    }
}

data class NavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val feedPost = remember {
        mutableStateOf(FeedPost())
    }

    val listItems = listOf(
        NavItem("Favorite", Icons.Filled.Favorite, Icons.Outlined.Favorite),
        NavItem("Edit", Icons.Filled.Edit, Icons.Outlined.Edit),
        NavItem("Delete", Icons.Filled.Delete, Icons.Outlined.Delete),
    )
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    ModalNavigationDrawer(drawerContent = {
        ModalDrawerSheet {
            listItems.forEachIndexed { index, navItem ->
                NavigationDrawerItem(
                    label = { Text(text = navItem.title) },
                    selected = selectedItemIndex == index,
                    icon = {
                        Icon(
                            imageVector = if (selectedItemIndex == index) navItem.selectedIcon
                            else navItem.unselectedIcon, contentDescription = navItem.title
                        )
                    },
                    onClick = { selectedItemIndex = index })
            }
        }
    }) {
        Scaffold(
            modifier = Modifier.padding(8.dp),
            topBar = {
                TopAppBar(title = { Text(text = "Top App Bar title") }, navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = null)
                    }
                })
            },
            bottomBar = {
                NavigationBar {
                    val selectedItemPosition = remember {
                        mutableIntStateOf(0)
                    }
                    val items = listOf(
                        NavigationItem.Home,
                        NavigationItem.Favorite,
                        NavigationItem.Profile
                    )

                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemPosition.intValue == index,
                            onClick = { selectedItemPosition.intValue = index },
                            icon = {
                                Icon(item.icon, contentDescription = null)
                            },
                            label = {
                                Text(text = stringResource(id = item.titleResId))
                            },
                        )
                    }
                }
            })
        { padding ->
            PostCard(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 8.dp),
                feedPost = feedPost.value,
                onFooterIconClickListener = { newItem ->
                    val oldStatistics = feedPost.value.statistics

                    val newStats = oldStatistics.toMutableList().apply {
                        replaceAll { oldItem ->
                            if (oldItem.type == newItem.type) {
                                oldItem.copy(count = oldItem.count + 1)
                            } else {
                                oldItem
                            }
                        }
                    }

                    feedPost.value = feedPost.value.copy(statistics = newStats)
                }
            )
        }
    }
}

@Preview
@Composable
fun MainScreenPreviewLight() {
    VkNewsClientTheme(darkTheme = false) {
        MainScreen()
    }
}


@Preview
@Composable
fun MainScreenPreviewDark() {
    VkNewsClientTheme(darkTheme = true) {
        MainScreen()
    }
}
