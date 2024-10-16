package com.strezh.vknewsclient.presentation.comments

import com.strezh.vknewsclient.domain.entity.FeedPost
import com.strezh.vknewsclient.domain.entity.PostComment

sealed class CommentsScreenState {

    object Initial: CommentsScreenState()

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ) : CommentsScreenState()
}