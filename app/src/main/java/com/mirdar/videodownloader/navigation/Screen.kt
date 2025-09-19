package com.mirdar.videodownloader.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home: Screen()
    @Serializable
    data object History: Screen()
    @Serializable
    data object Favorite: Screen()
}