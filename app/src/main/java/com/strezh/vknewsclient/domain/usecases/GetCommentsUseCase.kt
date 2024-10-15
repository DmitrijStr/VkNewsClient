package com.strezh.vknewsclient.domain.usecases

import com.strezh.vknewsclient.domain.entity.FeedPost
import com.strezh.vknewsclient.domain.entity.PostComment
import com.strezh.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(feedPost: FeedPost): StateFlow<List<PostComment>> {
        return repository.getComments(feedPost)
    }
}