package com.example.vknewsclient

import androidx.lifecycle.ViewModel
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.StatisticItem
import com.example.vknewsclient.ui.NavigationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val feed = List<FeedPost>(10) { index -> FeedPost(id = index) }

    val _feedState = MutableStateFlow(feed)
    val feedState: StateFlow<List<FeedPost>> = _feedState

    private val _selectedNavItem = MutableStateFlow<NavigationItem>(NavigationItem.Home)
    val selectedNavItem: StateFlow<NavigationItem> = _selectedNavItem


    fun selectNavItem(item: NavigationItem) {
        _selectedNavItem.value = item
    }

    fun updateCount(feedPost: FeedPost, item: StatisticItem) {
        val oldPosts = _feedState.value.toMutableList()
        val oldStatistics = feedPost.statistics
        val newStatistics = oldStatistics.toMutableList().apply {
            replaceAll { oldItem ->
                if (oldItem.type == item.type) {
                    oldItem.copy(count = oldItem.count + 1)
                } else {
                    oldItem
                }
            }
        }
        val newFeedPost = feedPost.copy(statistics = newStatistics)
        _feedState.value = oldPosts.apply {
            replaceAll {
                if (it.id == newFeedPost.id) {
                    newFeedPost
                } else {
                    it
                }
            }
        }
    }

    fun remove(feedPost: FeedPost) {
        val oldPosts = _feedState.value.toMutableList()
        oldPosts.remove(feedPost)
        _feedState.value = oldPosts
    }
}