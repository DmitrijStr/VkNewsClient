package com.strezh.vknewsclient.domain

import com.strezh.vknewsclient.R

data class FeedPost(
    val id: Int = 0,
    val groupName: String = "dev/null",
    val publicationDate: String = "14:00",
    val avatarResId: Int = R.drawable.post_comunity_thumbnail,
    val contentText: String = "Кабаныч когда узнал, что если сотрудникам не платить они начинают умирать от голода",
    val contentImageResId: Int = R.drawable.post_content_image,
    val statistics: List<StatisticItem> = listOf(
        StatisticItem(StatisticType.VIEWS, 966),
        StatisticItem(StatisticType.SHARES, 7),
        StatisticItem(StatisticType.COMMENTS, 8),
        StatisticItem(StatisticType.LIKES, 27)
    )
)