package com.strezh.vknewsclient.presentation.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.strezh.vknewsclient.R
import com.strezh.vknewsclient.domain.entity.FeedPost
import com.strezh.vknewsclient.domain.entity.StatisticItem
import com.strezh.vknewsclient.domain.entity.StatisticType
import com.strezh.vknewsclient.ui.theme.DarkRed
import com.strezh.vknewsclient.ui.theme.VkNewsClientTheme

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
    onLikeClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit,
) {
    Card(
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            CardPostHeader(feedPost = feedPost)
            Spacer(modifier = Modifier.height(8.dp))
            CardPostText(feedPost = feedPost)
            Spacer(modifier = Modifier.height(8.dp))
            CardPostImage(feedPost = feedPost)
            Spacer(modifier = Modifier.height(8.dp))
            CardPostFooter(
                statistics = feedPost.statistics,
                isLiked = feedPost.isLiked,
                onLikeClickListener = onLikeClickListener,
                onCommentClickListener = onCommentClickListener
            )
        }
    }
}

@Composable
private fun CardPostHeader(
    feedPost: FeedPost
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            model = feedPost.groupImageUrl,
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f)
        ) {
            Text(text = feedPost.groupName, color = MaterialTheme.colorScheme.primary)
            Text(text = feedPost.publicationDate, color = MaterialTheme.colorScheme.secondary)
        }
        Image(
            painter = painterResource(id = R.drawable.ic_more_vert),
            contentDescription = "More button"
        )
    }
}

@Composable
private fun CardPostText(feedPost: FeedPost) {
    Text(text = feedPost.contentText, color = MaterialTheme.colorScheme.secondary)
}

@Composable
private fun CardPostImage(feedPost: FeedPost) {
    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        model = feedPost.contentImageUrl,
        contentDescription = null
    )
}


@Composable
private fun IconWithText(
    iconResId: Int,
    text: String,
    onIconClickListener: (() -> Unit)? = null,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    val modifier = if (onIconClickListener == null) {
        Modifier
    } else {
        Modifier.clickable { onIconClickListener() }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = tint
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun CardPostFooter(
    statistics: List<StatisticItem>,
    isLiked: Boolean,
    onLikeClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit,
) {
    val views = statistics.getItemByType(StatisticType.VIEWS)
    val shares = statistics.getItemByType(StatisticType.SHARES)
    val comments = statistics.getItemByType(StatisticType.COMMENTS)
    val likes = statistics.getItemByType(StatisticType.LIKES)

    Row {
        Row(modifier = Modifier.weight(1f)) {

            IconWithText(
                iconResId = R.drawable.ic_views_count,
                text = formatStatisticItem(views.count),
            )
        }

        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceBetween) {
            IconWithText(
                iconResId = R.drawable.ic_share,
                text = formatStatisticItem(shares.count),
            )
            IconWithText(
                iconResId = R.drawable.ic_comment,
                text = formatStatisticItem(comments.count),
                onIconClickListener = {
                    onCommentClickListener(comments)

                })
            IconWithText(
                iconResId = if (isLiked) R.drawable.ic_like_set else R.drawable.ic_like,
                text = formatStatisticItem(likes.count),
                tint = if (isLiked) DarkRed else MaterialTheme.colorScheme.primary,
                onIconClickListener = {
                    onLikeClickListener(likes)
                })
        }
    }
}

private fun List<StatisticItem>.getItemByType(type: StatisticType): StatisticItem {
    return this.find { it.type == type } ?: throw IllegalStateException("Type not passed")
}

private fun formatStatisticItem(count: Int): String {
    return if (count > 100_000) {
        String.format("%sK", count / 1000)
    } else if (count > 1000) {
        String.format("%.1fK", (count / 1000f))
    } else {
        count.toString()
    }
}


@Preview
@Composable
fun FeedCardPreviewLight() {
    val dummyFeedPost = FeedPost(
        id = 1,
        communityId = 1,
        groupName = "Высокие технологии // Trashbox.ru",
        publicationDate = "12 September 2024, 03:52",
        groupImageUrl = "",
        contentText = "Это сервис для работы над проектами. На интерактивной доске можно делать почти всё что угодно: писать, рисовать, строить сложные схемы, оставлять записки на стикерах и другое. Пользуйтесь досками сами, делитесь ими с коллегами и друзьями. В совместном режиме пользователи видят друг друга, вместе добавляют элементы и оставляют комментарии.В Концепт можно перенести данные из другого подобного сервиса — Miro.",
        contentImageUrl = "https://sun23-2.userapi.com/s/v1/if1/G5ZV4ku4o_KzRsITyyFJ9h4sgs431T_MEAg99xh3h9njPq8nDN7v0QgB6lsuPUCak4ig0HGX.jpg?quality=96&crop=122,140,942,942&as=32x32,48x48,72x72,108x108,160x160,240x240,360x360,480x480,540x540,640x640,720x720&ava=1&cs=200x200",
        statistics = listOf(
            StatisticItem(StatisticType.VIEWS, 7300),
            StatisticItem(StatisticType.LIKES, 51),
            StatisticItem(StatisticType.COMMENTS, 2),
            StatisticItem(StatisticType.SHARES, 7)
        ),
        isLiked = true
    )


    VkNewsClientTheme(darkTheme = false) {
        PostCard(
            feedPost = dummyFeedPost,
            onLikeClickListener = {},
            onCommentClickListener = {})
    }
}

@Preview
@Composable
fun FeedCardPreviewDark() {
    val dummyFeedPost = FeedPost(
        id = 1,
        communityId = 1,
        groupName = "Высокие технологии // Trashbox.ru",
        publicationDate = "12 September 2024, 03:52",
        groupImageUrl = "https://sun9-6.userapi.com/impg/pOTYIVzX6Y7T5VOe_sGB9kBDnW0ul6hUzG0oXg/kG8fiq8XpQo.jpg?size=768x768&quality=96&sign=5ecd28078d79b981d7c257774eaa80c8&type=album",
        contentText = "Это сервис для работы над проектами. На интерактивной доске можно делать почти всё что угодно: писать, рисовать, строить сложные схемы, оставлять записки на стикерах и другое. Пользуйтесь досками сами, делитесь ими с коллегами и друзьями. В совместном режиме пользователи видят друг друга, вместе добавляют элементы и оставляют комментарии.В Концепт можно перенести данные из другого подобного сервиса — Miro.",
        contentImageUrl = "https://sun9-47.userapi.com/s/v1/ig2/IRW9iBUceMsWdDcUpuhSQL1PsMBeeU_TaZ8Q7U3hWf_ToAH4r8d9c0JLo8AZfIZ1JAHmrZwePQQCHWF9vUJrSaiM.jpg?quality=95&as=32x40,48x60,72x90,108x135,160x200,240x300,360x450,480x600,540x675,640x800,720x900,1080x1350,1280x1600,1440x1800,1920x2400&from=bu&u=sNM83r2b0AHYwIISJbOl0Nim46RRz_7p5BkYnX55swA&cs=646x807",
        statistics = listOf(
            StatisticItem(StatisticType.VIEWS, 7300),
            StatisticItem(StatisticType.LIKES, 51),
            StatisticItem(StatisticType.COMMENTS, 2),
            StatisticItem(StatisticType.SHARES, 7)
        ),
        isLiked = false
    )

    VkNewsClientTheme(darkTheme = true) {
        PostCard(
            feedPost = dummyFeedPost,
            onLikeClickListener = {},
            onCommentClickListener = {})
    }
}