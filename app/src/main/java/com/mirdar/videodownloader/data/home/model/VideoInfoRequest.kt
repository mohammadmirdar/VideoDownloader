package com.mirdar.videodownloader.data.home.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class VideoInfoRequest(
    @Json(name = "url") val url: String
)
