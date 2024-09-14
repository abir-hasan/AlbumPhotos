package com.example.albumphotos.navigation

import kotlinx.serialization.Serializable

@Serializable
object Albums

@Serializable
data class Photos(
    val albumId: String,
)
