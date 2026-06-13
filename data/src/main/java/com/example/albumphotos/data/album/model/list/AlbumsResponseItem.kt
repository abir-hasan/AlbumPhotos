package com.example.albumphotos.data.album.model.list

import com.google.gson.annotations.SerializedName

data class AlbumsResponseItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("userId")
    val userId: Int,
)
