package com.mirdar.videodownloader.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class UserAgent(
    @Json(name = "sdk") val sdk: Int,
    @Json(name = "s") val store: String,
    @Json(name = "di") val device: String,
    @Json(name = "loc") val locale: String,
    @Json(name = "an") val appName: String,
    @Json(name = "vc") val versionCode: Int,
    @Json(name = "sz") val screenSize: String,
    @Json(name = "vn") val versionName: String,
    @Json(name = "os") val os: String = "Android",
)
