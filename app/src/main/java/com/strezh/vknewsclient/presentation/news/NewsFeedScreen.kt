package com.strezh.vknewsclient.presentation.news

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.strezh.vknewsclient.domain.entity.FeedPost
import com.strezh.vknewsclient.presentation.getApplicationComponent
import com.strezh.vknewsclient.ui.theme.DarkBlue

@Composable
fun NewsFeedScreen(
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit
) {
    val component = getApplicationComponent()
    val viewModel: NewsFeedViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.screenState.collectAsState(NewsFeedScreenState.Initial)
    Log.d("RECOMPOSITION", "NewsFeedScreen")
    Log.d("RECOMPOSITION", "paddingValues $paddingValues")

    NewsFeedScreenContent(
        screenState = screenState,
        viewModel = viewModel,
        paddingValues = paddingValues,
        onCommentClickListener = onCommentClickListener
    )
}

@Composable
fun NewsFeedScreenContent(
    screenState: State<NewsFeedScreenState>,
    paddingValues: PaddingValues,
    onCommentClickListener: (feedPost: FeedPost) -> Unit,
    viewModel: NewsFeedViewModel,
) {
    when (val currentState = screenState.value) {
        is NewsFeedScreenState.Posts -> {
            FeedPosts(
                paddingValues = paddingValues,
                viewModel = viewModel,
                posts = currentState.posts,
                onCommentClickListener = onCommentClickListener,
                nextDataIsLoading = currentState.nextDataIsLoading
            )
        }

        is NewsFeedScreenState.Initial -> {

        }

        is NewsFeedScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = DarkBlue)
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    paddingValues: PaddingValues,
    viewModel: NewsFeedViewModel,
    posts: List<FeedPost>,
    onCommentClickListener: (feedPost: FeedPost) -> Unit,
    nextDataIsLoading: Boolean,
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
                    onLikeClickListener = { _ ->
                        viewModel.changeLikeStatus(feedPost = post)
                    }
                )
            }

            Spacer(modifier = Modifier.padding(top = 16.dp))
        }

        item {
            if (nextDataIsLoading) {
                Box(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center

                ) {
                    CircularProgressIndicator(color = DarkBlue)
                }
            } else {
                SideEffect {
                    viewModel.loadNextRecommendations()
                }
            }
        }
    }
}