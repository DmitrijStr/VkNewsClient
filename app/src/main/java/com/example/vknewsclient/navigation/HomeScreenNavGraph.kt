package com.example.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.vknewsclient.domain.FeedPost

fun NavGraphBuilder.homeScreenNavGraph(
    commentsScreenContent: @Composable (feedPost: FeedPost) -> Unit,
    newsFeedScreenContent: @Composable () -> Unit,
) {
    navigation(
        startDestination = Screen.NewsFeed.route,
        route = Screen.Home.route,
        builder = {
            composable(Screen.NewsFeed.route) {
                newsFeedScreenContent()
            }
            composable(
                route = Screen.Comments.route,
                arguments = listOf(
                    navArgument(Screen.KEY_FEED_POST_ID) {
                        type = NavType.IntType
                    },
                )) {
                val feedPostId = it.arguments?.getInt(Screen.KEY_FEED_POST_ID) ?: 0

                commentsScreenContent(FeedPost(id = feedPostId))
            }
        }
    )
}