package com.strezh.vknewsclient.domain.entity

import androidx.compose.runtime.Immutable

@Immutable
data class FeedPost(
    val id: Long,
    val communityId: Long,
    val groupName: String,
    val publicationDate: String,
    val groupImageUrl: String,
    val contentText: String,
    val contentImageUrl: String?,
    val statistics: List<StatisticItem>,
    val isLiked: Boolean,
)