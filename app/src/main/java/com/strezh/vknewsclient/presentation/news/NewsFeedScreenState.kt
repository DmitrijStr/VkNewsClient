package com.strezh.vknewsclient.presentation.news

import com.strezh.vknewsclient.domain.entity.FeedPost

sealed class NewsFeedScreenState {

    data object Initial : NewsFeedScreenState()

    data object Loading : NewsFeedScreenState()

    data class Posts(
        val posts: List<FeedPost>,
        val nextDataIsLoading: Boolean = false
    ) :
        NewsFeedScreenState()
}