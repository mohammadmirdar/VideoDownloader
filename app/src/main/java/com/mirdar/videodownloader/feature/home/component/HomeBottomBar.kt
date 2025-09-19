package com.mirdar.videodownloader.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.mirdar.designsystem.theme.VideoDownloaderTheme
import com.mirdar.videodownloader.com.mirdar.videodownloader.navigation.Menu
import com.mirdar.videodownloader.com.mirdar.videodownloader.navigation.getMenus
import com.mirdar.videodownloader.navigation.Screen
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun HomeBottomBar(
    menus: ImmutableList<Menu.MenuItem>,
    currentDestination: NavDestination?,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = VideoDownloaderTheme.colors.white,
        contentColor = VideoDownloaderTheme.colors.gray
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 30.dp)
        ) {
            menus.forEach { item ->
                val isSelected = currentDestination?.hierarchy?.any {
                    it.route?.contains(item.screen.javaClass.simpleName, ignoreCase = true) == true
                } == true

                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigate(item.screen) },
                    icon = {
                        NavigationIcon(
                            item = item,
                            isSelected = isSelected
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun NavigationIcon(
    item: Menu.MenuItem,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val (outline, fill) = item.icon
    val icon = if (isSelected) fill else outline

    Icon(
        modifier = modifier,
        painter = painterResource(id = icon),
        tint = VideoDownloaderTheme.colors.black,
        contentDescription = null
    )
}

@Composable
@Preview
private fun HomeBottomBarPreview() {
    VideoDownloaderTheme {
        HomeBottomBar(
            menus = getMenus(),
            currentDestination = null,
            onNavigate = {}
        )
    }
}