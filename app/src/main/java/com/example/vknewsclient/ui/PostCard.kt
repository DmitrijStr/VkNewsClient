package com.example.vknewsclient.ui

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vknewsclient.R
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.StatisticItem
import com.example.vknewsclient.domain.StatisticType
import com.example.vknewsclient.ui.theme.VkNewsClientTheme

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
    onLikeClickListener: (StatisticItem) -> Unit,
    onShareClickListener: (StatisticItem) -> Unit,
    onViewsClickListener: (StatisticItem) -> Unit,
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
                onViewsClickListener = onViewsClickListener,
                onShareClickListener = onShareClickListener,
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
        Image(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            painter = painterResource(id = feedPost.avatarResId),
            contentDescription = "post icon"
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
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        contentScale = ContentScale.FillWidth,
        painter = painterResource(id = feedPost.contentImageResId),
        contentDescription = ""
    )
}


@Composable
private fun IconWithText(
    iconResId: Int,
    text: String,
    onIconClickListener: () -> Unit
) {
    Row(
        modifier = Modifier.clickable { onIconClickListener() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun CardPostFooter(
    statistics: List<StatisticItem>,
    onLikeClickListener: (StatisticItem) -> Unit,
    onShareClickListener: (StatisticItem) -> Unit,
    onViewsClickListener: (StatisticItem) -> Unit,
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
                text = views.count.toString(),
                onIconClickListener = {
                    onViewsClickListener(views)
                })
        }

        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceBetween) {
            IconWithText(
                iconResId = R.drawable.ic_share,
                text = shares.count.toString(),
                onIconClickListener = {
                    onShareClickListener(shares)

                })
            IconWithText(
                iconResId = R.drawable.ic_comment,
                text = comments.count.toString(),
                onIconClickListener = {
                    onCommentClickListener(comments)

                })
            IconWithText(
                iconResId = R.drawable.ic_like,
                text = likes.count.toString(),
                onIconClickListener = {
                    onLikeClickListener(likes)
                })
        }
    }
}

private fun List<StatisticItem>.getItemByType(type: StatisticType): StatisticItem {
    return this.find { it.type == type } ?: throw IllegalStateException("Type not passed")
}


@Preview
@Composable
fun FeedCardPreviewLight() {
    VkNewsClientTheme(darkTheme = false) {
        PostCard(
            feedPost = FeedPost(),
            onLikeClickListener = {},
            onShareClickListener = {},
            onViewsClickListener = {},
            onCommentClickListener = {})
    }
}

@Preview
@Composable
fun FeedCardPreviewDark() {
    VkNewsClientTheme(darkTheme = true) {
        PostCard(
            feedPost = FeedPost(),
            onLikeClickListener = {},
            onShareClickListener = {},
            onViewsClickListener = {},
            onCommentClickListener = {})
    }
}