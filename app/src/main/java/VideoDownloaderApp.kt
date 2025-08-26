package com.mirdar.videodownloader

import android.app.Application
import com.adivery.sdk.Adivery
import com.mirdar.videodownloader.model.AppConfig
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class VideoDownloaderApp : Application() {

    @Inject
    lateinit var appConfig: AppConfig

    override fun onCreate() {
        super.onCreate()

        Adivery.configure(this, appConfig.adiveryId)

        Adivery.prepareRewardedAd(this, appConfig.adiveryRewardId)
    }
}