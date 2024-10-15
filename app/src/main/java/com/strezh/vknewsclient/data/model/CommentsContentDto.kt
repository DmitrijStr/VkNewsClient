package com.strezh.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class CommentsContentDto(
    @SerializedName("items")
    val items: List<CommentDto>,
    @SerializedName("profiles")
    val profiles: List<ProfileDto>
)