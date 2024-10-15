package com.strezh.vknewsclient.data.repository

import android.app.Application
import com.strezh.vknewsclient.data.mapper.CommentsMapper
import com.strezh.vknewsclient.data.mapper.NewsFeedMapper
import com.strezh.vknewsclient.data.network.ApiFactory
import com.strezh.vknewsclient.data.network.ApiService
import com.strezh.vknewsclient.domain.entity.FeedPost
import com.strezh.vknewsclient.domain.entity.PostComment
import com.strezh.vknewsclient.domain.entity.StatisticItem
import com.strezh.vknewsclient.domain.entity.StatisticType
import com.strezh.vknewsclient.extensions.mergeWith
import com.strezh.vknewsclient.domain.entity.AuthState
import com.strezh.vknewsclient.domain.repository.NewsFeedRepository
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class NewsFeedRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: NewsFeedMapper,
    private val commentsMapper: CommentsMapper,
    private val storage: VKPreferencesKeyValueStorage,
) : NewsFeedRepository {

    private val token
        get() = VKAccessToken.restore(storage)

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private val checkAuthStateEvents = MutableSharedFlow<Unit>(replay = 1)
    private val authStateFlow: StateFlow<AuthState> = flow {
        checkAuthStateEvents.emit(Unit)
        checkAuthStateEvents.collect {
            val currentToken = token
            val loggedIn = currentToken != null && currentToken.isValid
            val state = if (loggedIn) AuthState.Authorized else AuthState.NonAuthorized
            emit(state)
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = AuthState.Initial
    )

    private var nextFrom: String? = null

    private val loadedListFlow = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            val startFrom = nextFrom

            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
                return@collect
            }

            val response = if (startFrom == null) {
                apiService.loadRecommendations(getAccessToken())
            } else {
                apiService.loadRecommendations(getAccessToken(), startFrom)
            }

            val posts = mapper.mapResponseToPosts(response)
            _feedPosts.addAll(posts)
            nextFrom = response.newsFeedContent.nextFrom
            emit(feedPosts)
        }
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.catch {

    }

    private val recommendations: StateFlow<List<FeedPost>> = loadedListFlow
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts
        )

    override suspend fun checkAuthState() {
        checkAuthStateEvents.emit(Unit)
    }

    override suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
    }

    override suspend fun addLike(feedPost: FeedPost) {
        val response = apiService.addLike(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )

        val newLikesCount = response.likes.count
        updateLikesCount(feedPost, true, newLikesCount)
    }

    override suspend fun deleteLike(feedPost: FeedPost) {
        val response = apiService.deleteLike(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )

        val newLikesCount = response.likes.count
        updateLikesCount(feedPost, false, newLikesCount)
    }

    override suspend fun ignoreItem(feedPost: FeedPost) {
        apiService.ignoreFeedPost(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )

        _feedPosts.remove(feedPost)
        refreshedListFlow.emit(feedPosts)
    }

    override fun getAuthStateFlow(): StateFlow<AuthState> {
        return authStateFlow
    }

    override fun getRecommendations(): StateFlow<List<FeedPost>> {
        return recommendations
    }

    override fun getComments(feedPost: FeedPost): StateFlow<List<PostComment>> = flow {
        val response = apiService.getComments(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )
        emit(commentsMapper.mapResponseToComments(response))
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

    private suspend fun updateLikesCount(feedPost: FeedPost, isLiked: Boolean, newLikesCount: Int) {
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(type = StatisticType.LIKES, newLikesCount))
        }
        val newPost = feedPost.copy(statistics = newStatistics, isLiked = isLiked)
        _feedPosts[_feedPosts.indexOf(feedPost)] = newPost
        refreshedListFlow.emit(feedPosts)
    }

    private fun getAccessToken(): String {
        return token?.accessToken ?: throw IllegalStateException("Token is null")
    }

    companion object {
        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}