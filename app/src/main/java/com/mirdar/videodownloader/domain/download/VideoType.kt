package com.mirdar.videodownloader.domain.download

sealed class VideoType {
    object Progressive : VideoType()
    object Hls : VideoType()
}