package com.strezh.vknewsclient.domain.usecases

import com.strezh.vknewsclient.domain.entity.FeedPost
import com.strezh.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class ChangeLikeStatusUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {

    suspend fun addLike(feedPost: FeedPost) {
        repository.addLike(feedPost)
    }

    suspend fun deleteLike(feedPost: FeedPost) {
        repository.deleteLike(feedPost)
    }
}