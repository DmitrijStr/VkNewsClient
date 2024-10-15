package com.strezh.vknewsclient.domain.usecases

import com.strezh.vknewsclient.domain.entity.FeedPost
import com.strezh.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(feedPost: FeedPost) {
        repository.ignoreItem(feedPost)
    }
}