package com.strezh.vknewsclient.domain.repository

import com.strezh.vknewsclient.domain.entity.FeedPost
import com.strezh.vknewsclient.domain.entity.PostComment
import com.strezh.vknewsclient.domain.entity.AuthState
import kotlinx.coroutines.flow.StateFlow

interface NewsFeedRepository {

    fun getAuthStateFlow(): StateFlow<AuthState>

    fun getRecommendations(): StateFlow<List<FeedPost>>

    fun getComments(feedPost: FeedPost): StateFlow<List<PostComment>>

    suspend fun addLike(feedPost: FeedPost)

    suspend fun deleteLike(feedPost: FeedPost)

    suspend fun ignoreItem(feedPost: FeedPost)

    suspend fun loadNextData()

    suspend fun checkAuthState()
}