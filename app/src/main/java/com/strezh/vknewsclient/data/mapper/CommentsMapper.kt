package com.strezh.vknewsclient.data.mapper

import com.strezh.vknewsclient.data.model.CommentsResponseDto
import com.strezh.vknewsclient.domain.entity.PostComment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class CommentsMapper @Inject constructor() {
    fun mapResponseToComments(commentsResponseDto: CommentsResponseDto): List<PostComment> {
        val result = mutableListOf<PostComment>()

        val comments = commentsResponseDto.comments.items
        val profiles = commentsResponseDto.comments.profiles

        for (comment in comments) {
            if (comment.text.isBlank()) continue

            val user = profiles.find { it.id == comment.authorId } ?: continue

            val postComments = PostComment(
                id = comment.id,
                authorAvatarUrl = user.avatarUrl,
                authorName = "${user.firstName} ${user.lastName}",
                commentText = comment.text,
                publicationDate = mapTimeStampToDate(comment.date)
            )

            result.add(postComments)
        }

        return result
    }

    private fun mapTimeStampToDate(timeStamp: Long): String {
        val date = Date(timeStamp * 1000)

        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }

}