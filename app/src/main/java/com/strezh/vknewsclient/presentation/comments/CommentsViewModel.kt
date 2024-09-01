package com.strezh.vknewsclient.presentation.comments

import androidx.lifecycle.ViewModel
import com.strezh.vknewsclient.domain.FeedPost
import com.strezh.vknewsclient.domain.PostComment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CommentsViewModel(
    feedPost: FeedPost
) : ViewModel() {

    private val _screenState = MutableStateFlow<CommentsScreenState>(CommentsScreenState.Initial)
    val screenState: StateFlow<CommentsScreenState> = _screenState

    private fun loadComments(feedPost: FeedPost) {
        val comments = List(10) { PostComment(id = it) }

        _screenState.value = CommentsScreenState.Comments(feedPost, comments)
    }

    init {
        loadComments(feedPost)
    }
}