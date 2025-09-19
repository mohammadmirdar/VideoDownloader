package com.mirdar.videodownloader.com.mirdar.videodownloader.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.mirdar.videodownloader.R
import com.mirdar.videodownloader.navigation.Screen
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import com.mirdar.designsystem.R as designsystemR

sealed class Menu {

    @Stable
    @Serializable
    data class MenuItem(
        val screen: Screen,
        @StringRes val title: Int,
        val icon: Pair<Int, Int>,
    ) : Menu()

    data object Items {
        val home = MenuItem(
            screen = Screen.Home,
            title = R.string.home,
            icon = Pair(designsystemR.drawable.ic_home_outline, designsystemR.drawable.ic_home_fill)
        )

        val history = MenuItem(
            screen = Screen.History,
            title = R.string.history,
            icon = Pair(
                designsystemR.drawable.ic_clock_outline,
                designsystemR.drawable.ic_clock_fill
            ),
        )

        val favorite = MenuItem(
            screen = Screen.Favorite,
            title = R.string.favorite,
            icon = Pair(
                designsystemR.drawable.ic_heart_outline,
                designsystemR.drawable.ic_heart_fill
            ),
        )
    }
}

fun getMenus()  = persistentListOf(
    Menu.Items.home,
    Menu.Items.history,
    Menu.Items.favorite,
)