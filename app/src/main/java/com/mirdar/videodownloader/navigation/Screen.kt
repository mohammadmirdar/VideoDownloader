package com.mirdar.videodownloader.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    data object Home: Screen()
}