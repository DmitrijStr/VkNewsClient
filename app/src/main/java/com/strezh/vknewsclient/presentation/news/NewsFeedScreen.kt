package com.strezh.vknewsclient.presentation.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.strezh.vknewsclient.domain.FeedPost

@Composable
fun NewsFeedScreen(paddingValues: PaddingValues, onCommentClickListener: (feedPost: FeedPost) -> Unit) {
    val viewModel: NewsFeedViewModel = viewModel()

    val screenState = viewModel.screenState.collectAsState()

    when (val currentState = screenState.value) {
        is NewsFeedScreenState.Posts -> {
            FeedPosts(
                paddingValues = paddingValues,
                viewModel = viewModel,
                posts = currentState.posts,
                onCommentClickListener = onCommentClickListener
            )
        }

        NewsFeedScreenState.Initial -> {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    paddingValues: PaddingValues,
    viewModel: NewsFeedViewModel,
    posts: List<FeedPost>,
    onCommentClickListener: (feedPost: FeedPost) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        items(posts, key = { it.id }) { post ->
            val state = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    when (value) {
                        SwipeToDismissBoxValue.StartToEnd -> {
                            viewModel.remove(post)
                        }

                        SwipeToDismissBoxValue.EndToStart -> {

                        }

                        SwipeToDismissBoxValue.Settled -> {

                        }
                    }
                    return@rememberSwipeToDismissBoxState true
                },

                positionalThreshold = { it * .25f }
            )

            SwipeToDismissBox(
                modifier = Modifier.animateItemPlacement(),
                state = state,
                backgroundContent = { },
                enableDismissFromEndToStart = false
            )
            {
                PostCard(modifier = Modifier.padding(horizontal = 8.dp),
                    feedPost = post,
                    onCommentClickListener = { onCommentClickListener(post) },
                    onLikeClickListener = { statisticItem ->
                        viewModel.updateCount(post, statisticItem)
                    },
                    onShareClickListener = { statisticItem ->
                        viewModel.updateCount(post, statisticItem)
                    },
                    onViewsClickListener = { statisticItem ->
                        viewModel.updateCount(post, statisticItem)
                    })
            }

            Spacer(modifier = Modifier.padding(top = 16.dp))
        }
    }
}