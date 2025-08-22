package com.mirdar.videodownloader.data.home.model

import com.mirdar.videodownloader.domain.home.model.VideoInfoModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class VideoInfoResponse(
    @Json(name = "description") val description: String,
    @Json(name = "direct_url") val directUrl: String,
    @Json(name = "thumbnail") val thumbnail: String,
    @Json(name = "title") val title: String
)

fun VideoInfoResponse.toDomain() = VideoInfoModel(
    description = description,
    directUrl = directUrl,
    thumbnail = thumbnail,
    title = title
)
