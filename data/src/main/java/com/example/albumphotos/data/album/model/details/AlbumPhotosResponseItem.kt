package com.example.albumphotos.data.album.model.details

import com.google.gson.annotations.SerializedName

data class AlbumPhotosResponseItem(
    @SerializedName("albumId")
    val albumId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String,
)
