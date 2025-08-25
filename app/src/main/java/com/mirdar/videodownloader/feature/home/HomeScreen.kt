package com.mirdar.videodownloader.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mirdar.designsystem.components.GradientButton
import com.mirdar.designsystem.components.GradientOutlineButton
import com.mirdar.designsystem.theme.VideoDownloaderTheme
import com.mirdar.videodownloader.feature.home.component.HomeBottomBar
import com.mirdar.videodownloader.feature.home.component.HomeTopBar
import com.mirdar.videodownloader.feature.home.component.LatestDownloadList

@Composable
fun HomeScreen(
) {
    Scaffold(containerColor = VideoDownloaderTheme.colors.white, topBar = {
        HomeTopBar(modifier = Modifier)
    }, bottomBar = {
        HomeBottomBar()
    }) { paddingValues ->
        HomeContent(
            onSubmitClick = {}, modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun HomeContent(onSubmitClick: (String) -> Unit, modifier: Modifier = Modifier) {
    var textValue by remember { mutableStateOf("") }

    Column(
        modifier = modifier.padding(horizontal = 26.dp)
    ) {
        Spacer(Modifier.height(26.dp))

        OutlinedTextField(
            value = textValue, onValueChange = { textValue = it }, placeholder = {
                Text(
                    text = "Paste Link here"
                )
            }, shape = RoundedCornerShape(size = 20.dp), colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = VideoDownloaderTheme.colors.white,
                unfocusedBorderColor = VideoDownloaderTheme.colors.gray
            ), modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        ButtonRow()

        Spacer(Modifier.height(42.dp))

        LatestDownloadList()

        Spacer(Modifier.height(45.dp))


    }
}

@Composable
fun ButtonRow(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        GradientOutlineButton(
            onClick = {}, text = "Paste link", modifier = Modifier.weight(1f)
        )

        GradientButton(
            text = "Download", isSelected = true, onClick = {}, modifier = Modifier.weight(1f)
        )
    }
}

@Composable
@Preview(device = "id:pixel_8_pro")
private fun HomeScreenPreview() {
    VideoDownloaderTheme {
        HomeScreen()
    }
}